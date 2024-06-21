package com.sd.laborator.business.models

import com.sd.laborator.persistence.entities.CacheEntity


class CacheModel(private var query: String, private var result: String) {

    fun getQuery(): String {
        return query
    }

    fun setQuery(query: String) {
        this.query = query
    }

    fun getResult(): String {
        return result
    }

    fun setResult(result: String) {
        this.result = result
    }
    companion object {
        fun fromEntity(item:CacheEntity):CacheModel{
            return CacheModel(item.query!!, item.result!!)
        }
        fun toEntity(item:CacheModel):CacheEntity{
            return CacheEntity(null, null, item.query,item.result)
        }
    }
}