package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message
import com.sun.management.OperatingSystemMXBean
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.management.ManagementFactory
import java.net.URL

class DebugCommand: Command() {
    override fun getName(): String {
        return "/debug"
    }

    override fun getDescription(): String {
        return "Выводит отдалочную информацию."
    }

    override fun isNeedOwn(): Boolean {
        return true
    }

    override fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {
        val osBean: OperatingSystemMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean::class.java)

        val freeMem = Runtime.getRuntime().freeMemory()
        val totalMem = Runtime.getRuntime().totalMemory()
        val loadCpu = osBean.systemLoadAverage.toString().substring(0, 4)
        val freeDisk = File("/").freeSpace
        val totalDisk = File("/").totalSpace
        val osName = osBean.name
        val osVer = osBean.version

        bot.getBot().sendMessage(msg.from!!.id,
            "<b>\uD83D\uDEE0 Debug:</b>\n\n" +
                    "<b>Uptime:</b> ${bot.getUptime() / 1000} secs.\n" +
                    "<b>Memory:</b> ${(totalMem - freeMem) / 1024 / 1024} MB / ${totalMem / 1024 / 1024} MB\n" +
                    "<b>Disk:</b> ${(totalDisk - freeDisk) / 1024 / 1024 / 1024} GB / ${totalDisk / 1024 / 1024 / 1024} GB\n" +
                    "<b>CPU:</b> ${loadCpu} %\n" +
                    "<b>Cached messages:</b> ${bot.getCacheSize()} / ${TelegramBot.CACHE_SIZE_LIMIT}\n" +
                    "<b>Server:</b> $osName $osVer \n" ,
            parseMode = "html"
        )
    }
}