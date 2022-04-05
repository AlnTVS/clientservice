package com.alntvs.clientservice.service

import com.alntvs.clientservice.model.ClientDTO
import com.alntvs.clientservice.model.Operation
import com.alntvs.clientservice.model.OperationDTO
import com.alntvs.clientservice.model.ResponseDTO
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class KafkaConsumerServiceTest {
    private val clientService = mockk<ClientService>()
    private val kafkaProducerService = mockk<KafkaProducerService>()
    private val kafkaConsumerService = KafkaConsumerService(clientService, kafkaProducerService)

    @Test
    fun `webConsumer create by correct data`() {
        val clientDTO = ClientDTO(userName = "user1")
        val operationDTO = OperationDTO(Operation.CREATE, clientDTO)
        val responseOk = ResponseDTO("OK")

        justRun { clientService.create(clientDTO) }
        justRun { kafkaProducerService.sendResponse(any()) }

        kafkaConsumerService.webConsumer(operationDTO)
        verify {
            clientService.create(clientDTO)
            kafkaProducerService.sendResponse(responseOk)
        }
    }

    @Test
    fun `webConsumer update by correct data`() {
        val clientDTO = ClientDTO(id = 1, userName = "user1")
        val operationDTO = OperationDTO(Operation.UPDATE, clientDTO)
        val responseOk = ResponseDTO("OK")

        justRun { clientService.update(clientDTO) }
        justRun { kafkaProducerService.sendResponse(responseOk) }

        kafkaConsumerService.webConsumer(operationDTO)
        verify {
            clientService.update(clientDTO)
            kafkaProducerService.sendResponse(responseOk)
        }
    }

    @Test
    fun `webConsumer delete by correct data`() {
        val id: Long = 1
        val clientDTO = ClientDTO(id = id)
        val operationDTO = OperationDTO(Operation.DELETE, clientDTO)
        val responseOk = ResponseDTO("OK")

        justRun { clientService.delete(id) }
        justRun { kafkaProducerService.sendResponse(responseOk) }

        kafkaConsumerService.webConsumer(operationDTO)
        verify {
            clientService.delete(id)
            kafkaProducerService.sendResponse(responseOk)
        }
    }

    @Test
    fun `webConsumer no operation in received message`() {
        val operationDTO = OperationDTO(null, null)
        val exceptionMsg = "Received operation is null!"
        val responseError = ResponseDTO(exceptionMsg)

        justRun { kafkaProducerService.sendResponse(responseError) }

        kafkaConsumerService.webConsumer(operationDTO)

        verify {
            kafkaProducerService.sendResponse(responseError)
        }
    }

    @Test
    fun `webConsumer null clientDTO for all operations`() {
        for (operation in Operation.values()) {
            val operationDTO = OperationDTO(operation, null)
            val paramName = when (operation) {
                Operation.UPDATE -> "clientDTO"
                Operation.CREATE -> "clientDTO"
                Operation.DELETE -> "id"
            }
            val exceptionMsg = "Parameter '$paramName' must not be null! Operation: '$operation'."
            val responseError = ResponseDTO(exceptionMsg)

            justRun { kafkaProducerService.sendResponse(responseError) }

            kafkaConsumerService.webConsumer(operationDTO)

            verify {
                kafkaProducerService.sendResponse(responseError)
            }
        }
    }
}