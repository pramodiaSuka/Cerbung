package com.blackparade.cerbung

data class Notification(var id:Int = 0,
                        var users_id_sender:Int = 0,
                        var users_id_receiver:Int = 0,
                        var type:String = "",
                        var news_cerbung_id:Int?,
                        var request_cerbung_id:Int?,
                        var sent_date:String,
                        var sender_username:String)
