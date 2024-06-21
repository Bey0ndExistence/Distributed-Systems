package com.sd.laborator.services

import com.sd.laborator.interfaces.BlackListInterface
import com.sd.laborator.interfaces.WeatherForecastInterface
import com.sd.laborator.pojo.WeatherForecastData
import org.springframework.stereotype.Service
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import java.io.File

@Service
class BlackListService:BlackListInterface {
    @Autowired
    override lateinit var nextService: WeatherForecastInterface
    override fun isBlocked(locationName: String): WeatherForecastData? {
        val resource = File("src/main/resources/blocked.json") // Replace with the actual file path
        val jsonString = resource.inputStream().bufferedReader().use { it.readText() }

        val jsonObject = JSONObject(jsonString)
        val cities = jsonObject.getJSONArray("blocked").toList().map { it.toString().replace(" ", "%20")}

        return if(locationName in cities){
            null
        }else{
            nextService.getForecastData(locationName)
        }
    }
}