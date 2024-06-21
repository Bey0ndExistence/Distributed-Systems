package com.sd.laborator.services

import com.sd.laborator.interfaces.WeatherForecastInterface
import com.sd.laborator.pojo.WeatherForecastData
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.net.URL
import kotlin.math.roundToInt

@Service
class WeatherForecastService (private val timeService: TimeService) : WeatherForecastInterface {
    override fun getForecastData(locationId: String): WeatherForecastData {
        // ID-ul locaţiei nu trebuie codificat, deoarece este numeric
        val forecastDataURL = URL(" https://wttr.in/$locationId?format=j1")

        // preluare conţinut răspuns HTTP la o cerere GET către URL-ul de mai sus
        val rawResponse: String = forecastDataURL.readText()

        // parsare obiect JSON primit
        val responseRootObject = JSONObject(rawResponse)
        val currentWeatherDataObject = responseRootObject.getJSONArray("current_condition").getJSONObject(0)
        val areaDataObject = responseRootObject.getJSONArray("nearest_area").getJSONObject(0)
        val weatherDataObject = responseRootObject.getJSONArray("weather").getJSONObject(0)

        // construire şi returnare obiect POJO care încapsulează datele meteo
        return WeatherForecastData(
            location = areaDataObject.getJSONArray("areaName").getJSONObject(0).getString("value"),
            date = timeService.getCurrentTime(),
            weatherState = currentWeatherDataObject.getJSONArray("weatherDesc").getJSONObject(0).getString("value"),
            windDirection = currentWeatherDataObject.getString("winddir16Point"),
            windSpeed = currentWeatherDataObject.getFloat("windspeedKmph").roundToInt(),
            minTemp = weatherDataObject.getFloat("mintempC").roundToInt(),
            maxTemp = weatherDataObject.getFloat("maxtempC").roundToInt(),
            currentTemp = currentWeatherDataObject.getFloat("temp_C").roundToInt(),
            humidity = currentWeatherDataObject.getFloat("humidity").roundToInt()
        )
    }
}