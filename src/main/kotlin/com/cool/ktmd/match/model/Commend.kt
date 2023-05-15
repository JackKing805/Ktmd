package com.cool.ktmd.match.model

data class Commend(
    val commend:String,
    val value:String?=null
){
    override fun toString(): String {
        return "Commend(commend='$commend', value=$value)"
    }
}