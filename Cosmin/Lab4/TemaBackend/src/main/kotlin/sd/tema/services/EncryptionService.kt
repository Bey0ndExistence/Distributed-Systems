package sd.tema.services

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.stereotype.Service
import sd.tema.interfaces.EncryptionInterface
import java.security.MessageDigest
import java.util.*
@Service
class EncryptionService: EncryptionInterface {
    private val textEncryptor = Encryptors.text("15ABCD", "C112")

    override fun hash(input: String, input2:String): String {
        val bytes = (input + input2).toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return Base64.getEncoder().encodeToString(digest)
    }

    override fun encrypt(input: String): String {
        return textEncryptor.encrypt(input)
    }

    override fun decrypt(input: String): String {
        return textEncryptor.decrypt(input)
    }
}