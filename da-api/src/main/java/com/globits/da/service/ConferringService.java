package com.globits.da.service;

import com.globits.da.dto.ConferringDto;

import java.util.List;
import java.util.UUID;

public interface ConferringService {
    List<ConferringDto> getAll();
    List<ConferringDto> getByEmployeeId(UUID id);

    ConferringDto saveOrUpdate(ConferringDto conferringDto, UUID id);

    Boolean deleteById(UUID id);

    Boolean isValidDto(ConferringDto conferringDto);
}
