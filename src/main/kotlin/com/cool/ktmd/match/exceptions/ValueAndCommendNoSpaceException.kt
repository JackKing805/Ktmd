package com.cool.ktmd.match.exceptions

class ValueAndCommendNoSpaceException(
    value:String,
    commend:String
):RuntimeException(
    "between in commend[$commend]  and value[$value] must have space"
)