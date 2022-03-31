package com.alntvs.clientservice.service

import com.alntvs.clientservice.mapper.ClientMapper
import com.alntvs.clientservice.model.ClientDTO
import com.alntvs.clientservice.repository.ClientRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class ClientService(private val clientRepository: ClientRepository, private val mapper: ClientMapper) {

    fun create(clientDTO: ClientDTO) {
        clientDTO.userName?.also {
            clientRepository.findByUserName(it)?.also {
                throw IllegalArgumentException("Client with username:${clientDTO.userName} already exists!")
            } ?: clientRepository.save(mapper.clientDTOToEntity(clientDTO))
        } ?: throw IllegalArgumentException("Field 'userName' must be filled!")
    }

    fun getAll() = mapper.clientEntityToDTOasList(clientRepository.findAll())

    fun getById(id: Long): ClientDTO {
        try {
            return mapper.clientEntityToDTO(clientRepository.getById(id))
        } catch (e: EntityNotFoundException) {
            throw EntityNotFoundException("User with id = $id not exists")
        }
    }

    fun update(clientDTO: ClientDTO) {
        clientDTO.id?.also {
            clientRepository.findByIdOrNull(it)?.also {
                clientRepository.save(mapper.clientDTOToEntity(clientDTO))
            } ?: throw IllegalArgumentException("Client with id:${clientDTO.id} doesn't exists!")
        } ?: throw IllegalArgumentException("Field 'id' and 'userName' must be filled!")
    }
}