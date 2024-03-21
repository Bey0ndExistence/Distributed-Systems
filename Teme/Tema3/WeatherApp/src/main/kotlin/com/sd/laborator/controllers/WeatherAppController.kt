package com.sd.laborator.controllers

import com.sd.laborator.interfaces.LocationSearchInterface
import com.sd.laborator.interfaces.WeatherForecastInterface
import com.sd.laborator.pojo.WeatherForecastData
import com.sd.laborator.services.BlacklistService
import com.sd.laborator.services.WeatherOrchestrator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class WeatherAppController(

    @Autowired
    private val weatherOrchestrator: WeatherOrchestrator
) {

    @RequestMapping("/getforecast/{location}", method = [RequestMethod.GET])
    @ResponseBody
    fun getForecast(@PathVariable location: String): String {
        return weatherOrchestrator.execute(location)
    }

    @PostMapping("/update/{blist}")
    fun updateBlacklist(@PathVariable("blist") blist: String): ResponseEntity<String> {
        return weatherOrchestrator.updateBlacklist(blist)
    }
}