package com.sd.laborator

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.Exception
import java.net.Socket
import kotlin.system.exitProcess
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.net.ServerSocket
import java.net.SocketTimeoutException

class StudentMicroservice {
    // intrebarile si raspunsurile sunt mentinute intr-o lista de perechi de forma:
    // [<INTREBARE 1, RASPUNS 1>, <INTREBARE 2, RASPUNS 2>, ... ]
    private var questionDatabase: MutableList<Pair<String, String>>
    private lateinit var messageManagerSocketReceivingQuestions: Socket
    private lateinit var messageManagerSocketSendingQuestions: Socket
    private lateinit var studentMicroserviceServerSocket: ServerSocket
    private var mutex = Mutex()

    init {
        val databaseLines: List<String> = File("questions_database.txt").readLines()
        questionDatabase = mutableListOf()

        /*
         "baza de date" cu intrebari si raspunsuri este de forma:

         <INTREBARE_1>\n
         <RASPUNS_INTREBARE_1>\n
         <INTREBARE_2>\n
         <RASPUNS_INTREBARE_2>\n
         ...
         */
        for (i in databaseLines.indices step 2) {
            questionDatabase.add(Pair(databaseLines[i], databaseLines[i + 1]))
        }
    }

    companion object Constants {
        // pentru testare, se foloseste localhost. pentru deploy, server-ul socket (microserviciul MessageManager) se identifica dupa un "hostname"
        // acest hostname poate fi trimis (optional) ca variabila de mediu
        val MESSAGE_MANAGER_HOST = System.getenv("MESSAGE_MANAGER_HOST") ?: "localhost"
        const val MESSAGE_MANAGER_PORT_RECEIVING_Q = 1500
        const val MESSAGE_MANAGER_PORT_SENDING_Q = 1550
        const val STUDENT_PORT = 1700
    }

    private fun subscribeToMessageManager() {
        try {
            messageManagerSocketReceivingQuestions = Socket(MESSAGE_MANAGER_HOST, MESSAGE_MANAGER_PORT_RECEIVING_Q)
            messageManagerSocketSendingQuestions = Socket(MESSAGE_MANAGER_HOST, MESSAGE_MANAGER_PORT_SENDING_Q)

            messageManagerSocketSendingQuestions.soTimeout = 3000
            println("M-am conectat la MessageManager!")
        } catch (e: Exception) {
            println("Nu ma pot conecta la MessageManager!")
            exitProcess(1)
        }
    }

    private suspend fun respondToQuestion(question: String): String? {
        mutex.withLock {
            questionDatabase.forEach {
                // daca se gaseste raspunsul la intrebare, acesta este returnat apelantului
                if (it.first == question) {
                    return it.second
                }
            }
            return null
        }
    }


    fun run() = runBlocking {
        // microserviciul se inscrie in lista de "subscribers" de la MessageManager prin conectarea la acesta
        subscribeToMessageManager()
        launch(newSingleThreadContext("Teacher_questions_thread")) {
            println("TeacherThreadContext: I'm working in thread ${Thread.currentThread().name}")
            waitForTeacherQuestions()
        }
        launch(newSingleThreadContext("Asking_questions_thread")) {
            println("Asking_questions_thread_Context: I'm working in thread ${Thread.currentThread().name}")
            uiRequests()
        }
    }

    private fun waitForTeacherQuestions() = runBlocking {
        println("StudentMicroservice se executa pe portul: ${messageManagerSocketReceivingQuestions.localPort}")
        println("Se asteapta mesaje...")

        val bufferReader = BufferedReader(InputStreamReader(messageManagerSocketReceivingQuestions.inputStream))

        while (true) {
            // se asteapta intrebari trimise prin intermediarul "MessageManager"
            val question = bufferReader.readLine()

            if (question == null) {
                // daca se primeste un mesaj gol (NULL), atunci inseamna ca cealalta parte a socket-ului a fost inchisa
                println("Microserviciul MessageService (${messageManagerSocketReceivingQuestions.port}) a fost oprit.")
                bufferReader.close()
                messageManagerSocketReceivingQuestions.close()
                break
            }

            // se foloseste un thread separat pentru tratarea intrebarii primite
            launch(Dispatchers.Default) {
                val (messageType, messageDestination, messageBody) = question.split(" ", limit = 3)

                when (messageType) {
                    // tipul mesajului cunoscut de acest microserviciu este de forma:
                    // intrebare <DESTINATIE_RASPUNS> <CONTINUT_INTREBARE>
                    "intrebare" -> {
                        println("Am primit o intrebare de la $messageDestination: \"${messageBody}\"")

                        var responseToQuestion = respondToQuestion(messageBody)
                        responseToQuestion?.let {
                            responseToQuestion = "raspuns $messageDestination $it"
                            println("Trimit raspunsul: \"${responseToQuestion}\" la intrebarea \"${question}\"")
                            messageManagerSocketReceivingQuestions.getOutputStream().write((responseToQuestion + "\n").toByteArray())
                        }
                    }
                }
            }
        }
    }

    private fun uiRequests() = runBlocking {
        studentMicroserviceServerSocket = ServerSocket(STUDENT_PORT)

        println("TeacherMicroservice se executa pe portul: ${studentMicroserviceServerSocket.localPort}")
        println("Se asteapta cereri (intrebari)...")

        while (true) {
            val clientConnection = studentMicroserviceServerSocket.accept()

            launch(Dispatchers.Default) {
                println("S-a primit o cerere de la: ${clientConnection.inetAddress.hostAddress}:${clientConnection.port}")

                val clientBufferReader = BufferedReader(InputStreamReader(clientConnection.inputStream))
                val receivedQuestion = clientBufferReader.readLine()

                println("Trimit catre MessageManager: ${"intrebare ${messageManagerSocketSendingQuestions.localPort} $receivedQuestion\n"}")
                messageManagerSocketSendingQuestions.getOutputStream().write(("intrebare ${messageManagerSocketSendingQuestions.localPort} $receivedQuestion\n").toByteArray())

                val messageManagerBufferReader = BufferedReader(InputStreamReader(messageManagerSocketSendingQuestions.inputStream))
                try {
                    val receivedResponse = messageManagerBufferReader.readLine()

                    println("Am primit raspunsul: \"$receivedResponse\"")
                    clientConnection.getOutputStream().write((receivedResponse + "\n").toByteArray())
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
    val studentMicroservice = StudentMicroservice()
    studentMicroservice.run()
}