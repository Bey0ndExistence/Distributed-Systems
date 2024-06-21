import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Logger{
    fun log(message : String){
        try{
            val file = File("/home/student/Documents/SD_Laborator_07/Okazii/logs/auctioneer.log")
            if (!file.exists()) {
                file.createNewFile()
            }
            val current = LocalDateTime.now()
            file.appendText("[$current]:: $message\n")
        } catch (e : Exception) {
            println("Error $e")
        }
    }
}