package com.sd.laborator.interfaces

import com.sd.laborator.pojo.WeatherForecastData

interface LocationSearchInterface {
    var nextService: BlackListInterface
    fun getLocationId(locationName: String): WeatherForecastData?
}