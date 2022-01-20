package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message
import java.io.File

class SourcesCommand: Command() {
    override fun getName(): String {
        return "/sources"
    }

    override fun getDescription(): String {
        return "Отправляет исходники бота вместе с кешем, файлом."
    }

    override fun isNeedOwn(): Boolean {
        return true
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        Runtime.getRuntime().exec("zip -r /tmp/sources.zip ./").waitFor()
        bot.getBot().sendDocument(msg.from!!.id, File("/tmp/sources.zip"), caption = "Bot sources")
    }
}