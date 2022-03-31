package com.alntvs.clientservice.service

import com.alntvs.clientservice.mapper.ClientMapper
import com.alntvs.clientservice.model.ClientDTO
import com.alntvs.clientservice.repository.ClientRepository
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class ClientService(private val clientRepository: ClientRepository, private val mapper: ClientMapper) {

    fun create(clientDTO: ClientDTO) {
        clientRepository.save(mapper.clientDTOToEntity(clientDTO))
    }

    fun getAll() = mapper.clientEntityToDTOasList(clientRepository.findAll())

    fun getById(id: Long): ClientDTO {
        try {
            return mapper.clientEntityToDTO(clientRepository.getById(id))
        } catch (e: EntityNotFoundException) {
            throw EntityNotFoundException("User with id = $id not exist" + Date().time)
        }
    }
}