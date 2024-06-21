package com.sd.laborator

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MessageManagerMicroservice {
    private val subscribers: HashMap<Int, Socket>
    private lateinit var messageManagerSocketTeacher: ServerSocket
    private lateinit var messageManagerSocketStudent: ServerSocket

    companion object Constants {
        const val MESSAGE_MANAGER_PORT_TEACHER_QUESTIONS = 1500
        const val MESSAGE_MANAGER_PORT_STUDENT_QUESTIONS = 1550
    }
    private val mutex = Mutex()
    init {
        subscribers = hashMapOf()
    }

    private suspend fun broadcastMessage(message: String, except: Int) {
        mutex.withLock {
            subscribers.forEach {
                it.takeIf { it.key != except }
                        ?.value?.getOutputStream()?.write((message + "\n").toByteArray())
            }
        }
    }

    private suspend fun respondTo(destination: Int, message: String) {
        mutex.withLock {
            subscribers[destination]?.getOutputStream()?.write((message + "\n").toByteArray())
        }
    }

    public fun run() = runBlocking{
        // se porneste un socket server TCP pe portul 1500 care asculta pentru conexiuni
        messageManagerSocketTeacher = ServerSocket(MESSAGE_MANAGER_PORT_TEACHER_QUESTIONS)

        messageManagerSocketStudent = ServerSocket(MESSAGE_MANAGER_PORT_STUDENT_QUESTIONS)
        println("MessageManagerMicroservice Teacher se executa pe portul: ${messageManagerSocketTeacher.localPort}")
        println("MessageManagerMicroservice Student se executa pe portul: ${messageManagerSocketStudent.localPort}")
        println("Se asteapta conexiuni si mesaje...")
        launch(newSingleThreadContext("Message Manager for Teacher")){
            questionsSocketThread(messageManagerSocketTeacher)
        }
        launch(newSingleThreadContext("Message Manager for Student")){
            questionsSocketThread(messageManagerSocketStudent)
        }
    }


    private fun questionsSocketThread(socket: ServerSocket ) = runBlocking {
        while (true) {
            // se asteapta conexiuni din partea clientilor subscriberi
            val clientConnection = socket.accept()

            // se porneste un thread separat pentru tratarea conexiunii cu clientul
            launch(Dispatchers.Default) {
                println("Subscriber conectat: ${clientConnection.inetAddress.hostAddress}:${clientConnection.port}")

                // adaugarea in lista de subscriberi trebuie sa fie atomica!
                synchronized(subscribers) {
                    subscribers[clientConnection.port] = clientConnection
                }

                val bufferReader = BufferedReader(InputStreamReader(clientConnection.inputStream))

                while (true) {
                    // se citeste raspunsul de pe socketul TCP
                    val receivedMessage = bufferReader.readLine()

                    // daca se primeste un mesaj gol (NULL), atunci inseamna ca cealalta parte a socket-ului a fost inchisa
                    if (receivedMessage == null) {
                        // deci subscriber-ul respectiv a fost deconectat
                        println("Subscriber-ul ${clientConnection.port} a fost deconectat.")
                        synchronized(subscribers) {
                            subscribers.remove(clientConnection.port)
                        }
                        bufferReader.close()
                        clientConnection.close()
                        break
                    }

                    println("[THREAD ${Thread.currentThread().name}] -> Primit mesaj: $receivedMessage")
                    val (messageType, messageDestination, messageBody) = receivedMessage.split(" ", limit = 3)

                    when (messageType) {
                        "intrebare" -> {
                            // tipul mesajului de tip intrebare este de forma:
                            // intrebare <DESTINATIE_RASPUNS> <CONTINUT_INTREBARE>
                            println("Trimit broadcast")
                            broadcastMessage("intrebare ${clientConnection.port} $messageBody", except = clientConnection.port)
                        }
                        "raspuns" -> {
                            // tipul mesajului de tip raspuns este de forma:
                            // raspuns <CONTINUT_RASPUNS>
                            respondTo(messageDestination.toInt(), messageBody)
                        }
                    }
                }
            }
        }
    }

}

fun main(args: Array<String>){
    val messageManagerMicroservice = MessageManagerMicroservice()
    messageManagerMicroservice.run()
}

