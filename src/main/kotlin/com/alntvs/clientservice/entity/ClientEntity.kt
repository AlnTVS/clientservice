package com.alntvs.clientservice.entity

import javax.persistence.*

@Entity
@Table(name = "CLIENT")
data class ClientEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id : Long? = null,

    @Column(name = "USERNAME", nullable = false)
    var userName : String? = null,

    @Column(name = "FIRST_NAME")
    var firstName : String? = null,

    @Column(name = "LAST_NAME")
    var lastName : String? = null,

    @Column(name = "EMAIL")
    var email : String? = null,

    @Column(name = "MOBILE_PHONE", length = 15)
    var mobilePhone : String? = null,

    @Column(name = "ADDRESS")
    var address : String? = null,

    @Column(name = "IS_ACTIVE")
    var isActive : Boolean? = null
)