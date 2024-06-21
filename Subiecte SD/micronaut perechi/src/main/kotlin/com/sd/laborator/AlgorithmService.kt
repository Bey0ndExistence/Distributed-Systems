package com.sd.laborator

import jakarta.inject.Singleton

@Singleton
class AlgorithmService {
    fun findPairs(A: List<Int>, B: List<Int>): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()

        A.forEach {
            a -> run {
                B.forEach {
                    b -> run {
                        if(a * b == a + b * 3)
                            result.add(Pair(a, b))
                    }
                }
            }
        }

        return result
    }
}