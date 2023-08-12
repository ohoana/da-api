package com.globits.da.validation;

import com.globits.da.commons.ApiValidatorError;
import com.globits.da.consts.MessageConst;
import com.globits.da.dto.CertificateMapDto;
import com.globits.da.exception.DataNotFoundException;
import com.globits.da.exception.InvalidInputException;
import com.globits.da.repository.CertificateMapRepository;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Component
public class ValidationCertificateMap {
    private final EmployeeRepository employeeRepository;
    private final CertificateRepository certificateRepository;
    private final ProvinceRepository provinceRepository;
    private final CertificateMapRepository certificateMapRepository;

    public ValidationCertificateMap(EmployeeRepository employeeRepository, CertificateRepository certificateRepository, ProvinceRepository provinceRepository, CertificateMapRepository certificateMapRepository) {
        this.employeeRepository = employeeRepository;
        this.certificateRepository = certificateRepository;
        this.provinceRepository = provinceRepository;
        this.certificateMapRepository = certificateMapRepository;
    }

    private void checkEmployeeIdValid(UUID id) {
        if(!ObjectUtils.isEmpty(id)
                && !employeeRepository.existsById(id)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("Employee")
                    .message(MessageConst.ENTITY_NOT_FOUND)
                    .build();
            throw new DataNotFoundException(apiValidatorError);
        }
    }

    private void checkCertificateIdValid(UUID id) {
        if(!ObjectUtils.isEmpty(id)
                && !certificateRepository.existsById(id)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("Certificate")
                    .message(MessageConst.ENTITY_NOT_FOUND)
                    .build();
            throw new DataNotFoundException(apiValidatorError);
        }
    }

    private void checkProvinceIdValid(UUID id) {
        if(!ObjectUtils.isEmpty(id)
                && !provinceRepository.existsById(id)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("Province")
                    .message(MessageConst.ENTITY_NOT_FOUND)
                    .build();
            throw new DataNotFoundException(apiValidatorError);
        }
    }

    private void checkLimitCertificate(UUID employeeId, UUID certificateId) throws InvalidInputException {
        int numOfCertificateInUse =
                certificateMapRepository.countCertificateInUse(employeeId, certificateId);
        if(numOfCertificateInUse >= 3) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("Certificate")
                    .message(MessageConst.CERTIFICATE_LIMIT_ERROR)
                    .build();
            throw new InvalidInputException(apiValidatorError);
        }
    }

    private void checkTypeCertificate(UUID employeeId, UUID certificateId, UUID provinceId) {
        if(!ObjectUtils.isEmpty(certificateMapRepository
                .getCertificateInUseByProvinceId(employeeId,
                        certificateId,
                        provinceId))) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("Certificate")
                    .message(MessageConst.CERTIFICATE_SAME_TYPE_ERROR)
                    .build();
            throw new DataNotFoundException(apiValidatorError);
        }
    }

    public void checkValidCertificateMap(CertificateMapDto dto) throws InvalidInputException {
        checkEmployeeIdValid(dto.getEmployeeId());
        checkProvinceIdValid(dto.getProvinceId());
        checkCertificateIdValid(dto.getCertificateId());
        checkLimitCertificate(dto.getEmployeeId(), dto.getCertificateId());
        checkTypeCertificate(dto.getEmployeeId(), dto.getCertificateId(), dto.getProvinceId());
    }
}
