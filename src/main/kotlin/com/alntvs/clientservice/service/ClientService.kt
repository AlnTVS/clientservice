package com.alntvs.clientservice.service

import com.alntvs.clientservice.exception.ClientServiceException
import com.alntvs.clientservice.mapper.ClientMapper
import com.alntvs.clientservice.model.ClientDTO
import com.alntvs.clientservice.repository.ClientRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.stereotype.Service

@Service
class ClientService(
    private val clientRepository: ClientRepository,
    private val mapper: ClientMapper
) {

    fun create(clientDTO: ClientDTO) {
        clientDTO.userName?.also {
            clientRepository.findByUserName(it)?.also {
                throw IllegalArgumentException("Client with username:${clientDTO.userName} already exists!")
            } ?: clientRepository.save(mapper.clientDTOToEntity(clientDTO))
        } ?: throw IllegalArgumentException("Field 'userName' must be filled!")
    }

    fun getAll() = mapper.clientEntityToDTOasList(clientRepository.findAll())

    fun getById(id: Long): ClientDTO {
        return mapper.clientEntityToDTO(
            try {
                clientRepository.getById(id)
            } catch (e: JpaObjectRetrievalFailureException) {
                throw ClientServiceException("User with id = $id doesn't exists")
            }
        )
    }

    fun update(clientDTO: ClientDTO) {
        clientDTO.id?.also {
            clientRepository.findByIdOrNull(it)?.also { clientById ->
                val userName: String = clientDTO.userName ?: clientById.userName!!
                clientRepository.findByUserName(userName)?.also { clientByUserName ->
                    if (clientById.id != clientByUserName.id) {
                        throw IllegalArgumentException("Client with username:${clientDTO.userName} already exists!")
                    }
                }
                clientRepository.save(mapper.clientDTOToEntity(clientDTO))
            } ?: throw IllegalArgumentException("Client with id:${clientDTO.id} doesn't exists!")
        } ?: throw IllegalArgumentException("Field 'id' and 'userName' must be filled!")
    }

    fun delete(id: Long) {
        clientRepository.findByIdOrNull(id)?.also {
            clientRepository.deleteById(id)
            } ?: throw IllegalArgumentException("Client with id:$id doesn't exists!")
    }
}