package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message
import java.io.File

class GithubCommand: Command() {
    override fun getName(): String {
        return "/github"
    }

    override fun getDescription(): String {
        return "Отправляет ссылку на Github бота."
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        bot.getBot().sendMessage(msg.from!!.id, "<b>Мой Github:</b>\nhttps://github.com/kotleni/telegram-globalchatbot", parseMode = "html")
    }
}