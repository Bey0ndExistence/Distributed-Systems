package com.sd.laborator.services

import com.sd.laborator.interfaces.OperationChainingInterface

abstract class OperationChainAbstract : OperationChainingInterface {
    protected var nextOperation : OperationChainAbstract? = null
    fun setNext(next : OperationChainAbstract) {
        var latest = this
        while (latest.nextOperation != null){
            latest = latest.nextOperation!!
        }
        latest.nextOperation = next
    }
}