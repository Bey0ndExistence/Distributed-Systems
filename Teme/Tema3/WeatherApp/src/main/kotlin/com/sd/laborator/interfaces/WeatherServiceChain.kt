package com.sd.laborator.interfaces

interface WeatherServiceChain {
    fun execute(location: String): String
}
