package sd.tema.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import sd.tema.interfaces.AuthInterface

@Service
class AuthService @Autowired constructor(private val jdbcTemplate: JdbcTemplate, private val encryptionService: EncryptionService) : AuthInterface {
    override fun auth(username: String, password: String): Int?{
        val hashEd = encryptionService.hash(username,password)
        val query = "SELECT (id) FROM users WHERE credential = ?"
        val result = jdbcTemplate.queryForList(query, hashEd)
        return if (result.isEmpty()) {
            null
        }else{
            result[0]["id"] as Int
        }
    }
}