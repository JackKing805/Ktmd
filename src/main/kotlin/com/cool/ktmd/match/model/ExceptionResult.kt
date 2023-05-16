package com.cool.ktmd.match.model

sealed class ExceptionResult{
    class NoException:ExceptionResult()

    class HaveException(val desc:String):ExceptionResult()
}
