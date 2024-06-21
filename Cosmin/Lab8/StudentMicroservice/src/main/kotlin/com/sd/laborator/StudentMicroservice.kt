package com.sd.laborator

import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.Exception
import java.net.Socket
import kotlin.system.exitProcess

class StudentMicroservice {
    private lateinit var questionDatabase: List<Pair<String, String>>
    private lateinit var messageManagerSocket: Socket

    init {
        val databaseLines: List<String> = File("questions_database.txt").readLines()
        questionDatabase = databaseLines.chunked(2) { (question, answer) ->
            Pair(question, answer)
        }
    }

    companion object Constants {
        val MESSAGE_MANAGER_HOST = System.getenv("MESSAGE_MANAGER_HOST") ?: "localhost"
        const val MESSAGE_MANAGER_PORT = 1500
    }

    private fun subscribeToMessageManager() {
        try {
            messageManagerSocket = Socket(MESSAGE_MANAGER_HOST, MESSAGE_MANAGER_PORT)
            println("Connected to MessageManager!")
        } catch (e: Exception) {
            println("Failed to connect to MessageManager!")
            exitProcess(1)
        }
    }

    private fun respondToQuestion(question: String): String? {
        return questionDatabase.firstOrNull { it.first == question }?.second
    }

    fun run() {
        runBlocking {
            subscribeToMessageManager()

            println("com.sd.laborator.StudentMicroservice is running on port: ${messageManagerSocket.localPort}")
            println("Waiting for messages...")

            val bufferReader = BufferedReader(InputStreamReader(messageManagerSocket.inputStream))


            launch(Dispatchers.IO) {
                while (true) {
                    val response = bufferReader.readLine() ?: break


                    launch {
                        handleResponse(response)
                    }
                }


                bufferReader.close()
                messageManagerSocket.close()
            }
        }
    }

    private suspend fun handleResponse(response: String) {
        val (messageType, messageDestination, messageBody) = response.split(" ", limit = 3)

        when (messageType) {
            "intrebare" -> {
                println("Received a question from $messageDestination: \"$messageBody\"")
                val answer = respondToQuestion(messageBody)
                if (answer != null) {
                    val responseToQuestion = "raspuns $messageDestination $answer"
                    println("Sending the answer: \"$responseToQuestion\"")
                    messageManagerSocket.getOutputStream().write((responseToQuestion + "\n").toByteArray())
                }
            }
        }
    }
}

fun main() {
    val studentMicroservice = StudentMicroservice()
    studentMicroservice.run()
}
