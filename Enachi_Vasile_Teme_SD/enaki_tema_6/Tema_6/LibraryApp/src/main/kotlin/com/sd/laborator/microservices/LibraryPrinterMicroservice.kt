package com.sd.laborator.microservices

import com.sd.laborator.components.RabbitMqComponent
import com.sd.laborator.interfaces.LibraryDAO
import com.sd.laborator.interfaces.LibraryPrinter
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.awt.print.Book
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@Controller
class LibraryPrinterMicroservice {
    @Autowired
    private lateinit var libraryDAO: LibraryDAO

    @Autowired
    private lateinit var libraryPrinter: LibraryPrinter

    @Autowired
    private lateinit var rabbitMqComponent: RabbitMqComponent

    private lateinit var amqpTemplate: AmqpTemplate

    @Autowired
    fun initTemplate() {
        this.amqpTemplate = rabbitMqComponent.rabbitTemplate()
        clearQueue()
    }

    @RequestMapping("/print", method = [RequestMethod.GET])
    @ResponseBody
    fun customPrint(@RequestParam(required = true, name = "format", defaultValue = "") format: String): String {

        val rabbitQuery = "/print?format=$format"
        sendMessage("__get__~~__query__==$rabbitQuery")

        val parameters = receiveMessage()
        val pairAnalyzer = isQueryFresh(parameters)
        if (pairAnalyzer.first){
            println("This operation is fresh")
            return pairAnalyzer.second
        }

        val result =  when(format) {
            "html" -> libraryPrinter.printHTML(libraryDAO.getBooks().toSet())
            "json" -> libraryPrinter.printJSON(libraryDAO.getBooks().toSet())
            "raw" -> libraryPrinter.printRaw(libraryDAO.getBooks().toSet())
            else -> "Not implemented"
        }
        sendMessage("__insert__~~__query__==$rabbitQuery;;__result__==$result")
        val receivedInsertionMessage = receiveMessage()
        println("Insertion callback: $receivedInsertionMessage")
        return result
    }

    @RequestMapping("/find", method = [RequestMethod.GET])
    @ResponseBody
    fun customFind(@RequestParam(required = false, name = "author", defaultValue = "") author: String,
                   @RequestParam(required = false, name = "title", defaultValue = "") title: String,
                   @RequestParam(required = false, name = "publisher", defaultValue = "") publisher: String): String {
        val rabbitQuery = "/find?author=$author&title=$title&publisher=$publisher"
        sendMessage("__get__~~__query__==$rabbitQuery")
        val parameters = receiveMessage()
        val pairAnalyzer = isQueryFresh(parameters)
        if (pairAnalyzer.first){
            println("This operation is fresh")
            return pairAnalyzer.second
        }
        var result: String
        if (author != "")
            result = this.libraryPrinter.printJSON(this.libraryDAO.findAllByAuthor(author).toSet())
        else if (title != "")
            result = this.libraryPrinter.printJSON(this.libraryDAO.findAllByTitle(title).toSet())
        else if (publisher != "")
            result = this.libraryPrinter.printJSON(this.libraryDAO.findAllByPublisher(publisher).toSet())
        else result =  "Not a valid field"

        sendMessage("__insert__~~__query__==$rabbitQuery;;__result__==$result")
        val receivedInsertionMessage = receiveMessage()
        println("Insertion callback: $receivedInsertionMessage")
        return result
    }

    @RequestMapping("/find_and_print", method = [RequestMethod.GET])
    @ResponseBody
    fun customFindAndPrint(@RequestParam(required = true, name = "format", defaultValue = "") format: String,
                            @RequestParam(required = false, name = "author", defaultValue = "") author: String,
                            @RequestParam(required = false, name = "title", defaultValue = "") title: String,
                           @RequestParam(required = false, name = "publisher", defaultValue = "") publisher: String): String {
        val rabbitQuery = "/find_and_print?format=$format&author=$author&title=$title&publisher=$publisher"
        sendMessage("__get__~~__query__==$rabbitQuery")
        val parameters = receiveMessage()
        val pairAnalyzer = isQueryFresh(parameters)
        if (pairAnalyzer.first){
            println("This operation is fresh")
            return pairAnalyzer.second
        }
        var result: String
        val lambdaFunction =  when(format) {
            "html" -> libraryPrinter::printHTML
            "json" -> libraryPrinter::printJSON
            "raw" -> libraryPrinter::printRaw
            else -> return "Not implemented"
        }

        result = if (author != "")
            lambdaFunction(this.libraryDAO.findAllByAuthor(author).toSet())
        else if (title != "")
            lambdaFunction(this.libraryDAO.findAllByTitle(title).toSet())
        else if (publisher != "")
            lambdaFunction(this.libraryDAO.findAllByPublisher(publisher).toSet())
        else "Not a valid field"

        sendMessage("__insert__~~__query__==$rabbitQuery;;__result__==$result")
        val receivedInsertionMessage = receiveMessage()
        println("Insertion callback: $receivedInsertionMessage")
        return result
    }


    fun isQueryFresh(parameters: String) : Pair<Boolean, String>{
        println(parameters)
        if (parameters == "invalid_request" || parameters == "empty_result" || parameters == "not_succesful_operation") {
            return Pair(false, "null")
        }
        var time: String? = null
        var queryResult: String? = null

        val delimiter_1 = """__time__=="""
        val delimiter_2 = """__result__=="""

        val split = parameters.split(";;")
        for (item in split) {
            if (item.contains(delimiter_1)) {
                time = item.split(delimiter_1)[1]
            } else if (item.contains(delimiter_2)) {
                queryResult = item.split(delimiter_2)[1]
            }
        }
        println("time = $time")
        println("QueryResult = $queryResult")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(time, formatter)
        val now = LocalDateTime.now()

        val hours: Long = dateTime.until(now, ChronoUnit.HOURS)
        if (hours < 1) {
            return Pair(true, queryResult ?: "null")
        }
        return Pair(false, "null")
    }

    fun receiveMessage() : String {
        var msg = this.amqpTemplate.receive("tema_6.queue")
        //if queue is empty receive will get null
        while (msg == null){
            msg = this.amqpTemplate.receive("tema_6.queue")
        }
        println(msg.body)
        val processed_msg = (msg.body.map { it.toInt().toChar() }).joinToString(separator = "")
        return processed_msg
    }

    fun clearQueue() {
        while(this.amqpTemplate.receive("tema_6.queue") != null);
        println("Cleared Queue")
    }

    fun sendMessage(msg: String) : String{
        println("message: $msg")
        return this.amqpTemplate.convertAndSend(rabbitMqComponent.getExchange(), rabbitMqComponent.getRoutingKey(), msg).toString()
    }
}