package com.alntvs.clientservice.service

import com.alntvs.clientservice.model.ResponseDTO
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFuture

internal class KafkaProducerServiceTest {

    private val kafkaTemplate = mockk<KafkaTemplate<String, ResponseDTO>>()
    private val topic = "testTopic"

    @Test
    fun sendResponse() {
        justRun { kafkaTemplate.defaultTopic = topic }
        val kafkaProducerService = KafkaProducerService(
            kafkaTemplate,
            topic
        )
        val msg = ""
        val responseDTO = ResponseDTO(msg)
        val result = mockk<ListenableFuture<SendResult<String, ResponseDTO>>>()

        every { kafkaTemplate.sendDefault(responseDTO) } returns result

        kafkaProducerService.sendResponse(responseDTO)

        verify { kafkaTemplate.sendDefault(responseDTO) }
    }
}