package com.cool.ktmd.match.model

import java.util.regex.Pattern

data class Patterns(
    val keyValuePattern:Pattern,
    val keyPattern: Pattern
)