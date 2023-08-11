package com.globits.da.service.impl;

import com.globits.da.commons.ApiMessageError;
import com.globits.da.consts.MessageConst;
import com.globits.da.domain.District;
import com.globits.da.domain.Town;
import com.globits.da.dto.TownDto;
import com.globits.da.exception.InvalidInputException;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.TownRepository;
import com.globits.da.service.TownService;
import com.globits.da.exception.InvalidDtoException;
import com.globits.da.utils.InjectParam;
import com.globits.da.validator.marker.OnUpdate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final DistrictRepository districtRepository;
    private final Validator validator;
    private final InjectParam injectParam;

    public TownServiceImpl(TownRepository townRepository,
                           DistrictRepository districtRepository,
                           Validator validator, InjectParam injectParam) {
        this.townRepository = townRepository;
        this.districtRepository = districtRepository;
        this.validator = validator;
        this.injectParam = injectParam;
    }

    @Override
    public TownDto getById(UUID id) {
        try {
            Town town = townRepository.getOne(id);
            return new TownDto(town);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @Override
    public List<TownDto> getAll() {
        return townRepository.getAll();
    }

    @Override
    public List<TownDto> getByDistrictId(UUID districtId) {
        return townRepository.getByDistrictId(districtId);
    }

    @Override
    public TownDto save(TownDto dto) throws InvalidInputException {
        if(ObjectUtils.isEmpty(dto)) {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.DTO_NOT_NULL)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
        Town town = new Town();
        District district = districtRepository.getOne(dto.getDistrictId());
        injectParam.setTownValue(town, dto, district);
        town = townRepository.save(town);
        return new TownDto(town);
    }

    @Override
    public TownDto update(TownDto dto, UUID id) throws InvalidInputException {
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
        Optional<Town> townOpt = townRepository.findById(dto.getId());
        if(townOpt.isPresent()) {
            Town town = townOpt.get();
            District district = ObjectUtils.isEmpty(dto.getDistrictId())
                    ? districtRepository.getOne(dto.getDistrictId())
                    : null;
            injectParam.setTownValue(town, dto, district);
            town = townRepository.save(town);
            return new TownDto(town);
        } else {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.NOT_FOUND)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
    }

    @Override
    public Boolean delete(UUID id) {
        if(!ObjectUtils.isEmpty(id) && townRepository.existsById(id)) {
            townRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean isValidDto(TownDto dto) {
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<TownDto>> violations = validator.validate(dto, OnUpdate.class);
        if(!violations.isEmpty()) {
            for(ConstraintViolation<TownDto> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }
        if(!ObjectUtils.isEmpty(dto.getDistrictId()) &&
                !districtRepository.existsById(dto.getDistrictId())) {
            errors.put("District", MessageConst.NOT_FOUND);
        }
        if(!errors.isEmpty()) {
            throw new InvalidDtoException(errors);
        }
        return true;
    }
}
