package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message

class RateCommand: Command() {
    override fun getName(): String {
        return "/rate"
    }

    override fun getDescription(): String {
        return "Отображает рейтинговый список пользователей."
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        var max = 13
        var text = ""
        text += "🏅 <b>Рейтинг</b>:\n\n"

        if(line != null)
            max = line.toInt()

        var counter = 0
        bot.getStorage().getSortedUsers().forEach {
            if (counter > max + 1)
                return@forEach

            text += "${counter + 1}. ${it.username} - ${it.messages} ✉️, ${it.balance} 💵\n"

            counter += 1
        }

        bot.getBot().sendMessage(msg.from!!.id, text, parseMode = "html")
    }
}