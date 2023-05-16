package com.cool.ktmd.match

import com.cool.ktmd.match.exceptions.CommendNoSpaceException
import com.cool.ktmd.match.exceptions.ErrorHeaderException
import com.cool.ktmd.match.exceptions.HeaderAndCommendNoSpaceException
import com.cool.ktmd.match.exceptions.ValueAndCommendNoSpaceException
import com.cool.ktmd.match.interfaces.MatchListener
import com.cool.ktmd.match.model.Commend
import com.cool.ktmd.match.model.FormatterCommend
import com.cool.ktmd.match.model.MatchResult
import com.cool.ktmd.match.model.Version
import java.util.regex.Pattern

class KmdMatcher(
    private val header:String,
    private val commends:Array<FormatterCommend>,
    private val version: Version = Version(1,"1.0")
) {
    private var onMatch:MatchListener?=null

    //匹配header：(^header)
    //匹配commed:(-$commed +\w*)
    //命令行：(^[a-z|A-Z]*)[ ]+(-[a-z|A-Z][ ]+\w*)
    private val headerRegex = Pattern.compile("(^$header) ?")
    private val headerErrorRegex = Pattern.compile("(^$header)\\S+")
//    private val commendRegex = commends.map {
//        Patterns(
//            Pattern.compile("(-${it.commend}) +([^-].*[^- ])"),
//            Pattern.compile("(-${it.commend}) ?")//(-version) +(.*)
//        )
//    }

    //(-\w*) ? -version
    //(-\w*) +([^-].*[^- ]) -open asd
    //todo:优化规则1:-(\\w*)\\s+([^\\s-]+)
    private val keyValueRegex = Pattern.compile("-(\\w*)\\s+([^\\s-]+)")
    private val keyRegex = Pattern.compile("-(\\w+) ?")


    private val cErrorRegex1 = Pattern.compile("-(\\w+)-(\\w+)")//-b-c
    private val cErrorRegex2 = Pattern.compile(" (\\w+)-(\\w+)")//n-b


    private fun verify(commend: String){
        val matcher1 = headerErrorRegex.matcher(commend)
        if (matcher1.find()){
            throw HeaderAndCommendNoSpaceException()
        }

        val headerMatcher = headerRegex.matcher(commend)
        if (!headerMatcher.find()){
            throw ErrorHeaderException(header,commend)
        }

        val matcher = cErrorRegex1.matcher(commend)
        while (matcher.find()){
            throw CommendNoSpaceException(matcher.group(1).replace("-",""),matcher.group(2).replace("-",""))
        }

        val matcher2 = cErrorRegex2.matcher(commend)
        while (matcher2.find()){
            throw ValueAndCommendNoSpaceException(matcher2.group(1),matcher2.group(2).replace("-",""))
        }
    }

    fun match(commend:String): MatchResult {
        verify(commend)
        var removeHeaderCommend = commend.replace(header,"").trim()

        val countCommends = mutableListOf<Commend>()
        val unknownCommend = mutableListOf<Commend>()

        val matcher1 = keyValueRegex.matcher(removeHeaderCommend)
        while (matcher1.find()){
            val all = matcher1.group(0)
            val prefix = matcher1.group(1).replace("-","").trim()
            val suffix = matcher1.group(2).trim()
            if (prefix.isNotEmpty() && suffix.isNotEmpty()){
                if (commends.find { it.commend == prefix }==null) {
                    unknownCommend.add(Commend(prefix,suffix))
                }else{
                    countCommends.add(Commend(prefix,suffix))
                }
                removeHeaderCommend = removeHeaderCommend.replace(all,"")
            }
        }

        val matcher2 = keyRegex.matcher(removeHeaderCommend)
        while (matcher2.find()){
            val all = matcher2.group(0)
            val prefix = matcher2.group(1).replace("-","").trim()
            if (prefix.isNotEmpty()){
                if (commends.find { it.commend == prefix }==null){
                    unknownCommend.add(Commend(prefix,null))
                }else{
                    countCommends.add(Commend(prefix,null))
                }
                removeHeaderCommend = removeHeaderCommend.replace(all,"")
            }
        }

        countCommends.sortBy{
            commend.indexOf("-${it.commend}")
        }

        unknownCommend.sortBy {
            commend.indexOf("-${it.commend}")
        }

        return MatchResult(header,countCommends.toTypedArray(), unknownCommend.toTypedArray(),removeHeaderCommend,commends)
    }

    fun input(commend: String){
        try {
            val match = match(commend)
            onMatch?.onMatch(match)
        }catch (e:Exception){
            onMatch?.onException(e)
        }
    }

    fun listenMatch(onMatch:MatchListener){
        this.onMatch = onMatch
    }

    fun version() = version

    fun commends() = commends

    fun info():String{
        val builder = StringBuilder("")
        for ((index,it) in commends.withIndex()) {
            builder.append("    -${it.commend}  ${it.desc?:"No Description"}" + if (index!=commends.size - 1) "\n" else "")
        }
        val toString = builder.toString()
        return "$header ${version.versionString}\n$toString"
    }
}



