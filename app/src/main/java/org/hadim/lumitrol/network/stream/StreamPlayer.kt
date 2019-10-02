package org.hadim.lumitrol.network.stream

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*
import java.util.concurrent.Executors


/*
 * From https://github.com/silaslenz/Lumimote/blob/21ba1bf00ae5d39f7d583a3bb7327182a8371957/app/src/main/java/se/silenz/lumimote/MainActivity.kt
 */
class StreamPlayer(
    private val imageConsumer: ((Bitmap) -> Unit),
    private val onStreaming: (() -> Unit)?,
    private val onLoading: (() -> Unit)?,
    private val onFailure: ((Exception) -> Unit)?,
    ipAddress: String,
    private val udpPort: Int
) : Runnable {

    companion object {
        const val TAG: String = "StreamPlayer"
    }

    private var socket: DatagramSocket? = null
    private val address: InetAddress = InetAddress.getByName(ipAddress)

    private val imageExecutor = Executors.newSingleThreadScheduledExecutor()

    /*
     * TODO: design a more efficient way using Stream maybe?
     *  Current implementation has some lag.
     */
    override fun run() {

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)

        socket = null
        try {
            socket = DatagramSocket(udpPort)
            //socket?.soTimeout = 100
        } catch (error: Exception) {
            error.message?.let {
                Log.e("$TAG/run", "Can't create the socket for streaming: ${error.message}")
            }
            onFailure?.let { it(error) }
        }

        onLoading?.let { it() }

        socket?.let { socket ->

            // The camera sends each image in one UDP packet, normally between 25000 and 30000 bytes.
            // We set 35000 here to be safe.
            var udpPacketBuffer: ByteArray = ByteArray(35000)
            var currentFrame: Bitmap?

            var receivedPacket: DatagramPacket? = null

            currentFrame = null
            while (!Thread.interrupted()) {
                try {

                    receivedPacket = DatagramPacket(
                        udpPacketBuffer, udpPacketBuffer.size,
                        address, udpPort
                    )

                    socket?.receive(receivedPacket)

                    // TODO: some lag happen. Maybe because of getImage being run
                    //  in the same thread as receive?

                    imageExecutor.submit {
                        currentFrame = getImage(receivedPacket)
                        currentFrame?.let { currentFrame ->
                            onStreaming?.let { it() }
                            imageConsumer(currentFrame)
                        }
                    }

                } catch (error: Exception) {
                    error.message?.let { Log.e("$TAG/run", it) }
                    onLoading?.let { it() }
                }
            }
        }

        imageExecutor.shutdown()
        socket?.close()
        Thread.interrupted()
    }

    private fun getImage(receivedPacket: DatagramPacket): Bitmap? {

        var currentFrame: Bitmap? = null
        try {
            var imageData = getImageData(receivedPacket)
            currentFrame = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)

        } catch (error: Exception) {
            error.message?.let { Log.e("$TAG/getImage", it) }
            onLoading?.let { it() }
        }

        return currentFrame
    }

    private fun getImageData(receivedPacket: DatagramPacket): ByteArray {
        val udpData = receivedPacket.data
        // The camera adds some kind of header to each packet, which we need to ignore
        val videoDataStart = getImageDataStart(receivedPacket, udpData)
        return Arrays.copyOfRange(udpData, videoDataStart, receivedPacket.length)
    }

    private fun getImageDataStart(receivedPacket: DatagramPacket, udpData: ByteArray): Int {
        var videoDataStart = 130

        // The image data starts somewhere after the first 130 bytes, but at last in 320 bytes
        var k = 130
        while (k < 320 && k < receivedPacket.length - 1) {
            // The bytes FF and D8 signify the start of the jpeg data, see https://en.wikipedia.org/wiki/JPEG_File_Interchange_Format
            if (udpData[k] == 0xFF.toByte() && udpData[k + 1] == 0xD8.toByte()) {
                videoDataStart = k
            }
            k++
        }
        return videoDataStart
    }

//    private fun bytesToInt(byte1: Byte, byte2: Byte) =
//        ((byte1.toInt() and 255) shl 8) or (byte2.toInt() and 255)

}