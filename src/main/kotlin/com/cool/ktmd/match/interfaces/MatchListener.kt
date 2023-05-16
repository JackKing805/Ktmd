package com.cool.ktmd.match.interfaces

import com.cool.ktmd.match.model.MatchResult

interface MatchListener {
    fun onMatch(matchResult: MatchResult)

    fun onException(exception: Exception)
}