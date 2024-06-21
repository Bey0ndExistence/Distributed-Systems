package com.sd.laborator.interfaces

import com.sd.laborator.pojo.WeatherForecastData

interface OrchestratorInterface {
    fun orchestrate(location:String):String
}