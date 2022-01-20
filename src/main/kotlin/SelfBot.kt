import bot.TelegramBot
import bot.cmd.*
import com.elbekD.bot.types.Message
import data.CachedMessage
import data.MessageForId
import data.MyUser
import helper.FilterHelper


class SelfBot(): TelegramBot("5006938722:AAF0cVI8HrE9HjaNbnR7uEfmiEseaDxc0Ss", "globalchat2022_bot") {
    override fun start() {
        super.start()

        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            logd(paramThrowable.stackTraceToString())
            broadcastAdminMessage("<code>${paramThrowable.stackTraceToString()}</code>")
        }

        startInterval(DEFAULT_INTERVAL_DELAY) {
            updateUsersActive()
            getStorage().save()
        }

        setupCommands(
            JojoCommand(), StartCommand(), LogsCommand(),
            SourcesCommand(), DumpCommand(), HelpCommand(),
            MeCommand(), SaveCommand(), NickCommand(),
            DebugCommand(), BroadcastCommand(), ListCommand(),
            DeactivateCommand(), RateCommand(), OwnCommand(),
            DeownCommand() ,BanCommand(), PardonCommand(),
            EvalCommand(), DiceCommand()
        )

    }

    override fun onMessage(msg: Message) {
        super.onMessage(msg)

        if(!getStorage().isUserExist(msg.from!!.id)) {
            val username = if(msg.from!!.username == null) "user${getRandomSeed()}" else msg.from!!.username!!

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
        var text = msg.text

        user.lastEvent = System.currentTimeMillis()
        if(!user.isAcitve) {
            user.isAcitve = true
            getBot().sendMessage(user.id, "\uD83D\uDE34 <b>Вы снова активны!</b>. Теперь вам будут приходить сообщения с чата.", parseMode = "html")
        }

        if(user.isBanned)
            return@onMessage

        user.messages += 1 // increase sended messages

        // filter
        if(text != null) {
            text = FilterHelper.filterString(text)
            // text = FilterUtils.filterUrls(text)
        }

        if(msg.video_note != null) {
            broadcastVideoNote(msg.video_note!!.file_id, "<b>${user.username}</b>:", msg.from!!.id)
        } else if (msg.photo != null) {
            broadcastPhoto(msg.photo!![0].file_id, "<b>${user.username}</b>:", msg.from!!.id)
        } else if (msg.sticker != null) {
            broadcastSticker(msg.sticker!!.file_id, "<b>${user.username}</b>:", msg.from!!.id)
        } else if(msg.audio != null) {
            broadcastAudio(msg.audio!!.file_id, "<b>${user.username}</b>:", msg.from!!.id)
        } else if(msg.video != null) {
            broadcastVideo(msg.video!!.file_id, "<b>${user.username}</b>:", msg.from!!.id)
        } else if(msg.document != null) {
            broadcastDocument(msg.document!!.file_id, "<b>${user.username}</b>:", msg.from!!.id)
        } else if(msg.voice != null) {
            broadcastVoice(msg.voice!!.file_id, "<b>${user.username}</b>:", msg.from!!.id)
        } else {
            val ltext = if (msg.text == null) "*не поддерживаемый контент*" else text

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