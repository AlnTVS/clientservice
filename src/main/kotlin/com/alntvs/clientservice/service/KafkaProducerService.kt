package com.alntvs.clientservice.service

import com.alntvs.clientservice.model.ResponseDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducerService(
    private val kafkaTemplate: KafkaTemplate<String, ResponseDTO>,
    @Value("\${spring.kafka.producer.topic}")
    val topic: String
) {

    init {
        kafkaTemplate.defaultTopic = topic
    }

    fun sendResponse(msg: ResponseDTO) {
        kafkaTemplate.sendDefault(msg)
    }

}