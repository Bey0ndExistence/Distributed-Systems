package com.sd.laborator.controllers

import com.sd.laborator.interfaces.AgendaService
import com.sd.laborator.message.Response
import com.sd.laborator.pojo.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class AgendaController {
    @Autowired
    private lateinit var agendaService: AgendaService

    @RequestMapping(value = ["/person"], method = [RequestMethod.POST])
    fun createPerson(@RequestBody person: Person): Response {
        agendaService.createPerson(person)
        return Response(HttpStatus.OK.reasonPhrase, person)
    }

    @RequestMapping(value = ["/person/{id}"], method = [RequestMethod.GET])
    fun getPerson(@PathVariable id: Int): Response {
        val person: Person? = agendaService.getPerson(id)
        val status = if (person == null) {
            HttpStatus.NOT_FOUND
        } else {
            HttpStatus.OK
        }
        return Response(status.reasonPhrase, person)
    }

    @RequestMapping(value = ["/person/{id}"], method = [RequestMethod.PUT])
    fun updatePerson(@PathVariable id: Int, @RequestBody person: Person): Response {
        agendaService.getPerson(id)?.let {
            agendaService.updatePerson(it.id, person)
            return Response(HttpStatus.ACCEPTED.reasonPhrase, person)
        } ?: return Response(HttpStatus.NOT_FOUND.reasonPhrase, person)
    }

    @RequestMapping(value = ["/person/{id}"], method = [RequestMethod.DELETE])
    fun deletePerson(@PathVariable id: Int): Response {
        var person = agendaService.getPerson(id)
        return if (person != null) {
            agendaService.deletePerson(id)
            Response(HttpStatus.OK.reasonPhrase, person)
        } else {
            Response(HttpStatus.NOT_FOUND.reasonPhrase, person)
        }
    }

    @RequestMapping(value = ["/agenda"], method = [RequestMethod.GET])
    fun search(@RequestParam(required = false, name = "lastName", defaultValue = "") lastName: String,
                    @RequestParam(required = false, name = "firstName", defaultValue = "") firstName: String,
                     @RequestParam(required = false, name = "telephone", defaultValue = "") telephoneNumber: String):
            Response {
        val personList = agendaService.searchAgenda(lastName, firstName, telephoneNumber)
        var httpStatus = HttpStatus.OK
        if (personList.isEmpty()) {
            httpStatus = HttpStatus.NO_CONTENT
        }
        return Response(httpStatus.reasonPhrase, personList)
    }
}