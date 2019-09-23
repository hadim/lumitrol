package org.hadim.lumitrol.network

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.UnknownHostException
import androidx.core.content.ContextCompat.getSystemService




class SSDPDiscovery : AsyncTask<Any, Void, Void?>() {

    private val LOGTAG = "SSDPDiscovery"
    var addresses: HashSet<String> = HashSet()

    override fun doInBackground(vararg params: Any?): Void? {

        val serviceName = params[0] as String
        val context = params[1] as Context

        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager


        if (wifi != null) {

            val lock = wifi.createMulticastLock("The Lock")
            lock.acquire()

            var socket: DatagramSocket? = null

            try {

                val group = InetAddress.getByName("239.255.255.250")
                val port = 1900
                val mx = 1

                val query = "M-SEARCH * HTTP/1.1\r\n" +
                        "HOST: $group:$port\r\n" +
                        "MAN: \"ssdp:discover\"\r\n" +
                        "MX: $mx\r\n" +
                        "ST: $serviceName\r\n" +
                        "\r\n"

                socket = DatagramSocket(port)
                socket!!.setReuseAddress(true)

                val dgram = DatagramPacket(
                    query.toByteArray(), query.length,
                    group, port
                )
                socket!!.send(dgram)

                val time = System.currentTimeMillis()
                var curTime = System.currentTimeMillis()

                // Let's consider all the responses we can get in 1 second
                while (curTime - time < 1000) {
                    val p = DatagramPacket(ByteArray(12), 12)
                    socket!!.receive(p)

                    val s = String(p.getData(), 0, p.getLength())
                    if (s.toUpperCase() == "HTTP/1.1 200") {
                        addresses.add(p.getAddress().getHostAddress())
                    }

                    curTime = System.currentTimeMillis()
                }

            } catch (e: UnknownHostException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                socket!!.close()
            }
            lock.release()
        }
        return null
    }

}

