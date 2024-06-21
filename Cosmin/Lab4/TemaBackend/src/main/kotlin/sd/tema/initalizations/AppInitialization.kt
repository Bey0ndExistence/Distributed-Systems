package sd.tema.initalizations

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component


@Component
class AppInitialization @Autowired constructor(private val jdbcTemplate: JdbcTemplate): ApplicationRunner{
    override fun run(args: ApplicationArguments?) {
        val sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                credential VARCHAR(60) NOT NULL,
                firstname VARCHAR(100) NOT NULL,
                lastname VARCHAR(100) NOT NULL
            )
        """.trimIndent()
        jdbcTemplate.execute(sql)
        val expensesTable = """
            CREATE TABLE IF NOT EXISTS expenses (
                id INTEGER,
                expense_name VARCHAR(60),
                value INTEGER
            )
        """.trimIndent()
        jdbcTemplate.execute(expensesTable)
    }
}