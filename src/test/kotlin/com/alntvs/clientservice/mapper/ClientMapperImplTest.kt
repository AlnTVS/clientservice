package com.alntvs.clientservice.mapper

import com.alntvs.clientservice.entity.ClientEntity
import com.alntvs.clientservice.model.ClientDTO
import io.kotest.matchers.collections.shouldContainAnyOf
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldMatchEach
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ClientMapperImplTest {

    private val mapper = ClientMapperImpl()

    @Test
    fun `clientEntityToDTO map empty`() {
        val clientEntity = ClientEntity()
        val expectedClientDTO = ClientDTO()

        val result = mapper.clientEntityToDTO(clientEntity)

        result shouldBe expectedClientDTO
    }

    @Test
    fun `clientEntityToDTO map full`() {
        val clientEntity =
            ClientEntity(1, "username1", "firstname1", "lastname1", "e@mail.my", "+71231231212", "Moscow", true)

        val expectedClientDTO =
            ClientDTO(1, "username1", "firstname1", "lastname1", "e@mail.my", "+71231231212", "Moscow", true)

        val result = mapper.clientEntityToDTO(clientEntity)

        result shouldBe expectedClientDTO
    }

    @Test
    fun `clientDTOToEntity map empty`() {
        val clientDTO = ClientDTO()
        val expectedClientEntity = ClientEntity()

        val result = mapper.clientDTOToEntity(clientDTO)

        result shouldBe expectedClientEntity
    }

    @Test
    fun `clientDTOToEntity map full`() {
        val clientDTO =
            ClientDTO(1, "username1", "firstname1", "lastname1", "e@mail.my", "+71231231212", "Moscow", true)
        val expectedClientEntity =
            ClientEntity(1, "username1", "firstname1", "lastname1", "e@mail.my", "+71231231212", "Moscow", true)

        val result = mapper.clientDTOToEntity(clientDTO)

        result shouldBe expectedClientEntity
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

        resultListFromClientEntityList shouldContainExactlyInAnyOrder expectedClientDTOList
    }
}