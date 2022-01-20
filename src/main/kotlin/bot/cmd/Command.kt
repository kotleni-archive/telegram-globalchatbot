package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message

open class Command {
    open fun getName(): String {
        return ""
    }

    open fun getDescription(): String {
        return ""
    }

    open fun isNeedOwn(): Boolean {
        return false
    }

    open fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {

    }
}


