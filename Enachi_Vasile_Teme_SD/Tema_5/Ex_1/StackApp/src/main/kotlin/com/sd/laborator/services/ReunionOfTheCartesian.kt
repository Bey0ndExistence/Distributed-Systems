package com.sd.laborator.services

import com.sd.laborator.interfaces.CartesianProductOperation
import com.sd.laborator.interfaces.PrimeNumberGenerator
import com.sd.laborator.interfaces.UnionOperation
import com.sd.laborator.model.Stack
import org.springframework.beans.factory.annotation.Autowired

class ReunionOfTheCartesian : OperationChainAbstract(){

    private var cartesianProductOperation: CartesianProductOperation = CartesianProductService()
    private var unionOperation: UnionOperation = UnionService()


    override fun executeOperation(A: Stack?, B: Stack?, operation : String): String? {
        if (A == null || B == null){
            return "A si B nu trebuie sa fie obiecte null"
        }
        if (operation.equals("compute")) {
            if (A.data.count() == B.data.count()) {
                // (A x B) U (B x B)
                val partialResult1 = cartesianProductOperation.executeOperation(A.data, B.data)
                val partialResult2 = cartesianProductOperation.executeOperation(B.data, B.data)
                val result = unionOperation.executeOperation(partialResult1, partialResult2)
                return "compute~" + "{\"A\": \"" + A.data.toString() + "\", \"B\": \"" + B.data.toString() + "\", \"result\": \"" + result.toString() + "\"}"
            }
            return "compute~" + "Error: A.count() != B.count()"
        } else {
            return if (this.nextOperation != null) this.nextOperation!!.executeOperation(A, B, operation) else null
        }
    }
}