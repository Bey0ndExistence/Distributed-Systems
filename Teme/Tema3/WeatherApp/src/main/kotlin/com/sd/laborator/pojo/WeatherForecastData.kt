package com.sd.laborator.pojo

data class WeatherForecastData (
    var location: String,
    var country: String,
    var date: String,
    var weatherState: String,
    var windDirection: String,
    var windSpeed: Int, // km/h
    var currentTemp: Int,
    var humidity: Int// procent

)
