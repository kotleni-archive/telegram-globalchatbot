package bot.cmd

import bot.TelegramBot
import com.elbekD.bot.types.Message
import data.MyUser

enum class Permission {
    USER, OWNER, GOD
}

open class Command {
    open fun getName(): String {
        return ""
    }

    open fun getDescription(): String {
        return ""
    }

    open fun getPermission(): Permission {
        return Permission.USER
    }

    open fun invokeCommand(msg: Message, line: String?, bot: TelegramBot) {

    }

    fun isUserCan(user: MyUser): Boolean {
        var can = false

        if(getPermission() == Permission.OWNER && user.isOwner)
            can = true
        else if(getPermission() == Permission.GOD && user.isGod)
            can = true
        else if(getPermission() == Permission.USER)
            can = true

        return can
    }
}
