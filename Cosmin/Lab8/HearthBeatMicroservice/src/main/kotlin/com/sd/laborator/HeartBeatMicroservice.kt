package com.sd.laborator

import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import kotlin.system.exitProcess


class HeartBeatMicroservice {
    private lateinit var teacherSocket: Socket

    companion object {
        const val HEARTBEAT_INTERVAL_MS = 50000L
        const val HEARTBEAT_MESSAGE = "HearthBeatCheck"
        const val TEACHER_PORT = 1600
    }

    private fun subscribeToTeacher() {
        try {
            teacherSocket = Socket("localhost", TEACHER_PORT)
            println("Connected to Teacher!")
        } catch (e: java.lang.Exception) {
            println("Failed to connect to Teacher!: ${e.message}")
            exitProcess(1)
        }
    }

    suspend fun run() {
        println("AM intrat in run")
        subscribeToTeacher()
        println("Heart Beat Is ON")
        while (true) {
            try {
                val writer = PrintWriter(teacherSocket.getOutputStream(), true)
                val reader = BufferedReader(InputStreamReader(teacherSocket.getInputStream()))

                writer.println(HEARTBEAT_MESSAGE)

                val response = reader.readLine()
                println("Received response from TeacherMicroservice: $response")

                delay(HEARTBEAT_INTERVAL_MS)
            } catch (e: Exception) {
                println("Failed to send heartbeat to TeacherMicroservice: ${e.message}")
                break 
            }
        }
    }
}

fun main() = runBlocking<Unit> {
    val heartbeatMicroservice = HeartBeatMicroservice()
    heartbeatMicroservice.run()
}

