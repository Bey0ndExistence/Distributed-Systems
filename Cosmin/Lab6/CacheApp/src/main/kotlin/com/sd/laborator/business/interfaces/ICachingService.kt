package com.sd.laborator.business.interfaces

import com.sd.laborator.business.models.CacheModel

interface ICachingService {
    fun exists(query:String): CacheModel?
    fun addToCache(query:String, result: String)
}