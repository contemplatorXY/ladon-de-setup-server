package de.mc.ladon

import org.slf4j.LoggerFactory
import spark.Service.ignite
import java.io.FileReader
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket
import java.util.*

val log = LoggerFactory.getLogger("de.mc.ladon.main")

fun main(args: Array<String>) {

    val file = "ladon.properties"

    val props = Properties().apply {
        load(FileReader(file))
    }
    log.info(props.toString())

    val port = args.firstOrNull() ?: "8888"
    MulticastReceiver(port).start()

    val http = ignite()
    http.port(port.toInt())
    http.get("/") { req, res ->
        val reqAddr = req.raw().remoteAddr


        val isValid = props.getProperty(reqAddr) != null
        if (!isValid) res.status(409)
        log.info("Received request from $reqAddr : " + if (isValid) "valid" else "invalid")
        val p1 = props.clone() as Properties
        p1["self"] = reqAddr
        p1.storeToXML(res.raw().outputStream, "Ladon Config Server")
    }
}


class MulticastReceiver(val port: String) : Thread() {

    override fun run() {
        val socket = MulticastSocket(4446)
        val group = InetAddress.getByName("230.0.0.0")
        val buf = ByteArray(256)
        socket.joinGroup(group)
        while (true) {
            try {
                Thread.sleep(0)
                val packet = DatagramPacket(buf, buf.size)
                socket.receive(packet)
                val received = String(packet.data, 0, packet.length)
                log.info("Multicast: ${packet.address} $received")
                packet.data = port.toByteArray()
                socket.send(packet)
            } catch (e: InterruptedException) {
                socket.leaveGroup(group)
                socket.close()
            }
        }

    }
}