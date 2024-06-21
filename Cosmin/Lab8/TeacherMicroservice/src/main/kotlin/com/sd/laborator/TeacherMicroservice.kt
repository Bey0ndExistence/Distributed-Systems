package com.sd.laborator

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketTimeoutException
import kotlin.system.exitProcess

class TeacherMicroservice {
    private lateinit var messageManagerSocket: Socket
    private lateinit var teacherMicroserviceServerSocket: ServerSocket

    companion object Constants {
        val MESSAGE_MANAGER_HOST = System.getenv("MESSAGE_MANAGER_HOST") ?: "localhost"
        const val MESSAGE_MANAGER_PORT = 1500
        const val TEACHER_PORT = 1600
    }

    private fun subscribeToMessageManager() {
        try {
            messageManagerSocket = Socket(MESSAGE_MANAGER_HOST, MESSAGE_MANAGER_PORT)
            messageManagerSocket.soTimeout = 3000
            println("Connected to MessageManager!")
        } catch (e: Exception) {
            println("Failed to connect to MessageManager!")
            exitProcess(1)
        }
    }

    fun run() {
        runBlocking {
            subscribeToMessageManager()

            teacherMicroserviceServerSocket = ServerSocket(TEACHER_PORT)
            println("com.sd.laborator.TeacherMicroservice is running on port: ${teacherMicroserviceServerSocket.localPort}")
            println("Waiting for questions...")

            launch(Dispatchers.IO) {
                while (true) {
                    val clientConnection = teacherMicroserviceServerSocket.accept()
                    println("Received a request from: ${clientConnection.inetAddress.hostAddress}:${clientConnection.port}")

                    launch {
                        handleClientRequest(clientConnection)
                    }
                }
            }
        }
    }

    private suspend fun handleClientRequest(clientConnection: Socket) {
        val clientBufferReader = BufferedReader(InputStreamReader(clientConnection.inputStream))
        val receivedQuestion = withContext(Dispatchers.IO) {
            clientBufferReader.readLine()
        }

        if (receivedQuestion == "HearthBeatCheck") {
            val clientWriter = PrintWriter(withContext(Dispatchers.IO) {
                clientConnection.getOutputStream()
            }, true)
            clientWriter.println("TeacherMicroservice ok")
            withContext(Dispatchers.IO) {
                clientConnection.close()
            }
            return
        }

        val messageManagerWriter = PrintWriter(withContext(Dispatchers.IO) {
            messageManagerSocket.getOutputStream()
        }, true)
        messageManagerWriter.println("intrebare ${messageManagerSocket.localPort} $receivedQuestion")

        val messageManagerBufferReader = BufferedReader(InputStreamReader(messageManagerSocket.inputStream))
        try {
            val receivedResponse = withContext(Dispatchers.IO) {
                messageManagerBufferReader.readLine()
            }
            println("Received response: \"$receivedResponse\"")

            val clientWriter = PrintWriter(withContext(Dispatchers.IO) {
                clientConnection.getOutputStream()
            }, true)
            clientWriter.println(receivedResponse)
        } catch (e: SocketTimeoutException) {
            println("No response received in a timely manner.")
            val clientWriter = PrintWriter(withContext(Dispatchers.IO) {
                clientConnection.getOutputStream()
            }, true)
            clientWriter.println("No one responded to the question.")
        } finally {
            withContext(Dispatchers.IO) {
                clientConnection.close()
            }
        }
    }
}

fun main() {
    val teacherMicroservice = TeacherMicroservice()
    teacherMicroservice.run()
}
