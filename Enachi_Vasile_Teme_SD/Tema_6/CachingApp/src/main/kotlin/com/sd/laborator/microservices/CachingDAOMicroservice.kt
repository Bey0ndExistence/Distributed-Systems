package com.sd.laborator.microservices

import com.sd.laborator.components.RabbitMqComponent
import com.sd.laborator.interfaces.CachingDAO
import com.sd.laborator.model.CacheQuery
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.UncategorizedSQLException
import org.springframework.stereotype.Component

@Component
class CachingDAOMicroservice {
    @Autowired
    private lateinit var cachingDAO: CachingDAO

    @Autowired
    private lateinit var rabbitMqComponent: RabbitMqComponent

    private lateinit var amqpTemplate: AmqpTemplate

    @Autowired
    fun initTemplate() {
        this.amqpTemplate = rabbitMqComponent.rabbitTemplate()
        clearQueue()
    }
    //citesc din queue1
    // scriu in queue
    @RabbitListener(queues = ["\${tema_6.rabbitmq.queue}"])
    fun receiveMessage(msg: String) {
        try {
            println("Message: ${msg}")

            //val processed_msg = (msg.split(",").map { it.toInt().toChar() }).joinToString(separator = "")
            val processed_msg = msg
            println(processed_msg)
            val (operation, parameters) = processed_msg.split("~~")
            var query: String? = null
            var queryResult: String? = null

            val delimiter_1 = """__query__=="""
            val delimiter_2 = """__result__=="""

            val split = parameters.split(";;")
            for (item in split){
                if (item.contains(delimiter_1)){
                    query = item.split(delimiter_1)[1]
                } else if (item.contains(delimiter_2)){
                    queryResult = item.split(delimiter_2)[1]
                }
            }
            println("Query = $query")
            println("QueryResult = $queryResult")
            var result: Any? = when (operation) {
                "__create__" -> cachingDAO.createQueryTable()
                "__get__" -> query?.let { cachingDAO.getCacheResultByQuery(it) }
                "__insert__" -> query?.let {
                    queryResult?.let {
                        cachingDAO.insertQuery(
                            CacheQuery(
                                -1,
                                "irellevant",
                                query,
                                queryResult
                            )
                        )
                    }
                }
                "__delete__" -> query?.let { cachingDAO.deleteQuery(it) }
                else -> null
            }
            println("result: $result")
            if (result is Unit){
                sendMessage("succesful_operation")
                return
            }
            if (result != null) sendMessage(result.toString())
        }
        catch(e: Exception){
            when(e){
                is EmptyResultDataAccessException -> {
                    sendMessage("empty_result")
                }
                is UncategorizedSQLException -> {
                    sendMessage("not_succesful_operation")
                }
                else -> {
                    sendMessage("invalid_request")
                }

            }
        }
    }

    fun sendMessage(msg: String) {
        println("message: $msg")
        this.amqpTemplate.convertAndSend(rabbitMqComponent.getExchange(), rabbitMqComponent.getRoutingKey(), msg)
    }


    fun clearQueue() {
        while(this.amqpTemplate.receive("tema_6.queue") != null);
        println("Cleared Queue")
    }

}