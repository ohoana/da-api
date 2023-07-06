package com.globits.da.service.impl;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.DistrictService;
import com.globits.da.service.ProvinceService;
import com.globits.da.utils.exception.InvalidDtoException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProvinceServiceImpl implements ProvinceService {
    private final ProvinceRepository provinceRepository;
    private final DistrictService districtService;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository,
                               DistrictService districtService) {
        this.provinceRepository = provinceRepository;
        this.districtService = districtService;
    }

    @Override
    public ProvinceDto getById(UUID id) {
        try {
            Province province = provinceRepository.getOne(id);
            return new ProvinceDto(province);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @Override
    public List<ProvinceDto> getAll() {
        return provinceRepository.getAll();
    }

    @Override
    public ProvinceDto saveOrUpdate(ProvinceDto dto, UUID id) {
        if(!ObjectUtils.isEmpty(dto)) {
            Province province = null;
            if(!ObjectUtils.isEmpty(id)) {
                if(!ObjectUtils.isEmpty(dto.getId()) && !id.equals(dto.getId())) {
                    return null;
                }
                province = provinceRepository.getOne(id);
            }
            if(ObjectUtils.isEmpty(province)) {
                province = new Province();
            }
            try {
                province.setName(dto.getName());
                province = provinceRepository.save(province);
                List<District> districts = districtService.saveOrUpdateList(dto.getDistrictDtoList(), province);
                province.setDistricts(districts);
                province = provinceRepository.save(province);
                if(!ObjectUtils.isEmpty(province)) {
                    return new ProvinceDto(province);
                }
            } catch (EntityNotFoundException e) {
                Map<String, String> errors = new HashMap<>();
                errors.put("Province", "Not found!");
                throw new InvalidDtoException(errors);
            }
        }
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        if(!ObjectUtils.isEmpty(id)) {
            try {
                provinceRepository.deleteById(id);
                return true;
            } catch (EmptyResultDataAccessException e) {
                return false;
            }
        }
        return false;
    }
}
