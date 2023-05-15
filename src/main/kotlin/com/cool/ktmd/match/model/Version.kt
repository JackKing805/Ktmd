package com.cool.ktmd.match.model

data class Version(
    val version:Int,
    val versionString:String
){
    override fun toString(): String {
        return "versionCode:$version,version:$versionString"
    }
}