package com.globits.da.service;

import com.globits.da.dto.ReleaseInforDto;

import java.util.List;
import java.util.UUID;

public interface ReleaseInforService {
    List<ReleaseInforDto> getAll();
    List<ReleaseInforDto> getByEmployeeId(UUID id);

    ReleaseInforDto saveOrUpdate(ReleaseInforDto conferringDto, UUID id);

    Boolean deleteById(UUID id);

    Boolean isValidDto(ReleaseInforDto conferringDto);
}
