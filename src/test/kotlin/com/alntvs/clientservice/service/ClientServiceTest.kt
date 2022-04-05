package com.alntvs.clientservice.service

import com.alntvs.clientservice.entity.ClientEntity
import com.alntvs.clientservice.exception.ClientServiceException
import com.alntvs.clientservice.mapper.ClientMapper
import com.alntvs.clientservice.mapper.ClientMapperImpl
import com.alntvs.clientservice.model.ClientDTO
import com.alntvs.clientservice.repository.ClientRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import javax.persistence.EntityNotFoundException

internal class ClientServiceTest {

    private val clientRepository: ClientRepository = mockk<ClientRepository>()
    private val mapper: ClientMapper = mockk<ClientMapperImpl>()
    private val clientService: ClientService = ClientService(clientRepository, mapper)

    @Test
    fun `create by correct data`() {
        val username = "user1"
        val clientDTO = ClientDTO(userName = username)
        val mappedClientEntity = ClientEntity(userName = username)
        val savedClientEntity = ClientEntity(id = 1, userName = username)

        every { mapper.clientDTOToEntity(clientDTO) } returns mappedClientEntity
        every { clientRepository.findByUserName(username) } returns null
        every { clientRepository.save(mappedClientEntity) } returns savedClientEntity

        clientService.create(clientDTO)
        verify { clientRepository.save(mappedClientEntity) }
    }

    @Test
    fun `create client with already existing username`() {
        val username = "user1"
        val clientDTO = ClientDTO(userName = username)
        val mappedClientEntity = ClientEntity(userName = username)
        val msg = "Client with username:${clientDTO.userName} already exists!"

        every { mapper.clientDTOToEntity(clientDTO) } returns mappedClientEntity
        every { clientRepository.findByUserName(username) } returns mappedClientEntity

        val exception = assertThrows<IllegalArgumentException> {
            clientService.create(clientDTO)
        }
        assert(msg == exception.message)
    }

    @Test
    fun `create client with incorrect data, null username`() {
        val username: String? = null
        val clientDTO = ClientDTO(userName = username)
        val msg = "Field 'userName' must be filled!"

        val exception = assertThrows<IllegalArgumentException> {
            clientService.create(clientDTO)
        }

        assert(msg == exception.message)
    }

    @Test
    fun getAll() {
        val client1 = ClientEntity(id = 1, userName = "user1")
        val client2 = ClientEntity(id = 2, userName = "user2")
        val clientDTO1 = ClientDTO(id = 1, userName = "user1")
        val clientDTO2 = ClientDTO(id = 2, userName = "user2")
        val clientEntityList = listOf(client1, client2)
        val clientDTOList = listOf(clientDTO2, clientDTO1)

        every { clientRepository.findAll() } returns listOf(client1, client2)
        every { mapper.clientEntityToDTOasList(clientEntityList) } returns clientDTOList

        val result = clientService.getAll()

        verify { clientRepository.findAll() }
        assert(result == clientDTOList)
    }

    @Test
    fun `getById for exist user`() {
        val id: Long = 1
        val userName = "user1"
        val clientEntity = ClientEntity(id = id, userName = userName)
        val clientDTO = ClientDTO(id = id, userName = userName)

        every { clientRepository.getById(id) } returns clientEntity
        every { mapper.clientEntityToDTO(clientEntity) } returns clientDTO

        val result = clientService.getById(id)

        assert(result == clientDTO)
    }

    @Test
    fun `getById for not exist user`() {
        val id: Long = 1
        val msg = "User with id = $id doesn't exists"

        every { clientRepository.getById(id) } throws JpaObjectRetrievalFailureException(EntityNotFoundException())

        val exception = assertThrows<ClientServiceException> {
            clientService.getById(id)
        }

        assert(exception.message == msg)
    }

    @Test
    fun `update with correct clientDTO`() {
        val id: Long = 1
        val username = "user1"
        val clientDTO = ClientDTO(id = id, userName = username)
        val clientEntity = ClientEntity(id = id, userName = username)

        every { mapper.clientDTOToEntity(clientDTO) } returns clientEntity
        every { clientRepository.findByUserName(username) } returns null
        every { clientRepository.findByIdOrNull(id) } returns clientEntity
        every { clientRepository.save(clientEntity) } returns clientEntity

        clientService.update(clientDTO)

        verify {
            clientRepository.save(clientEntity)
        }
    }

    @Test
    fun `update username on already exist client`() {
        val idModifyClient: Long = 1
        val idOtherUser: Long = 2
        val username = "userName"
        val oldUsername = "oldUsername"
        val oldClientEntity = ClientEntity(id = idModifyClient, userName = oldUsername)
        val otherClientEntity = ClientEntity(id = idOtherUser, userName = username)
        val clientDTO = ClientDTO(id = idModifyClient, userName = username)
        val msg = "Client with username:${clientDTO.userName} already exists!"


        every { clientRepository.findByIdOrNull(idModifyClient) } returns oldClientEntity
        every { clientRepository.findByUserName(username) } returns otherClientEntity

        val result = assertThrows<IllegalArgumentException> {
            clientService.update(clientDTO)
        }

        assert(result.message == msg)
    }

    @Test
    fun `update not exist client`() {
        val id: Long = 1
        val username = "userName"
        val clientEntity = ClientEntity(id = id, userName = username)
        val clientDTO = ClientDTO(id = id, userName = username)
        val msg = "Client with id:${clientDTO.id} doesn't exists!"


        every { clientRepository.findByIdOrNull(id) } returns null
        every { clientRepository.findByUserName(clientEntity.userName!!) } returns clientEntity
        every { mapper.clientDTOToEntity(clientDTO) } answers { callOriginal() }
        every { clientRepository.save(clientEntity) } returns clientEntity

        val result = assertThrows<IllegalArgumentException> {
            clientService.update(clientDTO)
        }

        assert(result.message == msg)
    }

    @Test
    fun `update with null id`() {
        val id: Long? = null
        val username = "user1"
        val clientDTO = ClientDTO(id = id, userName = username)
        val clientEntity = ClientEntity(id = id, userName = username)
        val msg = "must be filled!"

        every { clientRepository.findByIdOrNull(id) } returns clientEntity
        every { clientRepository.findByUserName(clientEntity.userName!!) } returns clientEntity
        every { mapper.clientDTOToEntity(clientDTO) } answers { callOriginal() }
        every { clientRepository.save(clientEntity) } returns clientEntity

        val exception = assertThrows<IllegalArgumentException> {
            clientService.update(clientDTO)
        }

        assert(exception.message?.contains(msg) ?: false)
    }

    @Test
    fun `update with null username`() {
        val id: Long = 1
        val clientDTO = ClientDTO(id = id, userName = null)
        val username = "user1"
        val clientEntity = ClientEntity(id = id, userName = username)

        every { clientRepository.findByIdOrNull(id) } returns clientEntity
        every { clientRepository.findByUserName(clientEntity.userName!!) } returns clientEntity
        every { mapper.clientDTOToEntity(clientDTO) } answers { callOriginal() }
        every { clientRepository.save(clientEntity) } returns clientEntity

        clientService.update(clientDTO)

        verify { clientRepository.save(clientEntity) }
    }

    @Test
    fun delete() {
    }
}