package com.alntvs.clientservice.service

import com.alntvs.clientservice.model.Operation.*
import com.alntvs.clientservice.model.OperationDTO
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumerService(private val clientService: ClientService) {

    @KafkaListener(id = "ClientService", topics = ["client_service_topic"])
    fun webConsumer(msg: OperationDTO) {
        msg.operation?.also {
            when (it) {
                READ_BY_ID -> clientService.getAll()
                else -> {
                    msg.clientDTO
                        ?: throw IllegalArgumentException("Received null clientDTO from Kafka for $it operation")
                    when (it) {
                        CREATE -> clientService.create(msg.clientDTO)
                        READ_BY_ID -> clientService.getById(msg.clientDTO.id
                            ?: throw IllegalArgumentException("Received null id in clientDTO from Kafka for $it operation"))
                        UPDATE -> clientService.update(msg.clientDTO)
                        DELETE -> clientService.delete(msg.clientDTO.id
                            ?: throw IllegalArgumentException("Received null id in clientDTO from Kafka for $it operation"))
                    }
                }
            }
        }
    }
}