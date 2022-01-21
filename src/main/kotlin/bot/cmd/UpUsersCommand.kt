package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message

class UpUsersCommand: Command() {
    override fun getName(): String {
        return "/up_users"
    }

    override fun getDescription(): String {
        return ""
    }

    override fun getPermission(): Permission {
        return Permission.GOD
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        bot.getStorage().getUsers().forEach {
            it.lastEvent = System.currentTimeMillis()
            it.isAcitve = true
        }

        bot.getBot().sendMessage(msg.from!!.id, "❌ Все пользователи были активированы!")
    }
}