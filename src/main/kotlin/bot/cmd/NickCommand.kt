package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message

class NickCommand: Command() {
    override fun getName(): String {
        return "/nick"
    }

    override fun getDescription(): String {
        return "Установить ваш никнейм на другой."
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        val lets = "йцукенгшщзхфывапролдячсмитьёъqwertyuiopasdfghjklzxcvbnm1234567890_-.!"
        val user = bot.getStorage().getUser(msg.from!!.id)
        val oldUsername = user.username

        if(user.isBanned)
            return

        bot.getStorage().getUsers().forEach {
            if(it.username == line) { // exist
                bot.getBot().sendMessage(msg.from!!.id, "❌ Такой ник уже занят.")
                return
            }
        }

        if(line != null && line.length in 3..16) {
            line!!.forEach {
                if(!lets.toLowerCase().contains(it.toLowerCase())) {
                    bot.getBot().sendMessage(msg.from!!.id, "❌ Ник может содержать только эти символы:\n<i>$lets</i>", parseMode = "html")
                    return
                }
            }

            user.username = line
            bot.getStorage().getUsers().forEach {
                bot.getBot().sendMessage(it.id, "Пользователь <b>${oldUsername}</b> сменил ник на <b>${user.username}</b>", parseMode = "html")
            }
        } else {
            bot.getBot().sendMessage(msg.from!!.id, "❌ Ник должен содержать минимум 4 символа и максимум 15!")
        }
    }
}