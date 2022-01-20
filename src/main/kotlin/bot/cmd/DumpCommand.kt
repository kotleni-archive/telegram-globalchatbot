package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message

class DumpCommand: Command() {
    override fun getName(): String {
        return "/dump"
    }

    override fun getDescription(): String {
        return "Отправляет все данные бота, файлом."
    }

    override fun isNeedOwn(): Boolean {
        return true
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        bot.getBot().sendDocument(msg.from!!.id, bot.getStorage().getFile(), caption = "Storage file")
    }
}