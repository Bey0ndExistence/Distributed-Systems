package com.sd.laborator.presentation.controllers

import com.sd.laborator.business.interfaces.ICachingService
import com.sd.laborator.business.services.CachingService
import com.sd.laborator.presentation.config.RabbitMqComponent
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.client.RestTemplate

@Component
class CachingController @Autowired constructor(private val restTemplate: RestTemplate) {
    @Autowired
    private lateinit var cachingService: ICachingService
    @Autowired
    private lateinit var connectionFactory: RabbitMqComponent

    private lateinit var amqpTemplate: AmqpTemplate
    @Autowired
    fun initTemplate() {
        this.amqpTemplate = connectionFactory.rabbitTemplate()
    }

    @RabbitListener(queues = ["\${libraryapp.rabbitmq.queue}"])
    /*fun recieveMessage(msg: String) {
        try {
            val processed_msg = msg.split('=')
            if(processed_msg[0] == "query"){
                val res = cachingService.exists(processed_msg[1])
                if(res != null){
                    sendMessage(res.getResult())
                }else return sendMessage("MISS")
            }else if (processed_msg[0] == "add"){
                val (query, result) = processed_msg[1].split("&&")
                cachingService.addToCache(query, result)
            }
        } catch (e: Exception) {
            println(e)
        }
    }*/
    fun recieveMessage(msg: String) {
        try {
            val res = cachingService.exists(msg)
            if(res != null){
                sendMessage(res.getResult())
            }
            else {
                val response: ResponseEntity<String> = restTemplate.getForEntity(msg, String::class.java)

                val responseBody: String? = response.body
                if(responseBody != null){
                    println("raspuns$responseBody")
                    println("request$msg")
                    cachingService.addToCache(msg, responseBody)
                    sendMessage(responseBody)
                }else {sendMessage("error")}
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    fun sendMessage(msg: String) {
        this.amqpTemplate.convertAndSend(connectionFactory.getExchange(),
            connectionFactory.getRoutingKey(),
            msg)
    }
}