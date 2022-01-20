package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message
import data.MessageForId

class BroadcastCommand: Command() {
    override fun getName(): String {
        return "/broadcast"
    }

    override fun getDescription(): String {
        return "Выводит оповщение от имени бота в чат."
    }

    override fun isNeedOwn(): Boolean {
        return true
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        bot.broadcastMessage("<b>[Оповещение]</b> $line", MessageForId(msg.from!!.id, msg))
    }
}