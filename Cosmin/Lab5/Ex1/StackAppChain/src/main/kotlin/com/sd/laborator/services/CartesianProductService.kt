package com.sd.laborator.services

import com.sd.laborator.interfaces.CartesianProductOperation
import com.sd.laborator.interfaces.RegenerateInterfaceA
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CartesianProductService: CartesianProductOperation {

    override fun executeOperation(A: Set<Int>, B: Set<Int>): Set<Pair<Int, Int>> {
        var result: MutableSet<Pair<Int, Int>> = mutableSetOf()
        A.forEach { a -> B.forEach { b -> result.add(Pair(a, b)) } }
        return result.toSet()
    }
}