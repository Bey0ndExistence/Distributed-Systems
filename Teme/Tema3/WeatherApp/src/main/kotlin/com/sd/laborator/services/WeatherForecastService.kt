package com.sd.laborator.services

import com.sd.laborator.interfaces.WeatherForecastInterface
import com.sd.laborator.interfaces.WeatherServiceChain
import com.sd.laborator.pojo.WeatherForecastData
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.net.URL
import kotlin.math.roundToInt

@Service
class WeatherForecastService(private val timeService: TimeService) : WeatherServiceChain, WeatherForecastInterface {

    override fun execute(loc_time_string: String): String {
        val loc_time_list = loc_time_string.split("\\s+".toRegex())
        val loc = loc_time_list[0]
        val time = loc_time_list[1] + " " + loc_time_list[2]
        val forecastData = getForecastData(loc, time)
        return forecastData.toString()
    }

    override fun getForecastData(locationId: String, time: String): WeatherForecastData {
        // Implementation for fetching forecast data

        // ID-ul locaţiei nu trebuie codificat, deoarece este numeric
        val forecastDataURL = URL("https://wttr.in/$locationId?format=j1")

        // preluare conţinut răspuns HTTP la o cerere GET către URL-ul de mai sus
        val rawResponse: String = forecastDataURL.readText()

        // parsare obiect JSON primit
        val responseRootObject = JSONObject(rawResponse)
        val weatherDataObject = responseRootObject.getJSONArray("current_condition").getJSONObject(0)

        // construire şi returnare obiect POJO care încapsulează datele meteo
        return WeatherForecastData(
            location = responseRootObject.getJSONArray("nearest_area").getJSONObject(0).getJSONArray("areaName")
                .getJSONObject(0).getString("value"),
            date = time,
            weatherState = weatherDataObject.getJSONArray("weatherDesc").getJSONObject(0).getString("value"),
            windDirection = weatherDataObject.getString("winddir16Point"),
            windSpeed = weatherDataObject.getFloat("windspeedKmph").roundToInt(),
            currentTemp = weatherDataObject.getFloat("temp_C").roundToInt(),
            humidity = weatherDataObject.getFloat("humidity").roundToInt(),
            country = responseRootObject.getJSONArray("nearest_area").getJSONObject(0).getJSONArray("country")
                .getJSONObject(0).getString("value")
        )
    }
}

