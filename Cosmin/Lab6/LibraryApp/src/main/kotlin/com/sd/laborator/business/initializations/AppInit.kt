package com.sd.laborator.business.initializations

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class AppInit @Autowired constructor(private val jdbcTemplate: JdbcTemplate): ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val sql = """
            CREATE TABLE IF NOT EXISTS book (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                author VARCHAR NOT NULL,
                title VARCHAR NOT NULL,
                publisher VARCHAR NOT NULL,
                text TEXT NOT NULL
            )
        """.trimIndent()
        jdbcTemplate.execute(sql)
    }
}