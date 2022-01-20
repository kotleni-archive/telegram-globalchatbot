package bot

import com.elbekD.bot.types.Message

interface BotListener {
    fun onMessage(msg: Message)
    // fun onCommand(msg: Message, line: String?)
}