import bot.Config
import bot.TelegramBot
import bot.cmd.*
import com.elbekD.bot.types.Message
import data.CachedMessage
import data.MessageForId
import data.MyUser
import helper.NicknameHelper


class SelfBot(): TelegramBot(Config.getBotToken(), Config.getBotUsername()) {
    override fun start() {
        super.start()

        // thread exceptions handler
        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            logd(paramThrowable.stackTraceToString())
            broadcastAdminMessage("<code>${paramThrowable.stackTraceToString()}</code>")
        }

        // saving timer
        startInterval(DEFAULT_INTERVAL_DELAY) {
            updateUsersActive()
            getStorage().save()
        }

        // setup commands
        setupCommands(
            JojoCommand(), StartCommand(), LogsCommand(),
            GithubCommand(), DumpCommand(), HelpCommand(),
            MeCommand(), SaveCommand(), NickCommand(),
            DebugCommand(), BroadcastCommand(), ListCommand(),
            DeactivateCommand(), RateCommand(), OwnCommand(),
            DeownCommand() ,BanCommand(), PardonCommand(),
            EvalCommand(), DiceCommand()
        )

    }

    override fun onMessage(msg: Message) {
        super.onMessage(msg)

        // if need register new user
        if(!getStorage().isUserExist(msg.from!!.id)) {
            val username = if(msg.from!!.username == null) NicknameHelper.getRandom() else msg.from!!.username!!

            getStorage().addUser(MyUser(
                id = msg.from!!.id,
                username = username,
                messages = 0)
            )

            getStorage().save()

            getBot().sendMessage(msg.from!!.id, "Добро пожаловать в глобальный чат, <b>${username}</b>! Что-бы сменить ваш ник, отправьте в чат комманду: /nick <i>НовыйНик</i>", parseMode = "html")

            getStorage().getUsers().forEach {
                if(msg.from!!.id != it.id)
                    getBot().sendMessage(it.id, "У нас новый пользователь: <b>${username}</b>", parseMode = "html")
            }
        }

        val user = getStorage().getUser(msg.from!!.id)
        val text = msg.text
        val caption = if(msg.caption == null) "" else msg.caption!!

        user.lastEvent = System.currentTimeMillis()
        if(!user.isAcitve) {
            user.isAcitve = true
            getBot().sendMessage(user.id, "\uD83D\uDE34 <b>Вы снова активны!</b>. Теперь вам будут приходить сообщения с чата.", parseMode = "html")
        }

        if(user.isBanned && !user.isOwner)
            return@onMessage

        user.messages += 1 // increase sended messages

        if(msg.video_note != null) {
            broadcastVideoNote(msg.video_note!!.file_id, "<b>${user.username}</b>:", msg.from!!.id)
        } else if (msg.photo != null) {
            broadcastPhoto(msg.photo!![0].file_id, "<b>${user.username}</b>: $caption", msg.from!!.id)
        } else if (msg.sticker != null) {
            broadcastSticker(msg.sticker!!.file_id, "<b>${user.username}</b>:", msg.from!!.id)
        } else if(msg.audio != null) {
            broadcastAudio(msg.audio!!.file_id, "<b>${user.username}</b>: $caption", msg.from!!.id)
        } else if(msg.video != null) {
            broadcastVideo(msg.video!!.file_id, "<b>${user.username}</b>: $caption", msg.from!!.id)
        } else if(msg.document != null) {
            broadcastDocument(msg.document!!.file_id, "<b>${user.username}</b>: $caption", msg.from!!.id)
        } else if(msg.voice != null) {
            broadcastVoice(msg.voice!!.file_id, "<b>${user.username}</b>:", msg.from!!.id)
        } else {
            var ltext = text
            if(msg.text == null) ltext = "*не поддерживаемый контент*"
            if(msg.dice != null) ltext = msg.dice!!.emoji

            if(msg.reply_to_message == null) {
                broadcastMessage( "<b>${user.username}</b>: ${ltext}", MessageForId(user.id, msg), msg.from!!.id)
            } else {
                var i = 0
                (getCache().clone() as ArrayList<CachedMessage>).forEach {
                    it.msgs.forEach {
                        if(it.message.message_id == msg.reply_to_message!!.message_id) { // reply finded
                            broadcastReplyMessage("<b>${user.username}</b>: ${ltext}", msg.from!!.id, i)
                            return
                        }
                    }

                    i += 1
                }

                broadcastMessage( "<b>${user.username}</b>: ${ltext}", MessageForId(user.id, msg), msg.from!!.id)
            }
        }
    }

    fun updateUsersActive() {
        getStorage().getUsers().forEach {
            if(it.isAcitve) {
                val secs = (System.currentTimeMillis() - it.lastEvent) / 1000
                val hours = (secs / 60) / 60

                if(hours >= 3) {
                    it.isAcitve = false
                    getBot().sendMessage(it.id, "\uD83D\uDE34 <b>Вы были деактивированы</b>. Теперь вы не будете получать сообещения с чата. Что-бы вернуться в чат, отправьте любое сообщение.", parseMode = "html")
                }
            }
        }
    }
}