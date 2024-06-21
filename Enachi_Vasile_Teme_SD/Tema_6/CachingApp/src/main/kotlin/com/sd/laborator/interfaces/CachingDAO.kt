package com.sd.laborator.interfaces

import com.sd.laborator.model.CacheQuery

interface CachingDAO {
    // Create
    fun createQueryTable()

    // Retrieve
    fun getCacheResultByQuery(query: String): String?

    // Insert
    fun insertQuery(cache: CacheQuery)

    // Delete
    fun deleteQuery(query: String)

}