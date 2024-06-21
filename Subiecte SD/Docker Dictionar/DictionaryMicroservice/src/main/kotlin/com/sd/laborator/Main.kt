package com.sd.laborator

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.Exception
import java.net.Socket
import kotlin.system.exitProcess

class DictionaryMicroservice {
    private lateinit var messageManagerSocket: Socket
    private var dictionary: List<String> = File("dictionary.txt").readLines()

    private var question: String? = null
    private var response: String? = null
    private var conversationCount = 0

    companion object Constants {
        // pentru testare, se foloseste localhost. pentru deploy, server-ul socket (microserviciul MessageManager) se identifica dupa un "hostname"
        // acest hostname poate fi trimis (optional) ca variabila de mediu
        val MESSAGE_MANAGER_HOST = System.getenv("MESSAGE_MANAGER_HOST") ?: "localhost"
        const val MESSAGE_MANAGER_PORT = 1500
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

    private fun registerConversation(msg: String): String? {
        return if (msg.startsWith("intrebare")) {
            val (_, destination, body) = msg.split(" ", limit = 3)
            if (destination.toIntOrNull() != null) {
                question = body
            }

            if (response == null) {
                null
            } else {
                val ret = "$question $response"

                question = null
                response = null

                ret
            }
        } else {
            if (question == null) {
                response = msg

                null
            } else {
                val ret = "$question $msg"


                question = null
                response = null

                ret
            }
        }
    }

    public fun run() = runBlocking {
        subscribeToMessageManager()
        println("DictionaryMicroservice se executa pe portul: ${messageManagerSocket.localPort}")
        println("Se asteapta mesaje...")

        val bufferReader = BufferedReader(InputStreamReader(messageManagerSocket.inputStream))

        launch(Dispatchers.IO) {
            delay(100)
            messageManagerSocket.getOutputStream().write("dictionary 0 body\n".toByteArray())
            while (true) {
                val response = bufferReader.readLine()

                if (response == null) {
                    // daca se primeste un mesaj gol (NULL), atunci inseamna ca cealalta parte a socket-ului a fost inchisa
                    println("Microserviciul DictionaryMicroservice (${messageManagerSocket.port}) a fost oprit.")
                    bufferReader.close()
                    messageManagerSocket.close()
                    break
                }

                launch processMsg@{
                    val conversation = registerConversation(response) ?: return@processMsg

                    var wordCount = 0
                    var dictWordCount = 0

                    conversation.split(" \n").forEach { word->
                        wordCount++
                        if (word in dictionary)
                            dictWordCount++
                    }
                    if(wordCount / 2 <= dictWordCount) {
                        File("conversations.txt").appendText("$conversation\n")
                    }
                }
            }
        }
    }

}

fun main() {
    val dictionaryMicroservice = DictionaryMicroservice()
    dictionaryMicroservice.run()
}