package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message

class DeactivateCommand: Command() {
    override fun getName(): String {
        return "/deactivate"
    }

    override fun getDescription(): String {
        return "Деактивировать пользователя в чате."
    }

    override fun isNeedOwn(): Boolean {
        return true
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        bot.getStorage().getUsers().forEach {
            if(it.username == line) { // finded
                it.lastEvent = 0
                bot.getBot().sendMessage(msg.from!!.id , "☑️ Пользователь <b>${it.username}</b> будет деактивирован в ближайшее время.", parseMode = "html")

                return
            }
        }

        bot.getBot().sendMessage(msg.from!!.id, "❌ Пользователь не найден!")
    }
}