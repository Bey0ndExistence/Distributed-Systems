package com.sd.laborator.components

import com.sd.laborator.model.Waiter
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.lang.Exception
import kotlin.random.Random

@Component
@Scope("prototype")
class WaiterComponent:Runnable {
    @Autowired
    private lateinit var connectionFactory: RabbitMqConnectionFactoryComponent

    private lateinit var amqpTemplate: AmqpTemplate

    private val waiter: Waiter = Waiter("X"+Random.nextInt(1, 100).toString())


    @Autowired
    fun initTemplate() {
        this.amqpTemplate = connectionFactory.rabbitTemplate()
    }

    @RabbitListener(queues = ["\${restaurantapp.rabbitmq.queue}"])
    fun recieveMessage(msg: String) {
        try {
            val parts = msg.split("&")
            val creator = parts[0].split("=")[1] // Extract creator value
            val sender = parts[1].split("=")[1] // Extract sender value
            val orderType = parts[2].split("=")[1] // Extract order type value
            println("Bucatarul $sender a terminat comanda de tipul $orderType trimisa de $creator")
        } catch (e: Exception) {
            println(e)
        }
    }

    fun sendMessage(msg: String) {
        this.amqpTemplate.convertAndSend(connectionFactory.getExchange(),
            connectionFactory.getRoutingKey(),
            msg)
    }

    override fun run(){
        while (true) {
            Thread.sleep(1000)
            val orderId = Random.nextInt(1, 4)
            sendMessage("sender=" + waiter.id + "&order=" + orderId)
            println("Chelenrul cu id-ul = " +  waiter.id + " trimite o comanda de tinpul " + orderId)
        }
    }
}