package com.cool.ktmd.match.model

data class MatchResult(
    val header: String,
    val commends: Array<Commend>,
    val unknownCommends:Array<Commend>,
    val errorStr:String,
    private val defineCommend:Array<FormatterCommend>
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

    fun exceptionInfo():ExceptionResult{
        if (unknownCommends.isEmpty() && errorStr.isEmpty()){
            return ExceptionResult.NoException()
        }
        val builder = StringBuilder("")
        for ((index,unknownCommend) in unknownCommends.withIndex()) {
            builder.append("commend:${unknownCommend.commend} not define"+if (index==unknownCommends.size-1) "" else "\n")
        }

        if (errorStr.isNotEmpty()){
            val split = errorStr.split(" ")
            if (split.isEmpty()){
                builder.append("and str:$errorStr is not define in CommendLines")
            }else{
                for ((index,it) in split.withIndex()) {
                    val start = if (builder.toString().isEmpty()) "" else if (index == 0) "and " else ""
                    val end = if (index==split.size-1) "" else "\n"
                    val isDefine = defineCommend.find { d->d.commend == it } !=null
                    builder.append(start)
                    if (isDefine){
                        builder.append("Whether you forget append '-' before commend:$it")
                    }else{
                        builder.append("str:$it is not define in CommendLines")
                    }
                    builder.append(end)
                }
            }
        }
        return ExceptionResult.HaveException(builder.toString())
    }
}