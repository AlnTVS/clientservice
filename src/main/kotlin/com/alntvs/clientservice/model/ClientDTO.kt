package com.alntvs.clientservice.model

data class ClientDTO(
    var id: Long? = null,
    var userName: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var mobilePhone: String? = null,
    var address: String? = null,
    var isActive: Boolean? = null
)