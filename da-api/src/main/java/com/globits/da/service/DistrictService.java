package com.globits.da.service;

import com.globits.da.dto.DistrictDto;
import com.globits.da.exception.InvalidInputException;

import java.util.List;
import java.util.UUID;

public interface DistrictService {
    DistrictDto getById(UUID id);
    List<DistrictDto> getAll();
    List<DistrictDto> getByProvinceId(UUID id);
    DistrictDto save(DistrictDto dto) throws InvalidInputException;
    DistrictDto update(DistrictDto dto, UUID id) throws InvalidInputException;
    Boolean delete(UUID id);
    Boolean isValidDto(DistrictDto dto);
}
