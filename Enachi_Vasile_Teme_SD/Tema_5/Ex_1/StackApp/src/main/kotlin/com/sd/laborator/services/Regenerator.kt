package com.sd.laborator.services

import com.sd.laborator.interfaces.PrimeNumberGenerator

abstract class Regenerator: OperationChainAbstract() {
    private var primeGenerator: PrimeNumberGenerator = PrimeNumberGeneratorService()

    protected fun generateStackData(count: Int): MutableSet<Int> {
        var X: MutableSet<Int> = mutableSetOf()
        if (count < 1)
            return X
        while (X.count() < count)
            X.add(primeGenerator.generatePrimeNumber())
        return X
    }
}