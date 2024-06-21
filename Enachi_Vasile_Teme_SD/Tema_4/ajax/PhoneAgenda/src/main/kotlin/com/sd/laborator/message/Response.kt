package com.sd.laborator.message

import com.sd.laborator.pojo.Person

data class Response(
    val status: String = "",
    val data: Any?
) {}