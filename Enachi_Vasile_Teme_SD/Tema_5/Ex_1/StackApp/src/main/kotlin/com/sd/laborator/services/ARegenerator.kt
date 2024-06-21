package com.sd.laborator.services

import com.sd.laborator.model.Stack

class ARegenerator: Regenerator() {

    override fun executeOperation(A: Stack?, B: Stack?, operation : String): String? {
        if (A == null){
            return "A nu trebuie sa fie obiect null"
        }
        return if (operation.equals("regenerate_A")) {
            A.data = this.generateStackData(20)
            "A~" + A.data.toString()
        } else {
            if (operation.equals("compute")) {
                if (B!!.data.count() == 0) {
                    B.data = generateStackData(20)
                }
            }
            if (this.nextOperation != null) this.nextOperation!!.executeOperation(A, B, operation) else null
        }
    }
}