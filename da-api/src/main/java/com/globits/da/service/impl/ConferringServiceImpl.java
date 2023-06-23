package com.globits.da.service.impl;

import com.globits.da.domain.Certificate;
import com.globits.da.domain.Conferring;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.ConferringDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.ConferringRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.ConferringService;
import com.globits.da.utils.exception.InvalidDtoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ConferringServiceImpl implements ConferringService {

    @Autowired
    private ConferringRepository conferringRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public List<ConferringDto> getAll() {
        return conferringRepository.getAll();
    }

    @Override
    public List<ConferringDto> getByEmployeeId(UUID id) {
        return conferringRepository.getByEmployeeId(id);
    }

    @Override
    public ConferringDto saveOrUpdate(ConferringDto conferringDto, UUID id) {
        if(!ObjectUtils.isEmpty(conferringDto)) {
            Conferring conferring = null;
            if(!ObjectUtils.isEmpty(id)) {
                if(!ObjectUtils.isEmpty(conferringDto.getId()) && !id.equals(conferringDto.getId())) {
                    return null;
                }
                try {
                    conferring = conferringRepository.getOne(id);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            if(ObjectUtils.isEmpty(conferring)) {
                if(!isValidDto(conferringDto)) {
                    return null;
                }
                conferring = new Conferring();
            }
            try {
                Employee employee = employeeRepository.getOne(conferringDto.getEmployeeId());
                Certificate certificate = certificateRepository.getOne(conferringDto.getCertificateId());
                Province province = provinceRepository.getOne(conferringDto.getProvinceId());

                conferring.setEmployee(employee);
                conferring.setProvince(province);
                conferring.setCertificate(certificate);
            } catch (EntityNotFoundException | InvalidParameterException e) {
                e.printStackTrace();
            }

            conferring.setBeginDate(conferringDto.getBeginDate());
            conferring.setExpireDate(conferringDto.getExpireDate());

            conferring = conferringRepository.save(conferring);

            if(!ObjectUtils.isEmpty(conferring)) {
                return new ConferringDto(conferring);
            }
        }
        return null;
    }

    @Override
    public Boolean deleteById(UUID id) {
        if(!ObjectUtils.isEmpty(id)) {
            conferringRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean isValidDto(ConferringDto conferringDto) {

        Map<String, String> errors = new HashMap<>();
        int numOfCertificateInUse =
                conferringRepository.countCertificateInUse(conferringDto.getEmployeeId(), conferringDto.getCertificateId());
        if(numOfCertificateInUse >= 3) {
            errors.put("Certificate", "Must not have over 3 certificate same type");
            throw new InvalidDtoException(errors);
        }
        if(!ObjectUtils.isEmpty(conferringRepository
                .getCertificateInUseByProvinceId(conferringDto.getEmployeeId(),
                        conferringDto.getCertificateId(),
                        conferringDto.getProvinceId()))) {
            errors.put("Certificate", "Must not have more certificate same type confer by same Province");
            throw new InvalidDtoException(errors);
        }
        return true;
    }
}
