package com.cool.ktmd.match.exceptions

class ErrorHeaderException(header:String,input:String):RuntimeException(
    "commend header[$header] can't found in your input,please ensure your input[$input] is correct"
)