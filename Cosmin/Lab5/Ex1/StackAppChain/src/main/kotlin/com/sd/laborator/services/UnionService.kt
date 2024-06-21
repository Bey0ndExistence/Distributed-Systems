package com.sd.laborator.services

import com.sd.laborator.interfaces.CartesianProductOperation
import com.sd.laborator.interfaces.UnionOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.sd.laborator.model.Stack

@Service
class UnionService: UnionOperation {
    @Autowired
    private lateinit var nextService: CartesianProductService
    override fun executeOperation(A: Stack?, B: Stack?, message: String?): String {
        if(message == "compute") {
            if (A?.data?.count() != B?.data?.count())
                return "Multimile au count() diferit"

            var res = (nextService.executeOperation(A!!.data, B!!.data) union
                    nextService.executeOperation(B.data, A.data)
                    )
            return "compute~" + "{\"A\": \"" + A?.data.toString() + "\", \"B\": \"" + B?.data.toString() + "\", \"result\": \"" + res.toString() + "\"}"
        }
        return "Mesaj eronat"
    }

}