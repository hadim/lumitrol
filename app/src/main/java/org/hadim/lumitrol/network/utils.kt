package org.hadim.lumitrol.network

import java.net.DatagramSocket

fun findFreeUDPPort(): Int {
    val socket = DatagramSocket(0)
    socket.reuseAddress = true
    return socket.localPort
}