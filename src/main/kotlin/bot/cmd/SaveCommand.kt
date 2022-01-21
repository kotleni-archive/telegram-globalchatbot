package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message

class SaveCommand: Command() {
    override fun getName(): String {
        return "/save"
    }

    override fun getDescription(): String {
        return "Сохранить данные бота."
    }

    override fun getPermission(): Permission {
        return Permission.GOD
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        val user = bot.getStorage().getUser(msg.from!!.id)

        if(user.isOwner) {
            bot.getStorage().save()
            bot.getBot().sendMessage(msg.from!!.id, "Запрос на сохранение отправлен.", parseMode = "html")
        }
    }
}