package com.sd.laborator.services

import com.sd.laborator.interfaces.BlackListInterface
import com.sd.laborator.interfaces.LocationSearchInterface
import com.sd.laborator.pojo.WeatherForecastData
import org.springframework.stereotype.Service
import java.net.URL
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class LocationSearchService : LocationSearchInterface {
    @Autowired
    override lateinit var nextService: BlackListInterface
    override fun getLocationId(locationName: String): WeatherForecastData? {
        return nextService.isBlocked(locationName.replace(" ", "%20"))
    }
}