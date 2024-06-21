package com.sd.laborator

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

class MessageManagerMicroservice {
    private val subscribers: ConcurrentHashMap<Int, Socket> = ConcurrentHashMap()
    private lateinit var messageManagerSocket: ServerSocket

    companion object Constants {
        const val MESSAGE_MANAGER_PORT = 1500
    }

    private fun broadcastMessage(message: String, except: Int) {
        subscribers.forEach { (_, socket) ->
            if (socket.port != except) {
                GlobalScope.launch(Dispatchers.IO) {
                    socket.getOutputStream().write((message + "\n").toByteArray())
                }
            }
        }
    }

    private fun respondTo(destination: Int, message: String) {
        subscribers[destination]?.let { socket ->
            GlobalScope.launch(Dispatchers.IO) {
                socket.getOutputStream().write((message + "\n").toByteArray())
            }
        }
    }

    fun run() {
        messageManagerSocket = ServerSocket(MESSAGE_MANAGER_PORT)
        println("com.sd.laborator.MessageManagerMicroservice is running on port: ${messageManagerSocket.localPort}")
        println("Waiting for connections and messages...")

        while (true) {
            val clientConnection = messageManagerSocket.accept()

            GlobalScope.launch(Dispatchers.IO) {
                println("Subscriber connected: ${clientConnection.inetAddress.hostAddress}:${clientConnection.port}")

                subscribers[clientConnection.port] = clientConnection

                val bufferReader = BufferedReader(InputStreamReader(clientConnection.inputStream))

                try {
                    while (true) {
                        val receivedMessage = bufferReader.readLine() ?: break

                        println("Received message: $receivedMessage")

                        val (messageType, messageDestination, messageBody) = receivedMessage.split(" ", limit = 3)

                        when (messageType) {
                            "intrebare" -> {
                                broadcastMessage("intrebare ${clientConnection.port} $messageBody", except = clientConnection.port)
                            }
                            "raspuns" -> {
                                respondTo(messageDestination.toInt(), messageBody)
                            }
                        }
                    }
                } catch (e: Exception) {
                    println("Subscriber ${clientConnection.port} disconnected.")
                    subscribers.remove(clientConnection.port)
                } finally {
                    bufferReader.close()
                    clientConnection.close()
                }
            }
        }
    }
}

fun main() {
    val messageManagerMicroservice = MessageManagerMicroservice()
    messageManagerMicroservice.run()
}
