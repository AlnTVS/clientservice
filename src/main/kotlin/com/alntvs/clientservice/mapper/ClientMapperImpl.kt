package com.alntvs.clientservice.mapper

import com.alntvs.clientservice.entity.ClientEntity
import com.alntvs.clientservice.model.ClientDTO
import org.springframework.stereotype.Component

@Component
class ClientMapperImpl: ClientMapper {
    override fun clientEntityToDTO(clientEntity: ClientEntity) = ClientDTO(
        clientEntity.id,
        clientEntity.userName,
        clientEntity.firstName,
        clientEntity.lastName,
        clientEntity.email,
        clientEntity.mobilePhone,
        clientEntity.address,
        clientEntity.isActive
        )

    override fun clientDTOToEntity(clientDTO: ClientDTO) = ClientEntity(
        clientDTO.id,
        clientDTO.userName,
        clientDTO.firstName,
        clientDTO.lastName,
        clientDTO.email,
        clientDTO.mobilePhone,
        clientDTO.address,
        clientDTO.isActive
        )

    override fun clientEntityToDTOasList(list: List<ClientEntity>): List<ClientDTO> {
        var resultList = ArrayList<ClientDTO>()
        for (entity in list) {
            resultList.add(clientEntityToDTO(entity))
        }
        return resultList
    }
}