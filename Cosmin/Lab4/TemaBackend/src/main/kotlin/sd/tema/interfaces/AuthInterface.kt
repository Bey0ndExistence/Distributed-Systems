package sd.tema.interfaces


interface AuthInterface {
    fun auth(username: String, password: String):Int?
}