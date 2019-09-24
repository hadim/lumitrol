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
import org.hadim.lumitrol.MainActivity
import android.R
import android.widget.SimpleAdapter
import org.json.JSONArray
import org.json.JSONObject


class SSDPDiscovery : AsyncTask<Any, Void, HashSet<String>>() {

    var addresses: HashSet<String> = HashSet()

    override fun doInBackground(vararg params: Any?): HashSet<String>? {

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
                socket.reuseAddress = true

                val dgram = DatagramPacket(
                    query.toByteArray(), query.length,
                    group, port
                )
                socket.send(dgram)

                val time = System.currentTimeMillis()
                var curTime = System.currentTimeMillis()

                // Let's consider all the responses we can get in 1 second
                while (curTime - time < 1000) {
                    Log.d("SSDP", "==========")
                    val p = DatagramPacket(ByteArray(12), 12)
                    socket.receive(p)

                    val s = String(p.data, 0, p.length)
                    if (s.toUpperCase() == "HTTP/1.1 200") {
                        addresses.add(p.address.hostAddress)
                    }

                    Log.d("SSDP", p.address.toString())

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
        return addresses
    }

    override fun onPostExecute(result: HashSet<String>) {
        Log.d("SSPD", addresses.toString())
    }

}

