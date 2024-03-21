package com.sd.laborator.services

import com.sd.laborator.interfaces.WeatherServiceChain
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

@Service
class TimeService : WeatherServiceChain{
    var nextService: WeatherServiceChain? = null
    override fun execute(location: String): String {
        val time = getCurrentTime()
        val loc_time = "$location $time"
        return nextService?.execute(loc_time) ?: "No further processing"
    }

    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
        return formatter.format(Date())
    }
}