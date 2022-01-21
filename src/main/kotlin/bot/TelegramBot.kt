package bot

import MainStorage
import bot.cmd.Command
import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message
import data.CachedMessage
import data.MessageForId
import logd
import startInterval

open class TelegramBot(val token: String, val username: String): BotListener {
    companion object {
        const val CACHE_SIZE_LIMIT = 600
        const val DEFAULT_INTERVAL_DELAY = 2000
    }

    private val bot = Bot.createPolling(username, token)
    private val storage = MainStorage()
    private val startTime = System.currentTimeMillis()
    private val commands: ArrayList<Command> = arrayListOf()
    private val cache: ArrayList<CachedMessage> = arrayListOf()

    open fun start() {
        storage.load()
        bot.start()

        bot.onMessage { onMessage(it) }

        startInterval(DEFAULT_INTERVAL_DELAY) {
            if(getCacheSize() > CACHE_SIZE_LIMIT)
                cache.clear()
        }

        logd("Bot started")
    }

    fun getBot(): Bot {
        return bot
    }

    fun getStorage(): MainStorage {
        return storage
    }

    fun getUptime(): Long {
        return System.currentTimeMillis() - startTime
    }

    fun getCommands(): List<Command> {
        return commands
    }

    fun getCache(): ArrayList<CachedMessage> {
        return cache
    }

    fun getCacheSize(): Int {
        var size = 0

        getCache().forEach {
            it.msgs.forEach {
                size += 1
            }
        }

        return size
    }

    fun setupCommands(vararg cmds: Command) {
        cmds.forEach { cmd ->
            commands.add(cmd)

            bot.onCommand(cmd.getName()) { msg, str ->
                val user = getStorage().getUser(msg.from!!.id)
                logd("(${msg.from!!.id}, ${msg.from!!.username}) ${user.username} -> ${cmd.getName()} $str")

                if(!user.isBanned) {
                    if (cmd.isUserCan(user))
                        cmd.invokeCommand(msg, str, this)
                    else
                        bot.sendMessage(
                            msg.from!!.id,
                            "❌ У вас нет прав на выполнение этой комманды! (${cmd.getPermission().name})"
                        )
                }
            }
        }
    }

    fun broadcastVoice(fileId: String, text: String, ignoreId: Long = -1) {
        getStorage().getUsers().forEach {
            if(it.id != ignoreId && it.isAcitve && !it.isBanned)
                getBot().sendVoice(it.id, fileId, caption = text, parseMode = "html")
        }
    }

    fun broadcastPhoto(fileId: String, text: String, ignoreId: Long = -1) {
        getStorage().getUsers().forEach {
            if(it.id != ignoreId && it.isAcitve && !it.isBanned)
                getBot().sendPhoto(it.id, fileId, caption = text, parseMode = "html")
        }
    }

    fun broadcastDocument(fileId: String, text: String, ignoreId: Long = -1) {
        getStorage().getUsers().forEach {
            if(it.id != ignoreId && it.isAcitve && !it.isBanned)
                getBot().sendDocument(it.id, fileId, caption = text, parseMode = "html")
        }
    }

    fun broadcastSticker(fileId: String, text: String, ignoreId: Long = -1) {
        getStorage().getUsers().forEach {
            if(it.id != ignoreId && it.isAcitve && !it.isBanned) {
                getBot().sendMessage(it.id, text, parseMode = "html").whenCompleteAsync { message, throwable ->
                    getBot().sendSticker(it.id, fileId)
                }
            }
        }
    }

    fun broadcastAudio(fileId: String, text: String, ignoreId: Long = -1) {
        getStorage().getUsers().forEach {
            if(it.id != ignoreId && it.isAcitve && !it.isBanned)
                getBot().sendAudio(it.id, fileId, caption = text, parseMode = "html")
        }
    }

    fun broadcastVideo(fileId: String, text: String, ignoreId: Long = -1) {
        getStorage().getUsers().forEach {
            if(it.id != ignoreId && it.isAcitve && !it.isBanned)
                getBot().sendVideo(it.id, fileId, caption = text, parseMode = "html")
        }
    }

    fun broadcastVideoNote(fileId: String, text: String, ignoreId: Long = -1) {
        getStorage().getUsers().forEach {
            if(it.id != ignoreId && it.isAcitve && !it.isBanned) {
                getBot().sendMessage(it.id, text, parseMode = "html").whenCompleteAsync { message, throwable ->
                    getBot().sendVideoNote(it.id, fileId)
                }
            }
        }
    }

    fun broadcastMessage(text: String, initMessageForId: MessageForId, ignoreId: Long = -1, ) {
        val msgs = ArrayList<MessageForId>()
        msgs.add(initMessageForId)

        getStorage().getUsers().forEach {
            if(it.id != ignoreId && it.isAcitve && !it.isBanned)
                getBot().sendMessage(it.id, text, parseMode = "html").whenCompleteAsync { msg, _ ->
                    msgs.add(MessageForId(it.id, msg))
                }
        }

        getCache().add(CachedMessage(msgs))
    }

    fun broadcastReplyMessage(text: String, ignoreId: Long = -1, cacheId: Int) {
        val msgs = ArrayList<MessageForId>()
        getCache()[cacheId].msgs.forEach {
            val user = getStorage().getUser(it.forId)
            if(user.id != ignoreId && user.isAcitve && !user.isBanned) {
                getBot().sendMessage(user.id, text, parseMode = "html", replyTo = it.message.message_id).whenCompleteAsync { msg, _ ->
                    msgs.add(MessageForId(user.id, msg))
                }
            }
        }

        getCache().add(CachedMessage(msgs))
    }

    fun broadcastAdminMessage(text: String, ignoreId: Long = -1, ) {
        getStorage().getUsers().forEach {
            if(it.id != ignoreId && it.isAcitve && it.isOwner && !it.isBanned)
                getBot().sendMessage(it.id, text, parseMode = "html")
        }
    }

    override fun onMessage(msg: Message) {
        val user = getStorage().getUser(msg.from!!.id)
        logd("(${msg.from!!.id}, ${msg.from!!.username}) ${user.username} -> ${msg.text}")
    }
}