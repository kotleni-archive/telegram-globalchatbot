package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message

class HelpCommand: Command() {
    override fun getName(): String {
        return "/help"
    }

    override fun getDescription(): String {
        return "Показывает справку по коммандам."
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        val user = bot.getStorage().getUser(msg.from!!.id)
        var text = "<b>Список комманд:</b>\n\n"

        bot.getCommands().forEach {
            if(it.isNeedOwn() && user.isOwner) {
                text += "${it.getName()} - ${it.getDescription()}\n"
            } else if(!it.isNeedOwn()) {
                text += "${it.getName()} - ${it.getDescription()}\n"
            }
        }

        bot.getBot().sendMessage(msg.from!!.id, text, parseMode = "html")
    }
}