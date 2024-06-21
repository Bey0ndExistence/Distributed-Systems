package com.sd.laborator

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Singleton

@Singleton
class GeneratorSirSieveService {
    val MAX_SIZE = 1000001

    val LOG: Logger = LoggerFactory.getLogger(GeneratorSirFunction::class.java)
    fun findNumbersLessThanRecursive(n: Int): List<Int> {
        val numbers: MutableList<Int> = ArrayList()
        numbers.add(1)
        findNumbersLessThanRecursiveCall(1, n, numbers)
        return numbers
    }


    private fun findNumbersLessThanRecursiveCall(currentNumber: Int, n: Int, sir: MutableList<Int>) {
        if (currentNumber < n){
            val temp: Int = (sir[sir.size-1] + sir[sir.size-1]*2/currentNumber).toInt()
            sir.add(temp)
            findNumbersLessThanRecursiveCall(currentNumber+1, n, sir)
        }
    }

}