package com.sd.laborator.controllers

import com.sd.laborator.interfaces.LocationSearchInterface
import com.sd.laborator.interfaces.WeatherForecastInterface
import com.sd.laborator.interfaces.BlackListInterface
import com.sd.laborator.pojo.WeatherForecastData
import com.sd.laborator.services.TimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class WeatherAppController {
    @Autowired
    private lateinit var locationSearchService: LocationSearchInterface

    @Autowired
    private lateinit var weatherForecastService: WeatherForecastInterface

    @Autowired
    private lateinit var blackListInterface: BlackListInterface

    @RequestMapping("/getforecast/{location}", method = [RequestMethod.GET])
    @ResponseBody
    fun getForecast(@PathVariable location: String): String {
        // se incearca preluarea WOEID-ului locaţiei primite in URL
        val locationId = locationSearchService.getLocationId(location)
        val rawForecastData: WeatherForecastData
        // dacă locaţia nu a fost găsită, răspunsul va fi corespunzător
        if (locationId == null) {
            return "Nu s-au putut gasi date meteo pentru cuvintele cheie \"$location\"!"
        }else{
            if(!blackListInterface.isBlocked(locationId)) {
                rawForecastData = weatherForecastService.getForecastData(locationId)
            }else{
                return "Nu aveti acces la informatii despre locatia \"$location\" din Iasi"
            }
        }

        // pe baza ID-ului de locaţie, se interoghează al doilea serviciu care returnează datele meteo
        // încapsulate într-un obiect POJO


        // fiind obiect POJO, funcţia toString() este suprascrisă pentru o afişare mai prietenoasă
        return rawForecastData.toString()
    }
}