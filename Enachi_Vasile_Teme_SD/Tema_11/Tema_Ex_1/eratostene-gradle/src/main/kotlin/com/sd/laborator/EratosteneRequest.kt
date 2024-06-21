package com.sd.laborator

import io.micronaut.core.annotation.Introspected

data class Data(val data: Integer, val toCheck: List<Integer>)

@Introspected
class EratosteneRequest{
	private lateinit var number: Data

	fun getNumber(): Int {
		return number.data.toInt()
	}

	fun getListToCheck(): List<Int>{
		return number.toCheck.map { it.toInt() }
	}
}