import java.text.SimpleDateFormat
import java.util.*

class Message private constructor(val sender: String, val body: String, val timestamp: Date, val name: String ="", val phone : String="", val email: String="") {
    companion object {
        fun create(sender: String, body: String, name: String="", phone: String="", email: String=""): Message {
            return Message(sender, body, Date(), name, phone, email)
        }

        fun deserialize(msg: ByteArray): Message {
            val msgString = String(msg)
            val (timestamp, sender, name, phone, email ,body) = msgString.split('&', limit = 6)

            return Message(sender, body, Date(timestamp.toLong()), name, phone, email)
        }

        operator fun List<String>.component6(): String {
            if (size > 5) {
                return get(5)
            } else {
                return ""
            }
        }
    }

    fun serialize(): ByteArray {
        return "${timestamp.time}&$sender&${name}&${phone}&${email}&$body\n".toByteArray()
    }

    override fun toString(): String {
        val dateString = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(timestamp)
        return "[$dateString] $sender {Name : $name, Telephone : $phone, email : $email} >>> $body"
    }
}

fun main(args: Array<String>) {
    val msg = Message.create("localhost:4848", "test mesaj")
    println(msg)
    val serialized = msg.serialize()
    val deserialized = Message.deserialize(serialized)
    println(deserialized)
}