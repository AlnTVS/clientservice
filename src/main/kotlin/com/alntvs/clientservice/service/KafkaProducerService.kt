package com.alntvs.clientservice.service

import com.alntvs.clientservice.model.ResponseDTO
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducerService(private val kafkaTemplate: KafkaTemplate<String, ResponseDTO>) {

    fun sendResponse(msg: ResponseDTO) {
        kafkaTemplate.send("service_client_topic", msg)
        return
    }

}