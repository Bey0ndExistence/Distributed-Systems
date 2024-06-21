package com.sd.laborator

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.*
import kotlin.system.exitProcess
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File

class TeacherMicroservice {
    private var questionDatabase: MutableList<Pair<String, String>>
    private lateinit var messageManagerSocketSendingQuestions: Socket
    private lateinit var messageManagerSocketReceivingQuestions: Socket
    private lateinit var teacherMicroserviceServerSocket: ServerSocket

    private val mutex = Mutex()

    init {
        val databaseLines: List<String> = File("questions_database.txt").readLines()
        questionDatabase = mutableListOf()

        for (i in databaseLines.indices step 2) {
            questionDatabase.add(Pair(databaseLines[i], databaseLines[i + 1]))
        }
    }

    private suspend fun respondToQuestion(question: String): String? {
        mutex.withLock {
            questionDatabase.forEach {
                if (it.first == question) {
                    return it.second
                }
            }
            return null
        }

    }

    companion object Constants {
        // pentru testare, se foloseste localhost. pentru deploy, server-ul socket (microserviciul MessageManager) se identifica dupa un "hostname"
        // acest hostname poate fi trimis (optional) ca variabila de mediu
        val MESSAGE_MANAGER_HOST = System.getenv("MESSAGE_MANAGER_HOST") ?: "localhost"
        const val MESSAGE_MANAGER_PORT_Sending_Questions = 1500
        const val MESSAGE_MANAGER_PORT_Receiving_Questions = 1550

        const val TEACHER_PORT = 1600
    }

    private fun subscribeToMessageManager() {
        try {
            messageManagerSocketSendingQuestions = Socket(MESSAGE_MANAGER_HOST, MESSAGE_MANAGER_PORT_Sending_Questions)
            messageManagerSocketReceivingQuestions = Socket(MESSAGE_MANAGER_HOST, MESSAGE_MANAGER_PORT_Receiving_Questions)
            messageManagerSocketSendingQuestions.soTimeout = 3000
            println("M-am conectat la MessageManager!")
        } catch (e: Exception) {
            println("Nu ma pot conecta la MessageManager!")
            exitProcess(1)
        }
    }

    fun run() = runBlocking {
        // microserviciul se inscrie in lista de "subscribers" de la MessageManager prin conectarea la acesta
        subscribeToMessageManager()

        launch(newSingleThreadContext("Teacher_questions_thread")) {
            println("TeacherThreadContext: I'm working in thread ${Thread.currentThread().name}")
            waitForStudentQuestions()
        }
        launch(newSingleThreadContext("Asking_questions_thread")) {
            println("Asking_questions_thread_Context: I'm working in thread ${Thread.currentThread().name}")
            uiRequests()
        }
    }


    private fun waitForStudentQuestions() = runBlocking {
        println("StudentMicroservice se executa pe portul: ${messageManagerSocketReceivingQuestions.localPort}")
        println("Se asteapta mesaje...")

        val bufferReader = BufferedReader(InputStreamReader(messageManagerSocketReceivingQuestions.inputStream))

        while (true) {
            // se asteapta intrebari trimise prin intermediarul "MessageManager"
            val response = bufferReader.readLine()

            if (response == null) {
                // daca se primeste un mesaj gol (NULL), atunci inseamna ca cealalta parte a socket-ului a fost inchisa
                println("Microserviciul MessageService (${messageManagerSocketReceivingQuestions.port}) a fost oprit.")
                bufferReader.close()
                messageManagerSocketReceivingQuestions.close()
                break
            }

            // se foloseste un thread separat pentru tratarea intrebarii primite
            launch(Dispatchers.Default) {
                val (messageType, messageDestination, messageBody) = response.split(" ", limit = 3)

                when (messageType) {
                    // tipul mesajului cunoscut de acest microserviciu este de forma:
                    // intrebare <DESTINATIE_RASPUNS> <CONTINUT_INTREBARE>
                    "intrebare" -> {
                        println("Am primit o intrebare de la $messageDestination: \"${messageBody}\"")

                        var responseToQuestion = respondToQuestion(messageBody)
                        responseToQuestion?.let {
                            responseToQuestion = "raspuns $messageDestination $it"
                            println("Trimit raspunsul: \"${response}\"")
                            messageManagerSocketReceivingQuestions.getOutputStream().write((responseToQuestion + "\n").toByteArray())
                        }
                    }
                }
            }
        }
    }

    private fun uiRequests() = runBlocking {
        // se porneste un socket server TCP pe portul 1600 care asculta pentru conexiuni
        teacherMicroserviceServerSocket = ServerSocket(TEACHER_PORT)

        println("TeacherMicroservice se executa pe portul: ${teacherMicroserviceServerSocket.localPort}")
        println("Se asteapta cereri (intrebari)...")

        while (true) {
            // se asteapta conexiuni din partea clientilor ce doresc sa puna o intrebare
            // (in acest caz, din partea aplicatiei client GUI)
            val clientConnection = teacherMicroserviceServerSocket.accept()

            // se foloseste un thread separat pentru tratarea fiecarei conexiuni client
            launch(Dispatchers.Default) {
                println("S-a primit o cerere de la: ${clientConnection.inetAddress.hostAddress}:${clientConnection.port}")

                // se citeste intrebarea dorita
                val clientBufferReader = BufferedReader(InputStreamReader(clientConnection.inputStream))
                val receivedQuestion = clientBufferReader.readLine()

                // intrebarea este redirectionata catre microserviciul MessageManager
                println("Trimit catre MessageManager: ${"intrebare ${messageManagerSocketSendingQuestions.localPort} $receivedQuestion\n"}")
                messageManagerSocketSendingQuestions.getOutputStream().write(("intrebare ${messageManagerSocketSendingQuestions.localPort} $receivedQuestion\n").toByteArray())
                // se asteapta raspuns de la MessageManager
                val messageManagerBufferReader = BufferedReader(InputStreamReader(messageManagerSocketSendingQuestions.inputStream))
                try {
                    val receivedResponse = messageManagerBufferReader.readLine()



                    // se trimite raspunsul inapoi clientului apelant
                    println("Am primit raspunsul: \"$receivedResponse\"")
                    clientConnection.getOutputStream().write((receivedResponse + "\n").toByteArray())
                } catch (e: SocketTimeoutException) {
                    println("Nu a venit niciun raspuns in timp util.")
                    clientConnection.getOutputStream().write("Nu a raspuns nimeni la intrebare\n".toByteArray())
                } finally {
                    // se inchide conexiunea cu clientul
                    clientConnection.close()
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    val teacherMicroservice = TeacherMicroservice()
    teacherMicroservice.run()
}