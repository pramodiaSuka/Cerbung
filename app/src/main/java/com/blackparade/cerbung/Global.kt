package com.blackparade.cerbung

import java.time.LocalDate
//import java.util.ArrayList
import java.util.Date
import kotlin.collections.ArrayList

object Global {


    val CERBUNG_ID = "com.blackparade.cerbung.cerbungId"
    val USER_ID = "com.blackparade.cerbung.userId"


    var currentId = 1

    var cerbungUser: ArrayList<User> = ArrayList()
    var notificationActivated:Boolean = true

    var currentUser:User? = null

    val genre = arrayOf(
        Genre(1, "Aksi"),
        Genre(2, "Horror"),
        Genre(3, "Kocak"),
        Genre(4, "Misteri"),
        Genre(5, "Drama"),
        Genre(6, "Romantis")
    )

}