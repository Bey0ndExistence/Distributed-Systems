package com.sd.laborator.components

import com.sd.laborator.model.Cook
import com.sd.laborator.model.Order
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.lang.Exception
import kotlin.random.Random

@Component
@Scope("prototype")
class CookComponent:Runnable {
    @Autowired
    private lateinit var connectionFactory: RabbitMqConnectionFactoryComponent

    private lateinit var amqpTemplate: AmqpTemplate

    private val cook: Cook = Cook("C"+ Random.nextInt(1, 100).toString(),)
    private var  orderList:MutableList<Order> = mutableListOf(Order(1,"2"))

    @Autowired
    fun initTemplate() {
        this.amqpTemplate = connectionFactory.rabbitTemplate()
    }

    @RabbitListener(queues = ["\${restaurantapp.rabbitmq.queue}"])
    fun recieveMessage(msg: String) {
        try {
            val parts = msg.split("&")
            val sender = parts[0].split("=")[1] // Extract sender value
            val order = parts[1].split("=")[1].toInt() // Extract order value and convert to integer
            addElem(order, sender)
        } catch (e: Exception) {
            println(e)
        }
    }

    fun addElem(id:Int, sender:String){
        if(orderList.size <= 5){
            orderList.add(Order(id, sender))
        } else println("Bucatarul ${cook.id} Este ocupat")
    }

    fun sendMessage(msg: String) {
        this.amqpTemplate.convertAndSend(connectionFactory.getExchange(),
            connectionFactory.getRoutingKey(),
            msg)
    }

    override fun run(){
        while (true) {
            if(orderList.isNotEmpty()){
                println("Bucatarul ${cook.id} a inceput munca ")
                val order:Order = orderList[0]
                when(order.id){
                    1->Thread.sleep(500)
                    2->Thread.sleep(1000)
                    3->Thread.sleep(1500)
                }
                orderList.removeAt(0)

                for (i in 1 until orderList.size) {
                    orderList[i - 1] = orderList[i]
                }
                if (orderList.isNotEmpty()) {
                    orderList.removeAt(orderList.size - 1)
                }
                sendMessage("creator="+ order.sender + "&sender=" + cook.id + "&orderType=" + order.id)
            }
        }
    }
}