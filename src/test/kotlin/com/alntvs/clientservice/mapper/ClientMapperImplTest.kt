package com.alntvs.clientservice.mapper

import com.alntvs.clientservice.entity.ClientEntity
import com.alntvs.clientservice.model.ClientDTO
import org.junit.jupiter.api.Test

internal class ClientMapperImplTest {

    private val mapper = ClientMapperImpl()

    @Test
    fun clientEntityToDTO() {
        val emptyClientEntity = ClientEntity()
        val fullClientEntity =
            ClientEntity(1, "username1", "firstname1", "lastname1", "e@mail.my", "+71231231212", "Moscow", true)
        val expectedClientDTOFromEmptyClientEntity = ClientDTO()
        val expectedClientDTOFromFullClientEntity =
            ClientDTO(1, "username1", "firstname1", "lastname1", "e@mail.my", "+71231231212", "Moscow", true)

        val resultFromEmptyClientEntity = mapper.clientEntityToDTO(emptyClientEntity)
        val resultFromFullClientEntity = mapper.clientEntityToDTO(fullClientEntity)

        assert(expectedClientDTOFromEmptyClientEntity == resultFromEmptyClientEntity)
        assert(expectedClientDTOFromFullClientEntity == resultFromFullClientEntity)
    }

    @Test
    fun clientDTOToEntity() {
        val emptyClientDTO = ClientDTO()
        val fullClientDTO =
            ClientDTO(1, "username1", "firstname1", "lastname1", "e@mail.my", "+71231231212", "Moscow", true)
        val expectedClientEntityFromEmptyClientDTO = ClientEntity()
        val expectedClientEntityFromFullClientDTO =
            ClientEntity(1, "username1", "firstname1", "lastname1", "e@mail.my", "+71231231212", "Moscow", true)

        val resultFromEmptyClientDTO = mapper.clientDTOToEntity(emptyClientDTO)
        val resultFromFullClientDTO = mapper.clientDTOToEntity(fullClientDTO)

        assert(expectedClientEntityFromEmptyClientDTO == resultFromEmptyClientDTO)
        assert(expectedClientEntityFromFullClientDTO == resultFromFullClientDTO)
    }

    @Test
    fun clientEntityToDTOasList() {
        val clientEntityList = listOf(
            ClientEntity(),
            ClientEntity(1, "username1", "firstname1", "lastname1", "e@mail.my", "+71231231212", "Moscow", true)
        )
        val expectedClientDTOList = listOf(
            ClientDTO(),
            ClientDTO(1, "username1", "firstname1", "lastname1", "e@mail.my", "+71231231212", "Moscow", true)
        )

        val resultListFromClientEntityList = mapper.clientEntityToDTOasList(clientEntityList)

        assert(expectedClientDTOList == resultListFromClientEntityList)
    }
}