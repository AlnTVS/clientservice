package com.alntvs.clientservice.controller

import com.alntvs.clientservice.model.ClientDTO
import com.alntvs.clientservice.service.ClientService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/user"], produces = [APPLICATION_JSON_VALUE])
class ClientController(val clientService: ClientService) {

    @PostMapping("/create")
    fun create(@RequestBody clientDTO: ClientDTO) {
        clientService.create(clientDTO)
    }

    @GetMapping("/all")
    fun getAll(): List<ClientDTO> = clientService.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) = clientService.getById(id)

    @PostMapping("/update")
    fun update(@RequestBody clientDTO: ClientDTO) {
        clientService.update(clientDTO)
    }

    @GetMapping("/delete/{id}")
    fun delete(@PathVariable id: Long) = clientService.delete(id)
}