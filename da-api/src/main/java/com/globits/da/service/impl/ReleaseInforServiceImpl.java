package com.globits.da.service.impl;

import com.globits.da.domain.Certificate;
import com.globits.da.domain.ReleaseInfor;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.ReleaseInforDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.ReleaseInforRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.ReleaseInforService;
import com.globits.da.utils.exception.InvalidDtoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ReleaseInforServiceImpl implements ReleaseInforService {

    @Autowired
    private ReleaseInforRepository releaseInforRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public List<ReleaseInforDto> getAll() {
        return releaseInforRepository.getAll();
    }

    @Override
    public List<ReleaseInforDto> getByEmployeeId(UUID id) {
        return releaseInforRepository.getByEmployeeId(id);
    }

    @Override
    public ReleaseInforDto saveOrUpdate(ReleaseInforDto dto, UUID id) {
        if(!ObjectUtils.isEmpty(dto)) {
            ReleaseInfor releaseInfor = null;
            if(!ObjectUtils.isEmpty(id)) {
                if(!ObjectUtils.isEmpty(dto.getId()) && !id.equals(dto.getId())) {
                    return null;
                }
                releaseInfor = releaseInforRepository.getOne(id);
            }

            if(ObjectUtils.isEmpty(releaseInfor)) {
                releaseInfor = new ReleaseInfor();
            }
            try {
                Employee employee = employeeRepository.getOne(dto.getEmployeeId());
                Certificate certificate = certificateRepository.getOne(dto.getCertificateId());
                Province province = provinceRepository.getOne(dto.getProvinceId());

                releaseInfor.setEmployee(employee);
                releaseInfor.setProvince(province);
                releaseInfor.setCertificate(certificate);


                releaseInfor.setBeginDate(dto.getBeginDate());
                releaseInfor.setExpireDate(dto.getExpireDate());

                releaseInfor = releaseInforRepository.save(releaseInfor);

                if(!ObjectUtils.isEmpty(releaseInfor)) {
                    return new ReleaseInforDto(releaseInfor);
                }
            } catch (EntityNotFoundException e) {
                Map<String, String> errors = new HashMap<>();
                errors.put("Release Infor", "Not found!");
            }
        }
        return null;
    }

    @Override
    public Boolean deleteById(UUID id) {
        if(!ObjectUtils.isEmpty(id)) {
            releaseInforRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean isValidDto(ReleaseInforDto dto) {

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
                releaseInforRepository.countCertificateInUse(dto.getEmployeeId(), dto.getCertificateId());
        if(numOfCertificateInUse >= 3) {
            errors.put("Certificate", "Must not have over 3 certificate same type");
        }
        if(!ObjectUtils.isEmpty(releaseInforRepository
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
