package com.sd.laborator.services

import com.sd.laborator.interfaces.*
import com.sd.laborator.pojo.WeatherForecastData
import org.springframework.stereotype.Service

@Service
class OrchestratorService(
    private val blackListService: BlackListInterface,
    private val locationSearchService: LocationSearchInterface,
    private val timeService: TimeInterface,
    private val weatherForecastService: WeatherForecastInterface
):OrchestratorInterface {
    override fun orchestrate(location: String): String {
        val locationID = locationSearchService.getLocationId(location)
        return if(!blackListService.isBlocked(locationID)){
            val time = timeService.getCurrentTime()
            weatherForecastService.getForecastData(locationID, time).toString()
        }else {
            "NU AVETI ACCES LA INFORMATILE LOCATIEI $location din Iasi"
        }
    }
}