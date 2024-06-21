package com.sd.laborator.business.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class TimeStampService @Autowired constructor(private val jdbcTemplate: JdbcTemplate):Runnable {

    override fun run() {
        while (true) {
            val query = """
                SELECT id FROM cache WHERE timestamp < ?
                """.trimIndent()
            val res = jdbcTemplate.queryForList(query, System.currentTimeMillis() - (60 * 60 * 1000))
            for(id in res){
                val deleteQuery = """
                    DELETE FROM cache WHERE id = ?
                """.trimIndent()
                jdbcTemplate.update(deleteQuery, id)
            }
            Thread.sleep(120000)
        }
    }
}