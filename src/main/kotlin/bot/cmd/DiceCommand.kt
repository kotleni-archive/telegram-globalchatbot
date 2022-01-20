package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message
import getRandomSeed
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.random.nextInt

class DiceCommand: Command() {
    private val random = Random(getRandomSeed())

    override fun getName(): String {
        return "/dice"
    }

    override fun getDescription(): String {
        return "Лотерея, выпадает случайный приз. (стоит 500 ✉️)"
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        val user = bot.getStorage().getUser(msg.from!!.id)

        if(user.messages < 500) {
            bot.getBot().sendMessage(msg.from!!.id, "❌ У вас недостаточно ✉️.")
            return
        }

        user.messages -= 500

        bot.getBot().sendMessage(msg.from!!.id, "🎲 Барабан крутится...")
        thread {
            Thread.sleep(random.nextLong(1000, 2500))

            var win = ""

            val count = random.nextInt(0, 3)
            when(random.nextInt(0, 5)) {
                0 -> { win = "${count} 🍫"; user.chocolates += count }
                1 -> { win = "${count} ⚙️"; user.gears += count }
                2 -> { win = "${count} 💵"; user.balance += count }
                4 -> { win = "ничего" }
            }

            bot.getBot().sendMessage(msg.from!!.id, "🎲 Вы выиграли: $win")
        }
    }
}