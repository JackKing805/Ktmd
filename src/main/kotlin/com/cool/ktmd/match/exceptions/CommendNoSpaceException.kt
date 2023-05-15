package com.cool.ktmd.match.exceptions

class CommendNoSpaceException(commend1:String,commend2:String):RuntimeException(
    "commend[$commend1] and commend[$commend2] must have one space"
)