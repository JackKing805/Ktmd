package com.cool.ktmd.match.model

data class MatchResult(
    val header: String,
    val commends: Array<Commend>,
    val unknownCommends:Array<Commend>,
    val errorStr:String
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MatchResult

        if (header != other.header) return false
        if (!commends.contentEquals(other.commends)) return false
        if (!unknownCommends.contentEquals(other.unknownCommends)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = header.hashCode()
        result = 31 * result + commends.contentHashCode()
        result = 31 * result + unknownCommends.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "MatchResult(header='$header', commends=${commends.contentToString()}, unknownCommends=${unknownCommends.contentToString()}, errorStr='$errorStr')"
    }

    fun findCommend(commend:String): Commend?{
        return commends.find { it.commend==commend }
    }
}