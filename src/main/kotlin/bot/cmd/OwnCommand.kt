package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message
import data.MessageForId

class OwnCommand: Command() {
    override fun getName(): String {
        return "/own"
    }

    override fun getDescription(): String {
        return "Добавляет пользователя в список администраторов."
    }

    override fun isNeedOwn(): Boolean {
        return true
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        bot.getStorage().getUsers().forEach {
            if(it.username == line) { // finded
                it.isOwner = true
                bot.broadcastMessage("☑️ Пользователь <b>${it.username}</b> стал админом!", MessageForId(msg.from!!.id, msg))

                return
            }
        }

        bot.getBot().sendMessage(msg.from!!.id, "❌ Пользователь не найден!")
    }
}