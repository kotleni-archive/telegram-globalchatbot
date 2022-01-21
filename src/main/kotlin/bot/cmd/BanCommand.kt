package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message
import data.MessageForId

class BanCommand: Command() {
    override fun getName(): String {
        return "/ban"
    }

    override fun getDescription(): String {
        return "Блокирует пользователя в чате."
    }

    override fun getPermission(): Permission {
        return Permission.OWNER
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        bot.getStorage().getUsers().forEach {
            if(it.username == line) { // finded
                it.isBanned = true
                bot.broadcastMessage("☑️ Пользователь <b>${it.username}</b> заблокирован!", MessageForId(msg.from!!.id, msg))

                return
            }
        }

        bot.getBot().sendMessage(msg.from!!.id, "❌ Пользователь не найден!")
    }
}