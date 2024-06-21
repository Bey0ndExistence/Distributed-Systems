package com.sd.laborator.services

import com.sd.laborator.model.Stack

class BRegenerator: Regenerator() {
    override fun executeOperation(A: Stack?, B: Stack?, operation : String): String? {
        if (B == null){
            return "B nu trebuie sa fie obiect null"
        }
        return if (operation.equals("regenerate_B")){
            B.data = this.generateStackData(20)
            "B~" + B.data.toString()
        } else {
            if (operation.equals("compute")) {
                if (B.data.count() == 0) {
                    B.data = generateStackData(20)
                }
            }
            if (this.nextOperation != null) this.nextOperation!!.executeOperation(A, B, operation) else null
        }
    }
}