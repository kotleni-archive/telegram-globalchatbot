package data

data class MyUser(
    val id: Long = -1,
    var username: String = "",
    var messages: Int = 0, // ✉️
    var balance: Int = 0, // 💵
    var chocolates: Int = 0, // ️🍫
    var gears: Int = 0, // ⚙
    var lastEvent: Long = System.currentTimeMillis(),
    var isBanned: Boolean = false,
    var isOwner: Boolean = false,
    var isAcitve: Boolean = true,
)
