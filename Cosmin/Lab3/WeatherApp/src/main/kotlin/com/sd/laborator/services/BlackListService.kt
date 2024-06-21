package com.sd.laborator.services

import com.sd.laborator.interfaces.BlackListInterface
import org.springframework.stereotype.Service
import org.json.JSONObject
import org.springframework.core.io.ClassPathResource
import java.io.File

@Service
class BlackListService:BlackListInterface {
    override fun isBlocked(locationName: String): Boolean {
        val resource = File("src/main/resources/blocked.json") // Replace with the actual file path
        val jsonString = resource.inputStream().bufferedReader().use { it.readText() }

        val jsonObject = JSONObject(jsonString)
        val cities = jsonObject.getJSONArray("blocked").toList().map { it.toString().replace(" ", "%20")}

        return locationName in cities
    }
}