package com.globits.da.service;

import com.globits.da.dto.ProvinceDto;

import java.util.List;
import java.util.UUID;

public interface ProvinceService {
    ProvinceDto getById(UUID id);
    List<ProvinceDto> getAll();
    ProvinceDto saveOrUpdate(ProvinceDto dto, UUID id);
    Boolean delete(UUID id);
}
