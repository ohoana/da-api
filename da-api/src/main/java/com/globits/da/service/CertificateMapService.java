package com.globits.da.service;

import com.globits.da.dto.CertificateMapDto;

import java.util.List;
import java.util.UUID;

public interface CertificateMapService {
    List<CertificateMapDto> getAll();
    List<CertificateMapDto> getByEmployeeId(UUID id);
    CertificateMapDto saveOrUpdate(CertificateMapDto conferringDto, UUID id);
    Boolean deleteById(UUID id);
    Boolean isValidDto(CertificateMapDto conferringDto);
}
