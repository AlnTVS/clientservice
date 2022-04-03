package com.alntvs.clientservice.service

import com.alntvs.clientservice.model.ClientDTO
import com.alntvs.clientservice.model.Operation.*
import com.alntvs.clientservice.model.OperationDTO
import com.alntvs.clientservice.model.ResponseDTO
import com.alntvs.clientservice.util.EnvProp
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumerService(
    private val clientService: ClientService,
    private val kafkaProducerService: KafkaProducerService,
    val envProp: EnvProp
) {

    @KafkaListener(id = "#{__listener.envProp.consumerId}", topics = ["#{__listener.envProp.consumerTopic}"])
    fun webConsumer(msg: OperationDTO) {
        var failed = false
        val answerError = { response: String ->
            failed = true
            kafkaProducerService.sendResponse(ResponseDTO(response))
        }
        val operation = msg.operation ?: answerError("Received operation is null!")
        val ifNullAnswerError = { any: Any?, paramName: String ->
            any ?: answerError("Parameter '$paramName' must not be null! Operation: '$operation'.")
        }
        try {
            when (msg.operation) {
                UPDATE -> clientService.update(ifNullAnswerError(msg.clientDTO, "clientDTO") as ClientDTO)
                CREATE -> clientService.create(ifNullAnswerError(msg.clientDTO, "clientDTO") as ClientDTO)
                DELETE -> clientService.delete(ifNullAnswerError(msg.clientDTO?.id, "id") as Long)
            }
        } catch (e: RuntimeException) {
            answerError(e.message?:"Unknown error")
        }
        if(!failed) kafkaProducerService.sendResponse(ResponseDTO("OK"))
    }

}
