package com.globits.da.service;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.DistrictDto;

import java.util.List;
import java.util.UUID;

public interface DistrictService {

    DistrictDto getById(UUID id);
    List<DistrictDto> getAll();

    List<DistrictDto> getByProvinceId(UUID provinceId);
    DistrictDto saveOrUpdate(DistrictDto dto, UUID id);

    List<District> saveOrUpdateList(List<DistrictDto> dtos, Province province);

    Boolean delete(UUID id);
}
