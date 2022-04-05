package com.alntvs.clientservice.service

import com.alntvs.clientservice.entity.ClientEntity
import com.alntvs.clientservice.mapper.ClientMapper
import com.alntvs.clientservice.model.ClientDTO
import com.alntvs.clientservice.repository.ClientRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ClientServiceTest {

    private val clientRepository: ClientRepository = mockk<ClientRepository>()
    private val mapper: ClientMapper = mockk<ClientMapper>()
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
    fun getById() {
    }

    @Test
    fun update() {
    }

    @Test
    fun delete() {
    }
}