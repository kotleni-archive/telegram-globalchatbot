package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message

class ListCommand: Command() {
    override fun getName(): String {
        return "/list"
    }

    override fun getDescription(): String {
        return "Отображает список всех пользователей чата."
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        var text = "✔️ Активные: \n"

        var counter = 0
        bot.getStorage().getUsers().forEach {
            if(it.isAcitve) {
                if (counter == 0)
                    text += "${it.username}"
                else
                    text += ", ${it.username}"

                counter += 1
            }
        }

        text += "\n\n✖️ Не активные: \n"

        counter = 0
        bot.getStorage().getUsers().forEach {
            if(!it.isAcitve) {
                if (counter == 0)
                    text += "${it.username}"
                else
                    text += ", ${it.username}"

                counter += 1
            }
        }

        text += "\n\n<b>Всего</b>: ${bot.getStorage().getUsers().size - counter} / ${bot.getStorage().getUsers().size}"

        bot.getBot().sendMessage(msg.from!!.id, text, parseMode = "html")
    }
}