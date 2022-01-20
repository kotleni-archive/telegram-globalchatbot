import com.google.gson.Gson
import data.MyData
import data.MyUser
import java.io.File

class MainStorage {
    private val gson = Gson()
    private var myData: MyData? = null

    fun getFile(): File {
        return File("./storage.json")
    }

    fun load() {
        val file = getFile()
        val text = file.readText()
        myData = gson.fromJson(text, MyData::class.java)

        logd("Storage loaded")
    }

    fun save() {
        val file = getFile()
        val json = gson.toJson(myData)
        file.writeText(json)

        // logd("Storage saved")
    }

    fun getUsers(): List<MyUser> {
        return myData!!.users!!
    }

    fun getSortedUsers(): List<MyUser> {
        val users = myData!!.users.clone() as ArrayList<MyUser>

        for(i in 0 until users.size) {
            for(ii in 0 until users.size - 1) {
                val a = users[ii]
                val b = users[ii + 1]

                if(b.messages > a.messages) {
                    users[ii] = b.copy()
                    users[ii + 1] = a.copy()
                }
            }
        }

        return users
    }

    fun getUser(id: Long): MyUser {
        getUsers().forEach {
            if(it.id == id)
                return it
        }
        return MyUser(id = -9)
    }

    fun getUser(username: String): MyUser {
        getUsers().forEach {
            if(it.username == username)
                return it
        }
        return MyUser(id = -1)
    }

    fun addUser(user: MyUser) {
        myData!!.users.add(user)

        logd("New user added: ${user.username} by ${user.id}")
    }

    fun isUserExist(id: Long): Boolean {
        myData!!.users.forEach {
            if(id == it.id)
                return true
        }

        return false
    }
}