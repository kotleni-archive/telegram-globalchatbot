package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message
import data.MessageForId

class DeownCommand: Command() {
    override fun getName(): String {
        return "/deown"
    }

    override fun getDescription(): String {
        return "Удаляет пользователя из списка администраторов."
    }

    override fun getPermission(): Permission {
        return Permission.OWNER
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        bot.getStorage().getUsers().forEach {
            if(it.username == line) { // finded
                it.isOwner = false
                bot.broadcastMessage("☑️ Пользователь <b>${it.username}</b> теперь не админ!", MessageForId(msg.from!!.id, msg))

                return
            }
        }

        bot.getBot().sendMessage(msg.from!!.id, "❌ Пользователь не найден!")
    }
}