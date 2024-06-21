package com.sd.laborator.components

import com.sd.laborator.interfaces.CartesianProductOperation
import com.sd.laborator.interfaces.PrimeNumberGenerator
import com.sd.laborator.interfaces.UnionOperation
import com.sd.laborator.model.Stack
import com.sd.laborator.services.ARegenerator
import com.sd.laborator.services.BRegenerator
import com.sd.laborator.services.Regenerator
import com.sd.laborator.services.ReunionOfTheCartesian
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class StackAppComponent {
    private var A: Stack = Stack(0)
    private var B: Stack = Stack(0)



    @Autowired
    private lateinit var connectionFactory: RabbitMqConnectionFactoryComponent
    private lateinit var amqpTemplate: AmqpTemplate

    @Autowired
    fun initTemplate() {
        this.amqpTemplate = connectionFactory.rabbitTemplate()
    }

    private var services: Regenerator = ARegenerator()
    init {
        this.services.setNext(BRegenerator())
        this.services.setNext(ReunionOfTheCartesian())
    }

    @RabbitListener(queues = ["\${stackapp.rabbitmq.queue}"])
    fun recieveMessage(msg: String) {
        // the result: 114,101,103,101,110,101,114,97,116,101,95,65 --> needs processing
        println("Receive Message: {$msg}")
        val processed_msg = (msg.split(",").map { it.toInt().toChar() }).joinToString(separator="")
        println("Processed Msg: {$processed_msg}")
        var result: String? =services.executeOperation(A, B, processed_msg)
        println("result: ")
        println(result)
        if (result != null) sendMessage(result)
    }

    fun sendMessage(msg: String) {
        println("message: ")
        println(msg)
        this.amqpTemplate.convertAndSend(connectionFactory.getExchange(),
            connectionFactory.getRoutingKey(),
            msg)
    }
}
