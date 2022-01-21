package bot

object Config {
    fun getBotToken(): String {
        return System.getenv("BOT_TOKEN")
    }

    fun getBotUsername(): String {
        return "globalchat2022_bot"
    }
}