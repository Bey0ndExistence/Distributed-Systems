package com.sd.laborator.services

import com.sd.laborator.interfaces.LocationSearchInterface
import com.sd.laborator.interfaces.WeatherServiceChain
import org.springframework.stereotype.Service
import java.net.URL
import org.json.JSONObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class LocationSearchService(var nextService: WeatherServiceChain ?= null) : WeatherServiceChain, LocationSearchInterface {

    override fun execute(location: String): String {
        val locationId = getLocationId(location)
        return nextService?.execute(locationId) ?: "No further operation"
    }

    override fun getLocationId(locationName: String): String {
        // Implementation for getting location ID
        return locationName ?: "NOT_FOUND"
    }
}