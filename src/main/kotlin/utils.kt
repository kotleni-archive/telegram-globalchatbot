import java.nio.charset.Charset
import kotlin.concurrent.thread

fun logd(text: String) {
    Logger.logd(text)
}

fun startInterval(delay: Int, l: () -> Unit) = thread {
    while (true) {
        Thread.sleep(delay.toLong())
        l.invoke()
    }
}

fun getRandomSeed(): Int {
    return Math.round(Math.random() * 9990).toInt()
}

//fun showMacosNotification(title: String, message: String) {
//    val proc = Runtime.getRuntime().exec("alerter -message \"$message\" -title \"$title\"")
//}