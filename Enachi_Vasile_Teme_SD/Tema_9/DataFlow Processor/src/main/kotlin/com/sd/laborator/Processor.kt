package com.sd.laborator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Processor
import org.springframework.integration.annotation.Transformer
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder

@EnableBinding(Processor::class)
@SpringBootApplication
open class SpringDataFlowTimeProcessorApplication {
    @Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
    fun transform(command: String?): Any? {
        if (command != null) {
            var current: String?
            var next = command
            var result = ""

            var flag = true
            while (flag) {

                val parts = next!!.split("|", limit = 2)
                current = parts[0]
                if (parts.size > 1) {
                    next = parts[1]
                } else {
                    flag = false
                }

                println("Executing command $current")
                val proc: Process = Runtime.getRuntime().exec(current)
                println("Processing")
                if (result != null) {
                    proc.outputStream.write(result.toByteArray())
                    proc.outputStream.flush()
                    proc.outputStream.close()
                }

                val stdInput = BufferedReader(InputStreamReader(proc.inputStream))


                var builder = StringBuilder("")
                var char: Int = stdInput.read()
                while (char != -1) {
                    builder.append(char.toChar())
                    char = stdInput.read()
                }
                result = builder.toString()

                return if (flag) {
                    "$result -> $next"
                } else {
                    result
                }
            }
        }
        return null
    }
}

fun main(args: Array<String>) {
    runApplication<SpringDataFlowTimeProcessorApplication>(*args)
}