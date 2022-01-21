package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message

class HackCommand: Command() {
    override fun getName(): String {
        return "/hack"
    }

    override fun getDescription(): String {
        return "Взломать систему."
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        val user = bot.getStorage().getUser(msg.from!!.id)

        bot.getBot().sendMessage(msg.from!!.id, "\uD83D\uDE21 Взламываем пентагон...")
        bot.getBot().sendMessage(msg.from!!.id, "Найдено 2022 уязвимости!")
        bot.getBot().sendMessage(msg.from!!.id, "Попытка подключения к серверу 1...")
        bot.getBot().sendMessage(msg.from!!.id, "О нет! Нас накрыли! Беги...")

        user.lastEvent = 0
    }
}