package com.sd.laborator.interfaces

import com.sd.laborator.pojo.WeatherForecastData

interface BlackListInterface {
    var nextService: WeatherForecastInterface
    fun isBlocked(locationName: String): WeatherForecastData?
}