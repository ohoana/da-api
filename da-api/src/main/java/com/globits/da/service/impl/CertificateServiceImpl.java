package com.globits.da.service.impl;

import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateServiceImpl implements CertificateService {
    @Autowired
    private CertificateRepository repository;

    @Override
    public CertificateDto getById(UUID id) {
        Certificate certificate = repository.getOne(id);
        return new CertificateDto(certificate);
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
                try {
                    certificate = repository.getOne(id);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            if(ObjectUtils.isEmpty(certificate)) {
                certificate = new Certificate();
            }

            certificate.setName(dto.getName());

            certificate = repository.save(certificate);
            if(!ObjectUtils.isEmpty(certificate)) {
                return new CertificateDto(certificate);
            }
        }
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        if(id != null) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}