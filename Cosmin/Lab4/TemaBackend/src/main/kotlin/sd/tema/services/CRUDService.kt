package sd.tema.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.jdbc.core.JdbcTemplate
import sd.tema.interfaces.CRUDInterface

@Service
class CRUDService @Autowired constructor(private val jdbcTemplate: JdbcTemplate, private val encryptionService: EncryptionService) : CRUDInterface {

    override fun createUser(username:String, password: String, firstname:String, lastname:String ){
        try {
            val insertDataSQL = """
            INSERT INTO users (credential, firstname, lastname ) VALUES (?, ?, ? )
        """.trimIndent()

            jdbcTemplate.update(insertDataSQL, encryptionService.hash(username,password), encryptionService.encrypt(firstname), encryptionService.encrypt(lastname))
        }catch (e: Exception) {
            println("Error inserting data: ${e.message}")
        }
    }

    override fun getUser(): List<Map<*, *>> {
        val selected = "SELECT firstname, lastname FROM users"
        val result = jdbcTemplate.queryForList(selected)

        val mappedResult = result.map { row ->
            val encryptedFirstname = row["firstname"] as String
            val firstname = encryptionService.decrypt(encryptedFirstname)

            val encryptedLastname = row["lastname"] as String
            val lastname = encryptionService.decrypt(encryptedLastname)

            mapOf(
                *row.toMutableMap().apply {
                    this["firstname"] = firstname
                    this["lastname"] = lastname
                }.toList().toTypedArray()
            )
        }

        return mappedResult
    }

    override fun getUser(id: Int): List<Map<*, *>> {
        val selected = "SELECT firstname, lastname FROM users WHERE id = ?"
        val result = jdbcTemplate.queryForList(selected, id)

        val mappedResult = result.map { row ->
            val encryptedFirstname = row["firstname"] as String
            val firstname = encryptionService.decrypt(encryptedFirstname)

            val encryptedLastname = row["lastname"] as String
            val lastname = encryptionService.decrypt(encryptedLastname)

            mapOf(
                *row.toMutableMap().apply {
                    this["firstname"] = firstname
                    this["lastname"] = lastname
                }.toList().toTypedArray()
            )
        }

        return mappedResult
    }

    override fun updateUser(username:String, password: String, firstname: String ,lastname: String, id: Int){
        val updateData = "UPDATE users SET credential = ?, firstname = ?, lastname = ? WHERE id = ?"
        jdbcTemplate.update(updateData,encryptionService.hash(username, password), firstname, lastname ,id)
    }

    override fun updateExpenses(expenseName: String, value: Int ,id: Int, oldName:String){
        val updateData = "UPDATE expenses SET expense_name = ?, value = ? WHERE id = ? AND expense_name = ?"
        jdbcTemplate.update(updateData, expenseName, value, id, oldName)
    }

    override fun deleteUser(id: Int){
        val deleteData = "DELETE FROM users WHERE id = ?"
        jdbcTemplate.update(deleteData, id)
        deleteExpenses(id)
    }

    override fun deleteUser(){
        val deleteData = "DELETE FROM users"
        jdbcTemplate.update(deleteData)
    }

    override fun getExpenses(id:Int): List<Map<*, *>>{
        val selected = "SELECT expense_name, value FROM expenses WHERE id = ?"
        val result = jdbcTemplate.queryForList(selected, id)

        return result
    }

    override fun createExpense(expenseName: String, value: Int ,id: Int){
        try {
            val insertDataSQL = """
            INSERT INTO expenses (expense_name, value, id ) VALUES ( ?, ?, ? )
        """.trimIndent()

            jdbcTemplate.update(insertDataSQL, expenseName, value, id)
        }catch (e: Exception) {
            println("Error inserting data: ${e.message}")
        }
    }

    override fun deleteExpenses(id: Int){
        val deleteData = "DELETE FROM expenses WHERE id = ?"
        jdbcTemplate.update(deleteData, id)
    }

    override fun deleteExpense(expenseName: String, id:Int){
        val deleteData = "DELETE FROM expenses WHERE id = ? AND expense_name = ?"
        jdbcTemplate.update(deleteData, id, expenseName)
    }
}