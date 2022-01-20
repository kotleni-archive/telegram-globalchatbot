package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message

class JojoCommand: Command() {
    override fun getName(): String {
        return "/jojo"
    }

    override fun getDescription(): String {
        return "О? Омаэву, шин-де-рю!."
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        val user = bot.getStorage().getUser(msg.from!!.id)

        bot.getBot().sendMessage(msg.from!!.id, "\uD83D\uDE21 Джотару...")
        bot.getBot().sendMessage(msg.from!!.id, "Что ты сделал со мной?")
        bot.getBot().sendMessage(msg.from!!.id, "Ты лишил меня девственности!")
        bot.getBot().sendMessage(msg.from!!.id, "Грязный ублюдок...")

        user.lastEvent = 0
    }
}