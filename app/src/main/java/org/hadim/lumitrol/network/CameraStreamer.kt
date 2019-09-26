package org.hadim.lumitrol.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.*
import java.util.concurrent.Executors


/*
* https://github.com/peci1/lumix-link-desktop/blob/f83f2dabb076872aa62007de64cf3aaa5acf7f77/StreamViewer/src/streamviewer/StreamViewer.java
 */
class CameraStreamer(imageConsumer: (Bitmap) -> Unit, ipAddress: String) : Runnable {

    var localUdpPort: Int = 0
    private var ipAddress: String
    private lateinit var localUdpSocket: DatagramSocket

    private var imageConsumer: (Bitmap) -> Unit
    private val imageExecutor = Executors.newCachedThreadPool()

    init {
        this.imageConsumer = imageConsumer
        this.ipAddress = ipAddress

        localUdpPort = 49199  // findFreeUDPPort()
        Log.d("CameraStreamer/init", "UDP port found: $localUdpPort")
    }

    private fun retrieveImage(receivedPacket: DatagramPacket): Bitmap? {

        val imageData = getImageData(receivedPacket)

        var image: Bitmap? = null
        var imageOffset = 0

        try {
            image = BitmapFactory.decodeByteArray(imageData, imageOffset, imageData.size - imageOffset)
            Log.d("CameraStreamer/retrieveImage", "Frame received.")
        } catch (e: IOException) {
            Log.e("CameraStreamer/retrieveImage", "Error while reading frame.")
            Log.e("CameraStreamer/retrieveImage", e.message)
        }

        return image
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

    fun clean() {
        localUdpSocket.close()
    }

    override fun run() {
        // The camera sends each image in one UDP packet, normally between 25000 and 30000 bytes. We set 35000 here to be safe.

        Thread.sleep(100)

        localUdpSocket = DatagramSocket(localUdpPort)
        localUdpSocket.setSoTimeout(100000)

        val udpPacketBuffer = ByteArray(35000)
        val ipAddressInet = InetAddress.getByName(ipAddress)

        while (!Thread.interrupted()) {
            try {
                Log.d("CameraStreamer/run", "before")
                val receivedPacket = DatagramPacket(udpPacketBuffer, udpPacketBuffer.size, ipAddressInet, localUdpPort)
                localUdpSocket.receive(receivedPacket)
                Log.d("CameraStreamer/run", "after")

//                imageExecutor.submit {
//                    val newFrame = retrieveImage(receivedPacket)
//                    Log.d("CameraStreamer/run", receivedPacket.length.toString())
//                    if (newFrame != null) {
//                        imageConsumer(newFrame)
//                    }
//                }

            } catch (e: Exception) {
                Log.e("CameraStreamer/run", "Error while requesting frame.")
                Log.e("CameraStreamer/run", e.message)
            }

        }

        imageExecutor.shutdown()
        localUdpSocket.close()
    }

}