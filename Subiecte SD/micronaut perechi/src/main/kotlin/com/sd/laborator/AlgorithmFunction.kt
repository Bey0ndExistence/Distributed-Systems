package com.sd.laborator;

import io.micronaut.function.FunctionBean
import io.micronaut.function.executor.FunctionInitializer
import jakarta.inject.Inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.function.Supplier
import kotlin.random.Random

@FunctionBean("eratostene")
class AlgorithmFunction(val A: List<Int>, val B: List<Int>) : FunctionInitializer(), Supplier<AlgorithmResponse> {
    @Inject
    private  lateinit var algorithmService: AlgorithmService

    private val LOG: Logger = LoggerFactory.getLogger(AlgorithmFunction::class.java)

    override fun get(): AlgorithmResponse {
        val response = AlgorithmResponse()
        LOG.info("Se calculeaza perechile...")
        LOG.info("${A}")
        LOG.info("${B}")
        response.setPairs((algorithmService.findPairs(A, B).toSet()))

        LOG.info("Am calculat perechile UwU ${algorithmService.findPairs(A, B)}")
        response.setMessage("Perechile so generat coai")
        return response
    }
//    override fun apply(msg : EratosteneRequest) : EratosteneResponse {
//        // preluare numar din parametrul de intrare al functiei
//        val number = msg.getNumber()
//
//        val response = EratosteneResponse()
//
//        // se verifica daca numarul nu depaseste maximul
//        if (number >= eratosteneSieveService.MAX_SIZE) {
//            LOG.error("Parametru prea mare! $number > maximul de ${eratosteneSieveService.MAX_SIZE}")
//            response.setMessage("Se accepta doar parametri mai mici ca " + eratosteneSieveService.MAX_SIZE)
//            return response
//        }
//
//        LOG.info("Se calculeaza primele $number numere prime ...")
//
//        // se face calculul si se seteaza proprietatile pe obiectul cu rezultatul
//        response.setPrimes(eratosteneSieveService.findPrimesLessThan(number))
//        response.setMessage("Calcul efectuat cu succes!")
//
//        LOG.info("Calcul incheiat!")
//        return response
//    }
}

/**
 * This main method allows running the function as a CLI application using: echo '{}' | java -jar function.jar 
 * where the argument to echo is the JSON to be parsed.
 */
fun main(args : Array<String>) {
    val A = List(100){ Random.nextInt(0, 100)}
    val B = List(100){ Random.nextInt(0, 100)}

    val function = AlgorithmFunction(A, B)
    function.run(args, { context -> function.get() })
}