package com.globits.da.service.impl;

import com.globits.da.commons.ApiMessageError;
import com.globits.da.commons.ApiValidatorError;
import com.globits.da.consts.MessageConst;
import com.globits.da.domain.CertificateMap;
import com.globits.da.dto.CertificateMapDto;
import com.globits.da.exception.DataNotFoundException;
import com.globits.da.exception.InvalidInputException;
import com.globits.da.repository.CertificateMapRepository;
import com.globits.da.service.CertificateMapService;
import com.globits.da.utils.InjectParam;
import com.globits.da.validation.ValidationCertificateMap;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificateMapServiceImpl implements CertificateMapService {
    private final CertificateMapRepository certificateMapRepository;
    private final InjectParam injectParam;
    private final ValidationCertificateMap validationCertificateMap;

    public CertificateMapServiceImpl(CertificateMapRepository certificateMapRepository,
                                     InjectParam injectParam, ValidationCertificateMap validationCertificateMap) {
        this.certificateMapRepository = certificateMapRepository;
        this.injectParam = injectParam;
        this.validationCertificateMap = validationCertificateMap;
    }

    @Override
    public List<CertificateMapDto> getAll() {
        return certificateMapRepository.getAll();
    }

    @Override
    public List<CertificateMapDto> getByEmployeeId(UUID id) {
        return certificateMapRepository.getByEmployeeId(id);
    }

    @Override
    public CertificateMapDto save(CertificateMapDto dto) throws InvalidInputException {
        if(ObjectUtils.isEmpty(dto)) {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.DTO_NOT_NULL)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
        CertificateMap certificateMap = new CertificateMap();
        validationCertificateMap.checkValidCertificateMap(dto);
        injectParam.setCertificateMapValue(certificateMap, dto);
        certificateMap = certificateMapRepository.save(certificateMap);
        return new CertificateMapDto(certificateMap);
    }

    @Override
    public CertificateMapDto update(CertificateMapDto dto, UUID id) throws InvalidInputException {
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
        Optional<CertificateMap> certificateMapOptional = certificateMapRepository.findById(dto.getId());
        if(certificateMapOptional.isPresent()) {
            CertificateMap certificateMap = certificateMapOptional.get();
            validationCertificateMap.checkValidCertificateMap(dto);
            injectParam.setCertificateMapValue(certificateMap, dto);
            certificateMap = certificateMapRepository.save(certificateMap);
            return new CertificateMapDto(certificateMap);
        } else {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("Certificate Mapping")
                    .message(MessageConst.ENTITY_NOT_FOUND)
                    .build();
            throw new DataNotFoundException(apiValidatorError);
        }
    }

    @Override
    public Boolean deleteById(UUID id) {
        if(!ObjectUtils.isEmpty(id) && certificateMapRepository.existsById(id)) {
            certificateMapRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
