package com.sd.laborator

import com.sd.laborator.business.services.TimeStampService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate


@SpringBootApplication
open class LibraryApp{
    @Bean
    open fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}

fun main(args: Array<String>) {
    val context = runApplication<LibraryApp>(*args)
    val timeStampService = context.getBean(TimeStampService::class.java)
    val timeStampThread = Thread(timeStampService)
    timeStampThread.start()
}
