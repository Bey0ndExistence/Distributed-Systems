package com.sd.laborator

import com.sd.laborator.components.CookComponent
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync


@SpringBootApplication
@EnableAsync
class StackApp

fun main(args: Array<String>) {
    val context = runApplication<StackApp>(*args)
    val cookComponent = context.getBean(CookComponent::class.java)
    val cookComponent1 = context.getBean(CookComponent::class.java)
    val cookThread = Thread(cookComponent)
    val cookThread1 = Thread(cookComponent1)
    cookThread.start()
    cookThread1.start()
}