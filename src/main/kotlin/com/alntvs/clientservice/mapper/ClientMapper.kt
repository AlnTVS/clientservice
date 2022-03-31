package com.alntvs.clientservice.mapper

import com.alntvs.clientservice.entity.ClientEntity
import com.alntvs.clientservice.model.ClientDTO
import org.mapstruct.Mapper

//mapstruct spring Без impl
@Mapper
interface ClientMapper {
    fun clientEntityToDTO(clientEntity: ClientEntity): ClientDTO
    fun clientDTOToEntity(clientDTO: ClientDTO): ClientEntity
    fun clientEntityToDTOasList(list: List<ClientEntity>): List<ClientDTO>
}