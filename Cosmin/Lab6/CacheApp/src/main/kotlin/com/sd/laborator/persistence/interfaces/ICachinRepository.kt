package com.sd.laborator.persistence.interfaces

import com.sd.laborator.persistence.entities.CacheEntity

interface ICachinRepository {
    fun getByQuery(query:String):CacheEntity?
    fun add(item: CacheEntity)
}