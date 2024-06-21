package com.sd.laborator

import io.micronaut.core.annotation.Introspected

@Introspected
class AlgorithmResponse {
    private var message: String? = null
    private var pairs: Set<Pair<Int, Int>>? = null

    fun getPairs(): Set<Pair<Int, Int>>? {
        return pairs
    }

    fun getMessage(): String? {
        return message
    }

    fun setPairs(primes: Set<Pair<Int, Int>>?) {
        this.pairs = primes
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    override fun toString(): String {
        return "AlgorithmResponse(message=$message, pairs=$pairs)"
    }
}
