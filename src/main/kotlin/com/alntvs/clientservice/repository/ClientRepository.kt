package com.alntvs.clientservice.repository

import com.alntvs.clientservice.entity.ClientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository: JpaRepository<ClientEntity,Long>, JpaSpecificationExecutor<ClientEntity>