package com.sd.laborator.model

data class Stack(var data: MutableSet<Int>){
    constructor(count : Int): this(mutableSetOf(count))
}