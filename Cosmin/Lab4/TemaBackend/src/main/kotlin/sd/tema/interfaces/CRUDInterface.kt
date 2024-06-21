package sd.tema.interfaces


interface CRUDInterface {
    fun createUser(username:String, password: String, firstname:String, lastname:String )
    fun getUser(): List<Map<*, *>>
    fun getUser(id: Int): List<Map<*, *>>
    fun updateUser(username:String, password: String, firstname: String ,lastname: String, id: Int)
    fun deleteUser(id: Int)
    fun deleteUser()
    fun updateExpenses(expenseName: String, value: Int ,id: Int, oldName:String)
    fun getExpenses(id:Int): List<Map<*, *>>
    fun createExpense(expenseName: String, value: Int ,id: Int)
    fun deleteExpenses(id: Int)
    fun deleteExpense(expenseName: String, id:Int)
}