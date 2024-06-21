package com.sd.laborator.services

import com.sd.laborator.interfaces.PrimeNumberGenerator
import com.sd.laborator.interfaces.RegenerateInterfaceA
import com.sd.laborator.model.Stack
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RegenerateAService: RegenerateInterfaceA {
    @Autowired
    private lateinit var primeGenerator: PrimeNumberGenerator
    @Autowired
    private lateinit var nextService:RegenerateBService;
    private fun generateStack(count: Int): MutableSet<Int> {
        var X: MutableSet<Int> = mutableSetOf()
        if (count < 1)
            return X
        while (X.count() < count)
            X.add(primeGenerator.generatePrimeNumber())
        return X
    }

    private fun generaete(A:Stack?){
        A?.data = generateStack(20)
    }


    override fun execute(A: Stack?, B: Stack?, message: String?): String? {
        if(message == "regenerate_A"){
            generaete(A)
            return "A~" + A?.data.toString()
        }else if((A==null) and (message =="compute")){
            generaete(A)
            return nextService.execute(A,B,message)
        }else if((message == "regenerate_B") or (message == "compute")){
            return nextService.execute(A,B,message)
        }
        return "A nu este valabil"
    }

}