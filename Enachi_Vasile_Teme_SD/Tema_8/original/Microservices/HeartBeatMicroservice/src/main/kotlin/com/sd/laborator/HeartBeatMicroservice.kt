package com.sd.laborator

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.*
import kotlin.system.exitProcess
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import java.io.File
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class HeartBeatMicroservice {
    private var questionDatabase: MutableList<Pair<String, String>>
    private lateinit var messageManagerSocket: Socket
    private lateinit var heartBeatMicroserviceServerSocket: ServerSocket

    private val mutex = Mutex()
    private val responsesQueue : BlockingQueue<String> = LinkedBlockingQueue()

    init {
        val databaseLines: List<String> = File("questions_database.txt").readLines()
        questionDatabase = mutableListOf()

        for (i in databaseLines.indices step 2) {
            questionDatabase.add(Pair(databaseLines[i], databaseLines[i + 1]))
        }
    }

    private fun respondToQuestion(question: String): String? {
        questionDatabase.forEach {
            if (it.first == question) {
                return it.second
            }
        }
        return null
    }

    companion object Constants {
        val MESSAGE_MANAGER_HOST = System.getenv("MESSAGE_MANAGER_HOST") ?: "localhost"
        const val MESSAGE_MANAGER_PORT = 1500
        const val HEARTBEAT_PORT = 2000
    }

    private fun subscribeToMessageManager() {
        try {
            messageManagerSocket = Socket(MESSAGE_MANAGER_HOST, MESSAGE_MANAGER_PORT)
            println("M-am conectat la MessageManager!")
        } catch (e: Exception) {
            println("Nu ma pot conecta la MessageManager!")
            exitProcess(1)
        }
    }

    fun run() = runBlocking {
        subscribeToMessageManager()

        launch(newSingleThreadContext("Teacher_questions_thread")) {
            println("TeacherThreadContext: I'm working in thread ${Thread.currentThread().name}")
            //waitForStudentQuestions()
            waitForQuestions()
        }
        launch(newSingleThreadContext("Asking_questions_thread")) {
            println("Asking_questions_thread_Context: I'm working in thread ${Thread.currentThread().name}")
            uiRequests()
        }
    }


    private fun waitForQuestions() = runBlocking {
        println("MessageMicroservice se executa pe portul: ${messageManagerSocket.localPort}")
        println("Se asteapta mesaje...")

        val bufferReader = BufferedReader(InputStreamReader(messageManagerSocket.inputStream))

        while (true) {
            val question = bufferReader.readLine()

            if (question == null) {
                // daca se primeste un mesaj gol (NULL), atunci inseamna ca cealalta parte a socket-ului a fost inchisa
                println("Microserviciul MessageService (${messageManagerSocket.port}) a fost oprit.")
                bufferReader.close()
                messageManagerSocket.close()
                break
            }

            launch(Dispatchers.Default) {
                val (messageType, messageDestination, messageBody) = question.split(" ", limit = 3)

                when (messageType) {
                    "intrebare" -> {
                        println("Am primit o intrebare de la $messageDestination: \"${messageBody}\"")

                        var responseToQuestion = respondToQuestion(messageBody)
                        responseToQuestion?.let {
                            responseToQuestion = "raspuns $messageDestination $it"
                            println("Trimit raspunsul: \"${responseToQuestion}\" la intrebarea \"${question}\"")
                            messageManagerSocket.getOutputStream().write((responseToQuestion + "\n").toByteArray())
                        }
                    }
                    "raspuns" -> {
                        println("Am primit un raspuns de la $messageDestination: \"${messageBody}\"")
                        responsesQueue.put(messageBody)
                    }
                }
            }
        }
    }

    private fun uiRequests() = runBlocking {
        heartBeatMicroserviceServerSocket = ServerSocket(HEARTBEAT_PORT)

        println("DummyMicroservice se executa pe portul: ${heartBeatMicroserviceServerSocket.localPort}")
        println("Se asteapta cereri (intrebari)...")

        while (true) {
            val clientConnection = heartBeatMicroserviceServerSocket.accept()
            launch(Dispatchers.Default) {
                println("S-a primit o cerere de la: ${clientConnection.inetAddress.hostAddress}:${clientConnection.port}")

                val clientBufferReader = BufferedReader(InputStreamReader(clientConnection.inputStream))
                val receivedQuestion = clientBufferReader.readLine()

                println("Trimit catre MessageManager: ${"intrebare ${messageManagerSocket.localPort} $receivedQuestion\n"}")
                messageManagerSocket.getOutputStream().write(("intrebare ${messageManagerSocket.localPort} $receivedQuestion\n").toByteArray())
                try {
                    val receivedResponse = responsesQueue.poll(3, TimeUnit.SECONDS) ?: throw SocketTimeoutException()
                    println("Am primit raspunsul: \"$receivedResponse\"")
                    clientConnection.getOutputStream().write((receivedResponse + "\n").toByteArray())
                    while (responsesQueue.isEmpty().not()){
                        val receivedResponse = responsesQueue.take()
                        println("Am primit raspunsul: \"$receivedResponse\"")
                        clientConnection.getOutputStream().write((receivedResponse + "\n").toByteArray())
                    }
                } catch (e: SocketTimeoutException) {
                    println("Nu a venit niciun raspuns in timp util.")
                    clientConnection.getOutputStream().write("Nu a raspuns nimeni la intrebare\n".toByteArray())
                } finally {
                    clientConnection.close()

                }
            }
        }
    }
}

fun main(args: Array<String>) {
    val teacherMicroservice = HeartBeatMicroservice()
    teacherMicroservice.run()
}