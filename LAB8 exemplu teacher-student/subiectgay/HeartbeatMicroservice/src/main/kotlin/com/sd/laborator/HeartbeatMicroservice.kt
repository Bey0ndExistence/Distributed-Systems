package com.sd.laborator

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import java.time.Period
import kotlin.concurrent.thread

class HeartbeatMicroservice {
    private lateinit var heartbeatSocket: ServerSocket

    companion object Constants {
        const val HEARTBEAT_PORT = 1800
        const val CHECK_INTERVAL_SECONDS: Long = 5

        val PROJECT_NAME = System.getenv("PROJECT_NAME") ?: "subiectgay"
    }

    public fun run() {
        // se porneste un socket server TCP pe portul 1500 care asculta pentru conexiuni
        heartbeatSocket = ServerSocket(HEARTBEAT_PORT)
        println("HeartbeatMicroservice se executa pe portul: ${heartbeatSocket.localPort}")
        println("Se asteapta conexiuni si mesaje...")

        while (true) {
            // se asteapta conexiuni din partea clientilor subscriberi
            val clientConnection = heartbeatSocket.accept()

            // se porneste un thread separat pentru tratarea conexiunii cu clientul
            thread {
                println("Subscriber conectat: ${clientConnection.inetAddress.hostAddress}:${clientConnection.port}")

                val bufferReader = BufferedReader(InputStreamReader(clientConnection.inputStream))

                val outputStream = clientConnection.getOutputStream()

                var receivedContainerName: String? = null

                while (true) {
                    val receivedMessage = bufferReader.readLine()
                    // daca se primeste un mesaj gol (NULL), atunci inseamna ca cealalta parte a socket-ului a fost inchisa
                    if (receivedMessage == null) {
                        // TODO: aici trebuie sa repornim serviciul
                        // deci subscriber-ul respectiv a fost deconectat
                        println("Serviciul ${clientConnection.port} a murit.")

                        bufferReader.close()
                        clientConnection.close()

                        if (receivedContainerName != null) {
                            try {
                                ProcessBuilder(
                                    "docker-compose",
                                    "-p",
                                    PROJECT_NAME,
                                    "restart",
                                    receivedContainerName
                                )
                                    .directory(File("/app/files/"))
                                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                                    .start()
                            } catch (e: Exception) {
                                println("$e")
                                println("NU VREA DOCKER SA DEA RESTART :(((")
                            }
                        }

                        break
                    }

                    receivedContainerName = receivedMessage

                    println("Primit mesaj: $receivedMessage deci serviciul este in viata")

                    Thread.sleep(CHECK_INTERVAL_SECONDS * 1000)

                    outputStream.write("esti in viata?\n".toByteArray())
                }
            }
        }
    }
}

fun main() {
    val heartbeatMicroservice = HeartbeatMicroservice()

    heartbeatMicroservice.run()
}
