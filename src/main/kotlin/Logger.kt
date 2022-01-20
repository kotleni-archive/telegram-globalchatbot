import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Logger {
    fun getFile(): File {
        val date = SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().time);
        return File("./logs/bot_$date.log")
    }

    fun logd(text: String) {
        val line = text

        println(line)
        getFile().appendText("$text\n")
    }
}