package com.alntvs.clientservice.service

import com.alntvs.clientservice.exception.ClientServiceException
import com.alntvs.clientservice.model.Operation.*
import com.alntvs.clientservice.model.OperationDTO
import com.alntvs.clientservice.model.ResponseDTO
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumerService(
    private val clientService: ClientService,
    private val kafkaProducerService: KafkaProducerService
) {

    @KafkaListener(id = "ClientService", topics = ["client_service_topic"])
    fun webConsumer(msg: OperationDTO) {
        val op = msg.operation
        try {
            msg.clientDTO
                ?: throw ClientServiceException("Received null clientDTO from Kafka for $op operation")
            when (msg.operation) {
                CREATE -> clientService.create(msg.clientDTO)
                UPDATE -> clientService.update(msg.clientDTO)
                DELETE -> clientService.delete(
                    msg.clientDTO.id ?: throw ClientServiceException("Received null id in clientDTO from Kafka for $op operation")
                )
                else -> throw ClientServiceException("No found operation")
            }
        } catch (e: ClientServiceException) {
            kafkaProducerService.sendResponse(ResponseDTO(e.message))
        }
        kafkaProducerService.sendResponse(ResponseDTO("OK"))
    }
}
