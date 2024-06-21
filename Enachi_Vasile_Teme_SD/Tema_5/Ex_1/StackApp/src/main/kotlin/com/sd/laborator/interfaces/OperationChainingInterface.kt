package com.sd.laborator.interfaces

import com.sd.laborator.model.Stack

interface OperationChainingInterface {
    fun executeOperation(A : Stack?, B : Stack?, operation : String):String?
}