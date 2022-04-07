package com.alntvs.clientservice

import com.alntvs.clientservice.entity.ClientEntity
import com.alntvs.clientservice.model.ClientDTO
import com.alntvs.clientservice.model.DataDto
import com.alntvs.clientservice.model.ErrorDto
import com.alntvs.clientservice.repository.ClientRepository
import com.google.gson.GsonBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ClientServiceApplicationTests(
    @Autowired
    val restTemplate: TestRestTemplate,
    @Autowired
    val clientRepository: ClientRepository
) {

    val gson = GsonBuilder().serializeNulls().disableHtmlEscaping().create()

    @Test
    fun `create REST by correct request`() {
        val clientDTO = ClientDTO(null, "username1", "Name", "Surname", "e@mail.com", "+007", "Russia, Moscow", true)
        val jsonClientDTO = gson.toJson(clientDTO)
        val dataDto = DataDto("success")
        val jsonDataDto = gson.toJson(dataDto)

        val httpHeaders = HttpHeaders()
        httpHeaders.set("Content-Type", "application/json")
        val httpEntity = HttpEntity(jsonClientDTO, httpHeaders)

        val response = restTemplate.postForEntity("/user/create", httpEntity, String::class.java)

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).isEqualTo(jsonDataDto)
    }

    @Test
    fun `create REST by incorrect request, username is already exist`() {
        val username = "username1"
        val clientDTO =
            ClientDTO(null, username, "Name2", "Surname2", "e2@mail.com", "+008", "Russia, Saint-Petersburg", false)
        val clientEntity = ClientEntity(null, username, "Name", "Surname", "e@mail.com", "+007", "Russia, Moscow", true)
        val errorMsg = "Client with username:$username already exists!"
        clientRepository.save(clientEntity)
        val jsonClientEntity = gson.toJson(clientDTO)

        val httpHeaders = HttpHeaders()
        httpHeaders.set("Content-Type", "application/json")
        val httpEntity = HttpEntity(jsonClientEntity, httpHeaders)

        val response = restTemplate.postForEntity("/user/create", httpEntity, String::class.java)

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).contains(errorMsg)
    }

    @Test
    fun `create REST by incorrect request, username is null`() {
        val clientDTO = ClientDTO(null, null)
        val errorMsg = "Field 'userName' must be filled!"
        val jsonClientEntity = gson.toJson(clientDTO)

        val httpHeaders = HttpHeaders()
        httpHeaders.set("Content-Type", "application/json")
        val httpEntity = HttpEntity(jsonClientEntity, httpHeaders)

        val response = restTemplate.postForEntity("/user/create", httpEntity, String::class.java)

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).contains(errorMsg)
    }

    @Test
    fun `update REST by correct request`() {
        val clientDTO =
            ClientDTO(1, "username1", "Name", "Surname", "e@mail.com", "+007", "Russia, Saint-Petersburg", true)
        val jsonClientEntity = gson.toJson(clientDTO)
        val expectedData = DataDto("success")
        val expectedBody = gson.toJson(expectedData)
        clientRepository.save(ClientEntity(userName = "username"))

        val httpHeaders = HttpHeaders()
        httpHeaders.set("Content-Type", "application/json")
        val httpEntity = HttpEntity(jsonClientEntity, httpHeaders)

        val response = restTemplate.postForEntity("/user/update", httpEntity, String::class.java)

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).isEqualTo(expectedBody)
    }

    @Test
    fun `update REST by incorrect request, null id`() {
        val clientDTO =
            ClientDTO(null, "username1", "Name", "Surname", "e@mail.com", "+007", "Russia, Saint-Petersburg", true)
        val jsonClientEntity = gson.toJson(clientDTO)
        val expectedMsg = "Field 'id' and 'userName' must be filled!"

        val httpHeaders = HttpHeaders()
        httpHeaders.set("Content-Type", "application/json")
        val httpEntity = HttpEntity(jsonClientEntity, httpHeaders)

        val response = restTemplate.postForEntity("/user/update", httpEntity, String::class.java)

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).contains(expectedMsg)
    }

    @Test
    fun `update REST by correct request, null username`() {
        val clientDTO =
            ClientDTO(1, null, "Name", "Surname", "e@mail.com", "+007", "Russia, Saint-Petersburg", true)
        val jsonClientEntity = gson.toJson(clientDTO)
        val expectedData = DataDto("success")
        val expectedBody = gson.toJson(expectedData)
        clientRepository.save(ClientEntity(userName = "username"))

        val httpHeaders = HttpHeaders()
        httpHeaders.set("Content-Type", "application/json")
        val httpEntity = HttpEntity(jsonClientEntity, httpHeaders)

        val response = restTemplate.postForEntity("/user/update", httpEntity, String::class.java)

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).isEqualTo(expectedBody)
    }

    @Test
    fun `update REST by incorrect request, new already exist username`() {
        val username = "username"
        val clientEntity1 = ClientEntity(userName = username)
        val clientEntity2 = ClientEntity(userName = "other username")
        val clientDTO =
            ClientDTO(2, username)
        val jsonClientEntity = gson.toJson(clientDTO)
        val expectedMsg = "Client with username:$username already exists!"
        clientRepository.save(clientEntity1)
        clientRepository.save(clientEntity2)

        val httpHeaders = HttpHeaders()
        httpHeaders.set("Content-Type", "application/json")
        val httpEntity = HttpEntity(jsonClientEntity, httpHeaders)

        val response = restTemplate.postForEntity("/user/update", httpEntity, String::class.java)

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).contains(expectedMsg)
    }

    @Test
    fun `update REST by incorrect request, doesn't exist id`() {
        val id: Long = 1
        val clientDTO =
            ClientDTO(id)
        val jsonClientEntity = gson.toJson(clientDTO)
        val expectedMsg = "Client with id:$id doesn't exists!"

        val httpHeaders = HttpHeaders()
        httpHeaders.set("Content-Type", "application/json")
        val httpEntity = HttpEntity(jsonClientEntity, httpHeaders)

        val response = restTemplate.postForEntity("/user/update", httpEntity, String::class.java)

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body).contains(expectedMsg)
    }

}
