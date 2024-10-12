package com.blackparade.cerbung

import java.util.Date

//data class Paragraph(val author:String,
//                     val createDate: String,
//                     val likeCount:Int,
//                     val content:String)
data class Paragraph(var id: Int,
                     var content: String,
                     var upload_date: String,
                     var author:String?,
                     var is_liked:Int,
                     var total_likes:Int)
//Class: Paragraph
//1. Author
//2. Date (Now)
//3. LikeCount
//4. Content