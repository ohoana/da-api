package com.globits.da.service;

import com.globits.da.dto.TownDto;
import com.globits.da.exception.InvalidInputException;

import java.util.List;
import java.util.UUID;

public interface TownService {
    TownDto getById(UUID id);
    List<TownDto> getAll();
    List<TownDto> getByDistrictId(UUID id);
    TownDto save(TownDto dto) throws InvalidInputException;
    TownDto update(TownDto dto, UUID id) throws InvalidInputException;
    Boolean delete(UUID id);
    Boolean isValidDto(TownDto dto);
}
