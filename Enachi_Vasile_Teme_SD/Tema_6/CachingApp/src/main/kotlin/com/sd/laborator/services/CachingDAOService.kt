package com.sd.laborator.services

import com.sd.laborator.interfaces.CachingDAO
import com.sd.laborator.model.CacheQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import java.sql.ResultSet
import java.sql.SQLException
import java.util.regex.Pattern

class CachingRowMapper : RowMapper<CacheQuery?> {
    @Throws(SQLException::class)
    override fun mapRow(rs: ResultSet, rowNum: Int): CacheQuery {
        return CacheQuery(rs.getInt("id"), rs.getString("timestamp"), rs.getString("query"), rs.getString("result"))
    }
}

@Service
class CachingDAOService: CachingDAO {
    //Spring Boot will automatically wire this object using application.properties:
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate
    var pattern: Pattern = Pattern.compile("\\W")

    override fun createQueryTable() {
        jdbcTemplate.execute("""CREATE TABLE "cache" ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, timestamp datetime, query VARCHAR, result VARCHAR, UNIQUE(timestamp, query, result) );""")
    }

    override fun getCacheResultByQuery(query: String): String?{
        /*
        if(pattern.matcher(query).find()) {
            println("SQL Injection for query name")
            return null
        }*/
        val result: CacheQuery? = jdbcTemplate.queryForObject("SELECT * FROM cache WHERE query = '$query';", CachingRowMapper())
        return "__time__==${result?.cacheTimestamp};;__result__==${result?.cacheResult}"
    }

    override fun insertQuery(cache: CacheQuery) {
        jdbcTemplate.update("""INSERT OR REPLACE INTO cache(timestamp , query, result) VALUES (datetime('now', 'localtime'), ?, ?);""", cache.cacheQuery, cache.cacheResult)
    }

    override fun deleteQuery(query: String) {
        jdbcTemplate.update("DELETE FROM cache WHERE query = ?;", query)
    }
}