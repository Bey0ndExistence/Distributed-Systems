package com.sd.laborator.presentation.controllers

import com.sd.laborator.business.interfaces.ILibraryDAOService
import com.sd.laborator.business.interfaces.ILibraryPrinterService
import com.sd.laborator.presentation.config.RabbitMqComponent
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class LibraryPrinterController {
    @Autowired
    private lateinit var _libraryDAOService: ILibraryDAOService

    @Autowired
    private lateinit var _libraryPrinterService: ILibraryPrinterService

    @Autowired
    private lateinit var connectionFactory: RabbitMqComponent

    private lateinit var amqpTemplate: AmqpTemplate
    @Autowired
    fun initTemplate() {
        this.amqpTemplate = connectionFactory.rabbitTemplate()
    }

    @RequestMapping("/print", method = [RequestMethod.GET])
    @ResponseBody
    fun customPrint(@RequestParam(required = true, name = "format", defaultValue = "") format: String): String {
        return when (format) {
            "html" -> _libraryPrinterService.printHTML(_libraryDAOService.getBooks())
            "json" -> _libraryPrinterService.printJSON(_libraryDAOService.getBooks())
            "raw" -> _libraryPrinterService.printRaw(_libraryDAOService.getBooks())
            else -> "Not implemented"
        }
    }

    @RequestMapping("/find", method = [RequestMethod.GET])
    @ResponseBody
    fun customFind(
        @RequestParam(required = false, name = "author", defaultValue = "") author: String,
        @RequestParam(required = false, name = "title", defaultValue = "") title: String,
        @RequestParam(required = false, name = "publisher", defaultValue = "") publisher: String
    ): String {
        if (author != "")
            return this._libraryPrinterService.printJSON(this._libraryDAOService.findAllByAuthor(author))
        if (title != "")
            return this._libraryPrinterService.printJSON(this._libraryDAOService.findAllByTitle(title))
        if (publisher != "")
            return this._libraryPrinterService.printJSON(this._libraryDAOService.findAllByPublisher(publisher))
        return "Not a valid field"
    }

    @RequestMapping("/find-and-print", method = [RequestMethod.GET])
    @ResponseBody
    fun specialFind(
        @RequestParam(required = false, name = "author", defaultValue = "") author: String,
        @RequestParam(required = false, name = "title", defaultValue = "") title: String,
        @RequestParam(required = false, name = "publisher", defaultValue = "") publisher: String,
        @RequestParam(required = true, name = "format", defaultValue = "") format: String
    ):String {
        if(format == "json"){
            if (author != "")
                return this._libraryPrinterService.printJSON(this._libraryDAOService.findAllByAuthor(author))
            if (title != "")
                return this._libraryPrinterService.printJSON(this._libraryDAOService.findAllByTitle(title))
            if (publisher != "")
                return this._libraryPrinterService.printJSON(this._libraryDAOService.findAllByPublisher(publisher))
        }else if(format == "html"){
            if (author != "")
                return this._libraryPrinterService.printHTML(this._libraryDAOService.findAllByAuthor(author))
            if (title != "")
                return this._libraryPrinterService.printHTML(this._libraryDAOService.findAllByTitle(title))
            if (publisher != "")
                return this._libraryPrinterService.printHTML(this._libraryDAOService.findAllByPublisher(publisher))
        }else if(format == "raw"){
            if (author != "")
                return this._libraryPrinterService.printRaw(this._libraryDAOService.findAllByAuthor(author))
            if (title != "")
                return this._libraryPrinterService.printRaw(this._libraryDAOService.findAllByTitle(title))
            if (publisher != "")
                return this._libraryPrinterService.printRaw(this._libraryDAOService.findAllByPublisher(publisher))
        }
        return "Not a valid format or field"
    }
}