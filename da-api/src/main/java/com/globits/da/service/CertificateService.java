package com.globits.da.service;

import com.globits.da.dto.CertificateDto;

import java.util.List;
import java.util.UUID;

public interface CertificateService {

    CertificateDto getById(UUID id);

    List<CertificateDto> getAll();

    CertificateDto saveOrUpdate(CertificateDto dto, UUID id);

    Boolean delete(UUID id);
}
