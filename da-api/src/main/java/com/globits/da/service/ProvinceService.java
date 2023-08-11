package com.globits.da.service;

import com.globits.da.dto.ProvinceDto;
import com.globits.da.exception.InvalidInputException;

import java.util.List;
import java.util.UUID;

public interface ProvinceService {
    ProvinceDto getById(UUID id);
    List<ProvinceDto> getAll();
    ProvinceDto save(ProvinceDto dto) throws InvalidInputException;
    ProvinceDto update(ProvinceDto dto, UUID id) throws InvalidInputException;
    Boolean delete(UUID id);
}
