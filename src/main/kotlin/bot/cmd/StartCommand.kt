package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message

class StartCommand: Command() {
    override fun getName(): String {
        return "/start"
    }

    override fun getDescription(): String {
        return "Отображает сообщение приветствия."
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        bot.getBot().sendMessage(msg.from!!.id, "Что-бы подключиться к чату, отправьте свое первое сообщение. \uD83D\uDE44")
    }
}