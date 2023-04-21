package com.example.healingwords

import java.util.Date

class Post (
    var userId: String = "",
    var desc: String = "",
    var timestamp: Long? = null
) {
    fun getTimestampAsLong(): Long? {
        return timestamp
    }
}


