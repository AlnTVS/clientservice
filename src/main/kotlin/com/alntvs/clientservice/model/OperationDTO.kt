package com.alntvs.clientservice.model

import java.io.Serializable

enum class Operation {
    CREATE,
    READ_BY_ID,
    READ_ALL,
    UPDATE,
    DELETE;
}

data class OperationDTO(
    val operation: Operation?,
    val clientDTO: ClientDTO?
): Serializable