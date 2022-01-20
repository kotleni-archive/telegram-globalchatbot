package helper

object FilterHelper {
    private val items = listOf(
        "вагина", "член", "пизда", "хуй", "негр", "бляд", "питон", "конченый", "еблан", "хуи", "ебан",
        "дрочи", "анал", "анус", "ебал", "сука", "блят"
    )

    fun filterString(line: String): String {
        var out = line

        items.forEach {
            var string = it
            "eyuioaуеыаояию".forEach {
                string = string.replace(it, '*')
            }

            out = out.replace(it, string)
        }

        return out
    }

    fun filterUrls(line: String): String {
        listOf("http://", "https://", "t.me", "vk.com", "google.com", "github.com").forEach {
            if(line.contains(it))
                return "*сообщение заблокировано*"
        }

        return line
    }
}