package com.blackparade.cerbung

class User(var id: Int = 0,
           var username:String = "",
           var password:String = "",
           var latest_post: String? = "",
           var total_likes: Int = 0,
           var old_password: String = "",
           var image_url:String = "",
           var date_joined: String = "") {
}