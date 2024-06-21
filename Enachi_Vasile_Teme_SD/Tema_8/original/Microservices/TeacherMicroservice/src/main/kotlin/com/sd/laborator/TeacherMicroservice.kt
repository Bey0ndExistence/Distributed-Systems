package com.sd.laborator

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.*
import kotlin.system.exitProcess
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class TeacherMicroservice {
    private var questionDatabase: MutableList<Pair<String, String>>
    private lateinit var messageManagerSocket: Socket
    private lateinit var teacherMicroserviceServerSocket: ServerSocket

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
        // pentru testare, se foloseste localhost. pentru deploy, server-ul socket (microserviciul MessageManager) se identifica dupa un "hostname"
        // acest hostname poate fi trimis (optional) ca variabila de mediu
        val MESSAGE_MANAGER_HOST = System.getenv("MESSAGE_MANAGER_HOST") ?: "localhost"
        const val MESSAGE_MANAGER_PORT = 1500
        const val TEACHER_PORT = 1600
    }

    private fun subscribeToMessageManager() {
        try {
            messageManagerSocket = Socket(MESSAGE_MANAGER_HOST, MESSAGE_MANAGER_PORT)
            //messageManagerSocket.soTimeout = 3000
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
            //waitForStudentQuestions()
            waitForTeacherQuestions()
        }
        launch(newSingleThreadContext("Asking_questions_thread")) {
            println("Asking_questions_thread_Context: I'm working in thread ${Thread.currentThread().name}")
            uiRequests()
        }
    }


    private fun waitForTeacherQuestions() = runBlocking {
        println("MessageMicroservice se executa pe portul: ${messageManagerSocket.localPort}")
        println("Se asteapta mesaje...")

        val bufferReader = BufferedReader(InputStreamReader(messageManagerSocket.inputStream))

        while (true) {
            // se asteapta intrebari trimise prin intermediarul "MessageManager"

            val question = bufferReader.readLine()

            if (question == null) {
                // daca se primeste un mesaj gol (NULL), atunci inseamna ca cealalta parte a socket-ului a fost inchisa
                println("Microserviciul MessageService (${messageManagerSocket.port}) a fost oprit.")
                bufferReader.close()
                messageManagerSocket.close()
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
                println("Trimit catre MessageManager: ${"intrebare ${messageManagerSocket.localPort} $receivedQuestion\n"}")
                messageManagerSocket.getOutputStream().write(("intrebare ${messageManagerSocket.localPort} $receivedQuestion\n").toByteArray())
                // se asteapta raspuns de la MessageManager
                try {
                    val receivedResponse = responsesQueue.poll(3, TimeUnit.SECONDS) ?: throw SocketTimeoutException()

                    // se trimite raspunsul inapoi clientului apelant
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