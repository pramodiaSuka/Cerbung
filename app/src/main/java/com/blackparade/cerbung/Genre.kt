package com.blackparade.cerbung

data class Genre(val id:Int, val name:String){
    override fun toString(): String {
        return name
    }
}
