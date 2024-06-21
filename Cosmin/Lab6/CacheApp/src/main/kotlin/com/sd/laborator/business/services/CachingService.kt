package com.sd.laborator.business.services

import com.sd.laborator.business.interfaces.ICachingService
import com.sd.laborator.business.models.CacheModel
import com.sd.laborator.persistence.entities.CacheEntity
import com.sd.laborator.persistence.interfaces.ICachinRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class CachingService @Autowired constructor(private val jdbcTemplate: JdbcTemplate) :ICachingService {
    @Autowired
    private lateinit var cachingRepository:ICachinRepository
    override fun exists(query: String): CacheModel? {
        val res = cachingRepository.getByQuery(query)
        return if(res != null)
            CacheModel.fromEntity(res)
        else null
    }

    override fun addToCache(query: String, result: String) {
        val model = CacheModel(query,result)
        cachingRepository.add(CacheModel.toEntity(model))
    }
}