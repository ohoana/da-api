package com.globits.da.service;

import com.globits.da.dto.ConferringDto;

import java.util.List;
import java.util.UUID;

public interface ConferringService {
    List<ConferringDto> getAll();

    ConferringDto saveOrUpdate(ConferringDto conferringDto, UUID id);
}