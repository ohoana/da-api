package com.globits.da.service.impl;

import com.globits.da.domain.District;
import com.globits.da.domain.Town;
import com.globits.da.dto.TownDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.TownRepository;
import com.globits.da.service.TownService;
import com.globits.da.utils.exception.InvalidDtoException;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

@Service
public class TownServiceImpl implements TownService {

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private Validator validator;

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
    public TownDto saveOrUpdate(TownDto dto, UUID id) {
        if(!ObjectUtils.isEmpty(dto)) {
            Town town = null;
            if(!ObjectUtils.isEmpty(id)) {
                if(!ObjectUtils.isEmpty(dto.getId()) && !id.equals(dto.getId())) {
                    return null;
                }
                town = townRepository.getOne(id);
            }

            if(ObjectUtils.isEmpty(town)) {
                town = new Town();
            }

            try {
                town.setName(dto.getName());
                if(!ObjectUtils.isEmpty(dto.getDistrictId())) {
                    District district = districtRepository.getOne(dto.getDistrictId());
                    town.setDistrict(district);
                }
                town = townRepository.save(town);
                if(!ObjectUtils.isEmpty(town)) {
                    return new TownDto(town);
                }
            } catch (EntityNotFoundException e) {
                Map<String, String> errors = new HashMap<>();
                errors.put("Town", "Not found!");
                throw new InvalidDtoException(errors);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public List<Town> saveOrUpdateList(List<TownDto> dtos, District district) {
        if(!ObjectUtils.isEmpty(dtos)) {
            List<Town> towns = new ArrayList<>();
            for(TownDto dto : dtos) {
                if(isValidDto(dto)) {
                    Town town = null;
                    try {
                        town = townRepository.getOne(dto.getId());
                        town.setName(dto.getName());
                        town.setDistrict(district);
                    } catch (EntityNotFoundException e) {
                        town = new Town();
                        town.setName(dto.getName());
                        town.setDistrict(district);
                    }
                    towns.add(town);
                }
            }
            return towns;
        }
        return null;
    }


    @Override
    public Boolean delete(UUID id) {
        if(!ObjectUtils.isEmpty(id)) {
            try {
                townRepository.deleteById(id);
                return true;
            } catch (EmptyResultDataAccessException e) {
                return false;
            }
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
            errors.put("District", "Not found!");
        }
        if(!errors.isEmpty()) {
            throw new InvalidDtoException(errors);
        }
        return true;
    }
}
