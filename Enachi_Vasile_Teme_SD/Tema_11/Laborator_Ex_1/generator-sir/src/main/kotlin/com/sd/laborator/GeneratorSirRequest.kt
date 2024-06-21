package com.sd.laborator

import io.micronaut.core.annotation.Introspected

@Introspected
class GeneratorSirRequest {
	private lateinit var number: Integer

	fun getNumber(): Int {
		return number.toInt()
	}
}