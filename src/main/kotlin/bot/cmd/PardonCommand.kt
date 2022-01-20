package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message
import data.MessageForId

class PardonCommand: Command() {
    override fun getName(): String {
        return "/pardon"
    }

    override fun getDescription(): String {
        return "Разблокировать пользователя в чате."
    }

    override fun isNeedOwn(): Boolean {
        return true
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        bot.getStorage().getUsers().forEach {
            if(it.username == line) { // finded
                it.isBanned = false
                bot.broadcastMessage("☑️ Пользователь <b>${it.username}</b> разблокирован!", MessageForId(msg.from!!.id, msg))

                return
            }
        }

        bot.getBot().sendMessage(msg.from!!.id, "❌ Пользователь не найден!")
    }
}