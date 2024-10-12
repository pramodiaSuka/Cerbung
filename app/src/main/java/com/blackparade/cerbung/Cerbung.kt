package com.blackparade.cerbung

import java.util.Date

data class Cerbung(var id:Int = 0,
                   var title:String = "",
                   var description:String = "",
                   var total_likes:Int = 0,
                   var total_paragraph:Int = 0,
                   var last_updated: String = "",
                   var edit_access:String = "",
                   var cover_image:String = "",
                   var genre:String = "",
                   var author:String = "",
                   var is_liked_by_user:Int = 0,
                   var is_followed_by_user:Int = 0,
                   var author_id:Int = 0,
                   var can_be_edited:Int = 0
                   )
