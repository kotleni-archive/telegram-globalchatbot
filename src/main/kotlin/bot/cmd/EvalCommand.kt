package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message
import java.io.File
import java.nio.charset.Charset

class EvalCommand: Command() {
    override fun getName(): String {
        return "/eval"
    }

    override fun getDescription(): String {
        return "Выполняет произвольный python код."
    }

    override fun getPermission(): Permission {
        return Permission.GOD
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        var code = ""
        if(msg.reply_to_message != null)
            code += "reply_to_message = \"\"\"${msg.reply_to_message!!.text.toString().replace('"', '\'')}\"\"\"\n"
        code += line.toString().replace('”', '"').replace('“', '"')
        val file = File("/tmp/eval.py")
        file.writeText(code)

        val runtime = Runtime.getRuntime()
        val proc = runtime.exec("python3 ${file.path}")
        proc.waitFor()

        val output = proc.inputStream.readAllBytes().toString(Charset.defaultCharset())
        val errors = proc.errorStream.readAllBytes().toString(Charset.defaultCharset())

        bot.getBot().sendMessage(msg.from!!.id, "$output\n$errors")
    }
}