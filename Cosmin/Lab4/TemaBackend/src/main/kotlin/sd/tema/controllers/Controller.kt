package sd.tema.controllers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import sd.tema.data.AuthData
import sd.tema.data.ExpensesData
import sd.tema.data.UserData
import sd.tema.interfaces.AuthInterface
import sd.tema.interfaces.CRUDInterface


@RestController
class Controller {

    @Autowired
    private lateinit var crudService: CRUDInterface
    @Autowired
    private lateinit var authService: AuthInterface

    @PostMapping("/login")
    @ResponseBody
    fun login(@RequestBody authData: AuthData): Map<String, Int?> {
        val id = authService.auth(authData.username, authData.password)
        val response = mapOf("id" to id)
        return response
    }

    @GetMapping("/user")
    @ResponseBody
    fun getData(): List<Map<*, *>>  {
        return crudService.getUser()
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    fun getData(@PathVariable id: Int): List<Map<*, *>>  {
        return crudService.getUser(id)
    }

    @PostMapping("/user")
    fun create(@RequestBody user:UserData){
        crudService.createUser(user.username, user.password, user.firstname, user.lastname)
    }

    @PatchMapping("/user/{id}")
    fun updateUserAndPass(@RequestBody user:UserData, @PathVariable id: Int){
        crudService.updateUser(user.username, user.password, user.firstname, user.lastname ,id)
    }

    @DeleteMapping("/user/{id}")
    fun delete(@PathVariable id:Int){
        crudService.deleteUser(id)
    }

    @DeleteMapping("/user")
    fun delete(){
        crudService.deleteUser()
    }

    @GetMapping("/expenses/{id}")
    @ResponseBody
    fun getExpenses(@PathVariable id:Int): List<Map<*, *>>{
        return crudService.getExpenses(id)
    }

    @PostMapping("expenses/{id}")
    fun createExpense(@PathVariable id: Int, @RequestBody expensesData: ExpensesData) {
        crudService.createExpense(expensesData.expensesName, expensesData.value , id)
    }
    @PatchMapping("expenses/{id}/{name}")
    fun updateExpenses(@PathVariable id: Int, @PathVariable name: String, @RequestBody expensesData: ExpensesData) {
        crudService.updateExpenses(expensesData.expensesName, expensesData.value , id, name)
    }

    @DeleteMapping("expenses/{id}")
    fun deleteExpenses(@PathVariable id: Int){
        crudService.deleteExpenses(id)
    }

    @DeleteMapping("expenses/{id}/{name}")
    fun deleteExpense(@PathVariable id: Int, @PathVariable name: String){
        crudService.deleteExpense(name, id)
    }
}