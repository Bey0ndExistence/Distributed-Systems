package com.sd.laborator;

import io.micronaut.function.executor.FunctionInitializer
import io.micronaut.function.FunctionBean;
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.function.Function;
import javax.inject.Inject

@FunctionBean("generator-sir")
class GeneratorSirFunction : FunctionInitializer(), Function<GeneratorSirRequest, GeneratorSirResponse> {
    @Inject
    private lateinit var generatorSirSieveService: GeneratorSirSieveService

    private val LOG: Logger = LoggerFactory.getLogger(GeneratorSirFunction::class.java)

    override fun apply(msg : GeneratorSirRequest) : GeneratorSirResponse {
        // preluare numar din parametrul de intrare al functiei

        val number = msg.getNumber()
        LOG.info("Se calculeaza primele $number numere prime ...")
        val response = GeneratorSirResponse()

        // se verifica daca numarul nu depaseste maximul
        if (number >= generatorSirSieveService.MAX_SIZE) {
            LOG.error("Parametru prea mare! $number > maximul de ${generatorSirSieveService.MAX_SIZE}")
            response.setMessage("Se accepta doar parametri mai mici ca " + generatorSirSieveService.MAX_SIZE)
            return response
        }

        LOG.info("Se calculeaza pentru n = $number ...")

        // se face calculul si se seteaza proprietatile pe obiectul cu rezultatul
        // response.setPrimes(eratosteneSieveService.findPrimesLessThan(number))
        response.setPrimes(generatorSirSieveService.findNumbersLessThanRecursive(number))
        response.setMessage("Calcul efectuat cu succes!")

        LOG.info("Calcul incheiat!")

        return response
    }   
}

/**
 * This main method allows running the function as a CLI application using: echo '{}' | java -jar function.jar 
 * where the argument to echo is the JSON to be parsed.
 */
fun main(args : Array<String>) { 
    val function = GeneratorSirFunction()
    function.run(args, { context -> function.apply(context.get(GeneratorSirRequest::class.java))})
}