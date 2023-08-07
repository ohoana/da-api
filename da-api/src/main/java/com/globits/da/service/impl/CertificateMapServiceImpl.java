package com.globits.da.service.impl;

import com.globits.da.domain.Certificate;
import com.globits.da.domain.CertificateMap;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.CertificateMapDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.repository.CertificateMapRepository;
import com.globits.da.service.CertificateMapService;
import com.globits.da.utils.exception.InvalidDtoException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CertificateMapServiceImpl implements CertificateMapService {
    private final CertificateMapRepository certificateMapRepository;
    private final EmployeeRepository employeeRepository;
    private final ProvinceRepository provinceRepository;
    private final CertificateRepository certificateRepository;

    public CertificateMapServiceImpl(CertificateMapRepository certificateMapRepository,
                                     EmployeeRepository employeeRepository,
                                     ProvinceRepository provinceRepository,
                                     CertificateRepository certificateRepository) {
        this.certificateMapRepository = certificateMapRepository;
        this.employeeRepository = employeeRepository;
        this.provinceRepository = provinceRepository;
        this.certificateRepository = certificateRepository;
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
    public CertificateMapDto saveOrUpdate(CertificateMapDto dto, UUID id) {
        if(!ObjectUtils.isEmpty(dto)) {
            CertificateMap certificateMap = null;
            if(!ObjectUtils.isEmpty(id)) {
                if(!ObjectUtils.isEmpty(dto.getId()) && !id.equals(dto.getId())) {
                    return null;
                }
                certificateMap = certificateMapRepository.getOne(id);
            }

            if(ObjectUtils.isEmpty(certificateMap)) {
                certificateMap = new CertificateMap();
            }
            try {
                Employee employee = employeeRepository.getOne(dto.getEmployeeId());
                Certificate certificate = certificateRepository.getOne(dto.getCertificateId());
                Province province = provinceRepository.getOne(dto.getProvinceId());

                certificateMap.setEmployee(employee);
                certificateMap.setProvince(province);
                certificateMap.setCertificate(certificate);


                certificateMap.setBeginDate(dto.getBeginDate());
                certificateMap.setExpireDate(dto.getExpireDate());

                certificateMap = certificateMapRepository.save(certificateMap);

                if(!ObjectUtils.isEmpty(certificateMap)) {
                    return new CertificateMapDto(certificateMap);
                }
            } catch (EntityNotFoundException e) {
                Map<String, String> errors = new HashMap<>();
                errors.put("Certificate Map", "Not found!");
                throw new InvalidDtoException(errors);
            }
        }
        return null;
    }

    @Override
    public Boolean deleteById(UUID id) {
        if(!ObjectUtils.isEmpty(id)) {
            certificateMapRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean isValidDto(CertificateMapDto dto) {

        Map<String, String> errors = new HashMap<>();

        if(!ObjectUtils.isEmpty(dto.getEmployeeId())
                && !employeeRepository.existsById(dto.getEmployeeId())) {
            errors.put("Employee", "Not found!");
        }
        if(!ObjectUtils.isEmpty(dto.getCertificateId())
                && !certificateRepository.existsById(dto.getCertificateId())) {
            errors.put("Certificate", "Not found!");
        }
        if(!ObjectUtils.isEmpty(dto.getProvinceId())
                && !provinceRepository.existsById(dto.getProvinceId())) {
            errors.put("Province", "Not found!");
        }

        int numOfCertificateInUse =
                certificateMapRepository.countCertificateInUse(dto.getEmployeeId(), dto.getCertificateId());
        if(numOfCertificateInUse >= 3) {
            errors.put("Certificate", "Must not have over 3 certificate same type");
        }
        if(!ObjectUtils.isEmpty(certificateMapRepository
                .getCertificateInUseByProvinceId(dto.getEmployeeId(),
                        dto.getCertificateId(),
                        dto.getProvinceId()))) {
            errors.put("Certificate", "Must not have more certificate same type confer by same Province");
        }

        if(!errors.isEmpty()) {
            throw new InvalidDtoException(errors);
        }
        return true;
    }
}
