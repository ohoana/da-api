package com.globits.da.service;

import com.globits.da.dto.CertificateMapDto;
import com.globits.da.exception.InvalidInputException;

import java.util.List;
import java.util.UUID;

public interface CertificateMapService {
    List<CertificateMapDto> getAll();
    List<CertificateMapDto> getByEmployeeId(UUID id);
    CertificateMapDto save(CertificateMapDto conferringDto) throws InvalidInputException;
    CertificateMapDto update(CertificateMapDto conferringDto, UUID id) throws InvalidInputException;
    Boolean deleteById(UUID id);
}
