package com.sd.laborator.services

import com.sd.laborator.interfaces.PrimeNumberGenerator
import com.sd.laborator.interfaces.RegenerateInterfaceB
import com.sd.laborator.model.Stack
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RegenerateBService: RegenerateInterfaceB {

    @Autowired
    private lateinit var primeGenerator: PrimeNumberGenerator
    @Autowired
    private lateinit var nextService:UnionService;
    private fun generateStack(count: Int): MutableSet<Int> {
        var X: MutableSet<Int> = mutableSetOf()
        if (count < 1)
            return X
        while (X.count() < count)
            X.add(primeGenerator.generatePrimeNumber())
        return X
    }


    private fun generaete(B:Stack?){
        B?.data = generateStack(20)
    }


    override fun execute(A: Stack?, B: Stack?, message: String?): String? {
        if(message == "regenerate_B"){
            generaete(B)
            return "B~" + B?.data.toString()
        }else if((B==null) and (message =="compute")){
            generaete(B)
            return nextService.executeOperation(A,B, message)
        }else if((message =="compute")){
            return nextService.executeOperation(A,B,message)
        }
        return "B nu este valabil"
    }
}