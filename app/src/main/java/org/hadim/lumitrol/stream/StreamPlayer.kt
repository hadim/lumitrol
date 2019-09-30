package org.hadim.lumitrol.stream

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.concurrent.Executors


/*
 * From https://github.com/silaslenz/Lumimote/blob/21ba1bf00ae5d39f7d583a3bb7327182a8371957/app/src/main/java/se/silenz/lumimote/MainActivity.kt
 */
class StreamPlayer(
    private val imageConsumer: ((Bitmap) -> Unit),
    private val onStreaming: (() -> Unit),
    private val onLoading: (() -> Unit),
    private val onFailure: ((Exception) -> Unit),
    ipAddress: String,
    private val udpPort: Int
) : Runnable {

    companion object {
        const val TAG: String = "StreamPlayer"
    }

    private var socket: DatagramSocket? = null
    private val address: InetAddress = InetAddress.getByName(ipAddress)

    private val imageExecutor = Executors.newCachedThreadPool()

    override fun run() {

        socket = null
        try {
            socket = DatagramSocket(udpPort)
            socket?.soTimeout = 100
        } catch (error: Exception) {
            error.message?.let {
                Log.e("$TAG/run", "Can't create the socket for streaming: ${error.message}")
            }
            onFailure(error)
        }

        onLoading()

        socket?.let {
            // The camera sends each image in one UDP packet, normally between 25000 and 30000 bytes. We set 35000 here to be safe.
            val udpPacketBuffer = ByteArray(35000)
            var currentFrame: Bitmap?

            val receivedPacket = DatagramPacket(
                udpPacketBuffer, udpPacketBuffer.size,
                address, udpPort
            )

            while (!Thread.interrupted()) {
                try {
                    socket?.receive(receivedPacket)
                    currentFrame = getImage(receivedPacket)

                    imageExecutor.submit {
                        currentFrame?.let { currentFrame ->
                            onStreaming()
                            imageConsumer(currentFrame)
                        }
                    }
                } catch (error: Exception) {
                    error.message?.let { Log.e("$TAG/run", it) }
                    onLoading()
                }
            }
        }

        imageExecutor.shutdown()
        socket?.close()
    }

    private fun getImage(receivedPacket: DatagramPacket): Bitmap? {

        var currentFrame: Bitmap? = null
        try {
            // https://gist.github.com/FWeinb/2d51fe63d0f9f5fc4d32d8a420b2c18d
//            val additionalMetadata = bytesToInt(receivedPacket.data[47], receivedPacket.data[48])
//            val metadata = receivedPacket.data.slice((48 + 16)..48 + additionalMetadata)

            val imageOffset = 2 + 30 + receivedPacket.data[31].toUByte().toInt()
            var imageData = receivedPacket.data.slice(imageOffset..receivedPacket.length - imageOffset).toByteArray()

            // Only for debugging purpose.
//            val decodedData = imageData.joinToString("") { b -> String.format("%02X", b) }
//            Log.d("$TAG/getImage", decodedData)

            currentFrame = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)

        } catch (error: Exception) {
            error.message?.let { Log.e("$TAG/getImage", it) }
            onLoading()
        }

        return currentFrame
    }

//    private fun bytesToInt(byte1: Byte, byte2: Byte) =
//        ((byte1.toInt() and 255) shl 8) or (byte2.toInt() and 255)

}