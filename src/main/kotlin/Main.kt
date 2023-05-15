import com.cool.ktmd.match.KmdMatcher
import com.cool.ktmd.match.model.FormatterCommend

//命令行：(^[a-z|A-Z]*)[ ]+(-[a-z|A-Z][ ]+\w*)
fun main(args: Array<String>) {
    val kmdMatcher = KmdMatcher(
        "ktor", arrayOf(
            FormatterCommend("help", "获取帮助"),
            FormatterCommend("version", "获取当前命令行版本"),
            FormatterCommend("wake", "唤醒手机"),
            FormatterCommend("open", "打开对应包名的应用"),
        )
    )
    println(kmdMatcher.info())
    while (true) {
        val readln = readln()
        if (readln.isNotEmpty()) {
            try {
                val match = kmdMatcher.match(readln)
                match.commends.forEach {
                    when (it.commend) {
                        "version" -> println("version:${kmdMatcher.version().versionString}")
                        "wake" -> println(it.value)
                        "open" -> {
                            println("openPackage:"+it.value)
                        }
                        "help"->{
                            println(kmdMatcher.info())
                        }
                        else -> {
                            println(it)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("error")
            }
        }
    }
}
