import java.util.Date

class Post : PostId() {
    var userId: String = ""
    var desc: String = ""
    var timestamp: Long? = null

    fun getTimestampAsLong(): Long? {
        return timestamp
    }
}



