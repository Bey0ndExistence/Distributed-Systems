package com.sd.laborator.services

import com.sd.laborator.interfaces.WeatherServiceChain
import com.sd.laborator.pojo.WeatherForecastData
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class WeatherOrchestrator(
    private val blacklistService: BlacklistService,
    private val locationSearchService: LocationSearchService,
    private val weatherForecastService: WeatherForecastService,
    private val timeService: TimeService
) : WeatherServiceChain {

    init {

        blacklistService.nextService = locationSearchService
        locationSearchService.nextService = timeService
        timeService.nextService = weatherForecastService

    }

    override fun execute(location: String): String {
        return blacklistService.execute(location)
    }

    fun updateBlacklist(blist: String): ResponseEntity<String> {
        blacklistService.updateBlacklist(blist.toLowerCase().split("-").toSet())
        return ResponseEntity.ok("Blacklist updated successfully")
    }
}