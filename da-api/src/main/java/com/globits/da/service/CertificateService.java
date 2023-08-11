package com.globits.da.service;

import com.globits.da.dto.CertificateDto;
import com.globits.da.exception.InvalidInputException;

import java.util.List;
import java.util.UUID;

public interface CertificateService {
    CertificateDto getById(UUID id);
    List<CertificateDto> getAll();
    CertificateDto save(CertificateDto dto) throws InvalidInputException;
    CertificateDto update(CertificateDto dto, UUID id) throws InvalidInputException;
    Boolean delete(UUID id);
}
