package com.sd.laborator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Source
import org.springframework.context.annotation.Bean
import org.springframework.integration.annotation.InboundChannelAdapter
import org.springframework.integration.annotation.Poller
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import java.util.*

@EnableBinding(Source::class)
@SpringBootApplication
open class SpringDataFlowTimeSourceApplication {
    val commands = listOf<String>("ls . | grep d | wc -l", "fortune | grep a | wc -l")

    @Bean
    @InboundChannelAdapter(value = Source.OUTPUT, poller = [Poller(fixedDelay = "10000", maxMessagesPerPoll = "1")])
    open fun timeMessageSource(): () -> Message<String> {
        return { MessageBuilder.withPayload(commands.random()).build() }
    }
}

fun main(args: Array<String>) {
    runApplication<SpringDataFlowTimeSourceApplication>(*args)
}