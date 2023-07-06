package com.globits.da.service.impl;

import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.service.CertificateService;
import com.globits.da.utils.exception.InvalidDtoException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository repository;

    public CertificateServiceImpl(CertificateRepository repository) {
        this.repository = repository;
    }

    @Override
    public CertificateDto getById(UUID id) {
        try {
            Certificate certificate = repository.getOne(id);
            return new CertificateDto(certificate);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @Override
    public List<CertificateDto> getAll() {
        return repository.getAll();
    }

    @Override
    public CertificateDto saveOrUpdate(CertificateDto dto, UUID id) {
        if(!ObjectUtils.isEmpty(dto)) {
            Certificate certificate = null;
            if(!ObjectUtils.isEmpty(id)) {
                if(!ObjectUtils.isEmpty(dto.getId()) && !id.equals(dto.getId())) {
                    return null;
                }
                certificate = repository.getOne(id);
            }
            if(ObjectUtils.isEmpty(certificate)) {
                certificate = new Certificate();
            }
            try {
                certificate.setName(dto.getName());
                certificate = repository.save(certificate);
                if(!ObjectUtils.isEmpty(certificate)) {
                    return new CertificateDto(certificate);
                }
            } catch (EntityNotFoundException e) {
                Map<String, String> errors = new HashMap<>();
                errors.put("Certificate", "Not found!");
                throw new InvalidDtoException(errors);
            }
        }
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        if(!ObjectUtils.isEmpty(id)) {
            try {
                repository.deleteById(id);
                return true;
            } catch (EmptyResultDataAccessException e) {
                return false;
            }
        }
        return false;
    }
}
