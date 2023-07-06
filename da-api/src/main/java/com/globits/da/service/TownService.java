package com.globits.da.service;

import com.globits.da.domain.District;
import com.globits.da.domain.Town;
import com.globits.da.dto.TownDto;

import java.util.List;
import java.util.UUID;

public interface TownService {
    TownDto getById(UUID id);
    List<TownDto> getAll();
    List<TownDto> getByDistrictId(UUID id);
    TownDto saveOrUpdate(TownDto dto, UUID id);
    List<Town> saveOrUpdateList(List<TownDto> dtos, District district);
    Boolean delete(UUID id);
    Boolean isValidDto(TownDto dto);
}
