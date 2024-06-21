package sd.tema.interfaces

interface EncryptionInterface {
    fun hash(input: String, input2:String):String
    fun encrypt(input: String): String
    fun decrypt(input: String): String
}