package com.alntvs.clientservice.service

import com.alntvs.clientservice.model.ResponseDTO
import com.alntvs.clientservice.util.EnvProp
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducerService(private val kafkaTemplate: KafkaTemplate<String, ResponseDTO>, private val envProp: EnvProp) {

    fun sendResponse(msg: ResponseDTO) {
        kafkaTemplate.send(envProp.producerTopic, msg)
    }

}