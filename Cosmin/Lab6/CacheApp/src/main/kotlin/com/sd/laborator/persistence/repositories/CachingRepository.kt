package com.sd.laborator.persistence.repositories

import com.sd.laborator.persistence.entities.CacheEntity
import com.sd.laborator.persistence.interfaces.ICachinRepository
import com.sd.laborator.persistence.mappers.CacheRowMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.UncategorizedSQLException

@Repository
class CachingRepository @Autowired constructor(private val jdbcTemplate: JdbcTemplate) :ICachinRepository {

    private var _rowMapper: RowMapper<CacheEntity?> = CacheRowMapper()

    override fun getByQuery(query: String): CacheEntity? {
        return try{
            jdbcTemplate.queryForObject("""SELECT * FROM cache WHERE query = '$query'""".trim(), _rowMapper)
        }catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    override fun add(item: CacheEntity) {
        try {
            jdbcTemplate.update("""INSERT INTO cache( timestamp, query, result) VALUES ( ?, ?, ?)""".trim(),
                 System.currentTimeMillis(), item.query, item.result)
        } catch (e: UncategorizedSQLException){
            println("An error has occurred in ${this.javaClass.name}.add")
            throw e
        }
    }

}