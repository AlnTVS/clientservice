package com.alntvs.clientservice.util

import org.springframework.stereotype.Component

@Component
class EnvProp() {
    val consumerTopic: String
        get() = System.getenv("BELL_CONSUMER_T")?:"client_service_topic"
    val producerTopic: String
        get() = System.getenv("BELL_PRODUCER_T")?:"service_client_topic"
    val consumerId: String
        get() = System.getenv("BELL_CONSUMER_ID")?:"ClientService"
}
