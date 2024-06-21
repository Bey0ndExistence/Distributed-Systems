package com.sd.laborator

import com.sd.laborator.components.WaiterComponent
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication



@SpringBootApplication
class StackApp

fun main(args: Array<String>) {
    val context = runApplication<StackApp>(*args)
    val waiterComponent = context.getBean(WaiterComponent::class.java)
    val waiterComponent1 = context.getBean(WaiterComponent::class.java)
    val waiterThread = Thread(waiterComponent)
    val waiterThread1 = Thread(waiterComponent1)
    waiterThread.start()
    waiterThread1.start()
}