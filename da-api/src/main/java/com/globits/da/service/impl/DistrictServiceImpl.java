package com.globits.da.service.impl;

import com.globits.da.commons.ApiMessageError;
import com.globits.da.consts.MessageConst;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.DistrictDto;
import com.globits.da.exception.InvalidDtoException;
import com.globits.da.exception.InvalidInputException;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.DistrictService;
import com.globits.da.utils.InjectParam;
import com.globits.da.validator.marker.OnUpdate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

@Service
public class DistrictServiceImpl implements DistrictService {
    private final DistrictRepository districtRepository;
    private final ProvinceRepository provinceRepository;
    private final Validator validator;
    private final InjectParam injectParam;

    public DistrictServiceImpl(DistrictRepository districtRepository, ProvinceRepository provinceRepository, Validator validator, InjectParam injectParam) {
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
        this.validator = validator;
        this.injectParam = injectParam;
    }

    @Override
    public DistrictDto getById(UUID id) {
        District district = districtRepository.getOne(id);
        return new DistrictDto(district);
    }

    @Override
    public List<DistrictDto> getAll() {
        return districtRepository.getAll();
    }

    @Override
    public List<DistrictDto> getByProvinceId(UUID provinceId) {
        return districtRepository.findByProvinceId(provinceId);
    }

    @Override
    public DistrictDto save(DistrictDto dto) throws InvalidInputException {
        if(ObjectUtils.isEmpty(dto)) {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.DTO_NOT_NULL)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
        District district = new District();
        Province province = provinceRepository.getOne(dto.getProvinceId());
        injectParam.setDistrictValue(district, dto, province);
        district = districtRepository.save(district);
        return new DistrictDto(district);
    }

    @Override
    public DistrictDto update(DistrictDto dto, UUID id) throws InvalidInputException {
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
        Optional<District> districtOpt = districtRepository.findById(dto.getId());
        if(districtOpt.isPresent()) {
            District district = districtOpt.get();
            Province province = !ObjectUtils.isEmpty(dto.getProvinceId())
                    ? provinceRepository.getOne(dto.getProvinceId())
                    : null;
            injectParam.setDistrictValue(district, dto, province);
            district = districtRepository.save(district);
            return new DistrictDto(district);
        } else {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.NOT_FOUND)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
    }

    @Override
    public Boolean delete(UUID id) {
        if(!ObjectUtils.isEmpty(id) && districtRepository.existsById(id)) {
            districtRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean isValidDto(DistrictDto dto) {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<DistrictDto>> violations = validator.validate(dto, OnUpdate.class);
        if(!violations.isEmpty()) {
            for(ConstraintViolation<DistrictDto> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }
        if(!ObjectUtils.isEmpty(dto.getProvinceId()) &&
                !provinceRepository.existsById(dto.getProvinceId())) {
            errors.put("Province", MessageConst.NOT_FOUND);
        }
        if(!errors.isEmpty()) {
            throw new InvalidDtoException(errors);
        }
        return true;
    }
}
