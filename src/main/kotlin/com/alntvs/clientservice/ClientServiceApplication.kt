package com.alntvs.clientservice

import liquibase.integration.spring.SpringLiquibase
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@SpringBootApplication
class ClientServiceApplication

fun main(args: Array<String>) {
	runApplication<ClientServiceApplication>(*args)
}
