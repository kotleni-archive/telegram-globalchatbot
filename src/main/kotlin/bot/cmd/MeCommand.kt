package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message

class MeCommand: Command() {
    override fun getName(): String {
        return "/me"
    }

    override fun getDescription(): String {
        return "Показывает информацию по ващему профилю."
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        var text = ""

        if(line == null) {
            val user = bot.getStorage().getUser(msg.from!!.id)
            text += "Вы <b>${user.username}</b>\n\n"
            text += "У вас имеется:\n"
            text += " 💵: ${user.balance}\n"
            text += " ⚙️: ${user.gears}\n"
            text += " 🍫: ${user.chocolates}\n"
            text += " ✉️: ${user.messages}\n"
        } else {
            val user = bot.getStorage().getUser(line)

            if(user.id < 0) {
                bot.getBot().sendMessage(msg.from!!.id, "❌ Пользователь не найден!", parseMode = "html")
                return
            }

            text += "Это <b>${user.username}</b>\n\n"
            text += "У него имеется:\n"
            text += " 💵: ${user.balance}\n"
            text += " ⚙️: ${user.gears}\n"
            text += " 🍫: ${user.chocolates}\n"
            text += " ✉️: ${user.messages}\n"
        }

        bot.getBot().sendMessage(msg.from!!.id, text, parseMode = "html")
    }
}