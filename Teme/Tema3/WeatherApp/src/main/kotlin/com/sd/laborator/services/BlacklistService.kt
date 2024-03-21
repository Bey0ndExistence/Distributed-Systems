package com.sd.laborator.services

import com.sd.laborator.interfaces.BlacklistInterface
import com.sd.laborator.interfaces.WeatherServiceChain
import org.springframework.stereotype.Service

@Service
class BlacklistService(
    var nextService: WeatherServiceChain? = null
) : WeatherServiceChain {
    private var blackList: Set<String> = emptySet()

    override fun execute(location: String): String {
        if (isAllowed(location)) {
            return nextService?.execute(location) ?: "No further processing"
        }
        return "Forbidden"
    }

    private fun isAllowed(location: String): Boolean {
        return !blackList.contains(location.toLowerCase())
    }

    fun updateBlacklist(updatedBlacklist: Set<String>) {
        blackList = updatedBlacklist
    }
}