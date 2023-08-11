package com.globits.da.service.impl;

import com.globits.da.commons.ApiMessageError;
import com.globits.da.commons.ApiValidatorError;
import com.globits.da.consts.MessageConst;
import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.exception.DataNotFoundException;
import com.globits.da.exception.InvalidInputException;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.service.CertificateService;
import com.globits.da.utils.InjectParam;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository repository;
    private final InjectParam injectParam;
    public CertificateServiceImpl(CertificateRepository repository, InjectParam injectParam) {
        this.repository = repository;
        this.injectParam = injectParam;
    }

    @Override
    public CertificateDto getById(UUID id) {

        Optional<Certificate> certificateOpt = repository.findById(id);
        if(certificateOpt.isPresent()) {
            return new CertificateDto(certificateOpt.get());
        } else {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("id")
                    .message(MessageConst.ENTITY_NOT_FOUND)
                    .build();
            throw new DataNotFoundException(apiValidatorError);
        }
    }

    @Override
    public List<CertificateDto> getAll() {
        return repository.getAll();
    }

    @Override
    public CertificateDto save(CertificateDto dto) throws InvalidInputException {
        if(ObjectUtils.isEmpty(dto)) {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.DTO_NOT_NULL)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
        Certificate certificate = new Certificate();
        injectParam.setCertificateValue(certificate, dto);
        certificate = repository.save(certificate);
        return new CertificateDto(certificate);
    }

    @Override
    public CertificateDto update(CertificateDto dto, UUID id) throws InvalidInputException {
        if(ObjectUtils.isEmpty(dto)) {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.DTO_NOT_NULL)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
        if(!id.equals(dto.getId())) {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.ID_UPDATE_NOT_MATCH)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
        Optional<Certificate> certificateOptional = repository.findById(dto.getId());
        if(certificateOptional.isPresent()) {
            Certificate certificate = certificateOptional.get();
            injectParam.setCertificateValue(certificate, dto);
            certificate = repository.save(certificate);
            return new CertificateDto(certificate);
        } else {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("Certificate")
                    .message(MessageConst.ENTITY_NOT_FOUND)
                    .build();
            throw new DataNotFoundException(apiValidatorError);
        }
    }

    @Override
    public Boolean delete(UUID id) {
        if(!ObjectUtils.isEmpty(id) && repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
