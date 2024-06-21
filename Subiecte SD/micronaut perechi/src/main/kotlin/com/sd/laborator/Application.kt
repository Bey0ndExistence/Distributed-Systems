package com.sd.laborator

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.runtime.Micronaut
import kotlin.random.Random

object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.run(Application::class.java, *args)
    }

    @Controller
    class LambdaController {
        @Post
        fun execute(): AlgorithmResponse {
            println(handler.get())
            return handler.get()
        }

        companion object {
            val A = List(1000){ Random.nextInt(0, 100)}.sorted()
            val B = List(1000){ Random.nextInt(0, 100)}.sorted()

            private val handler = AlgorithmFunction(A, B)
        }
    }
}