package com.globits.da.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.DistrictService;
import com.globits.da.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProvinceServiceImpl implements ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictService districtService;


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
        return provinceRepository.getALlProvince();
    }

    @Override
    public ProvinceDto saveOrUpdate(ProvinceDto dto, UUID id) {
        if(!ObjectUtils.isEmpty(dto)) {
            Province province = null;
            //update
            if(!ObjectUtils.isEmpty(id)) {
                if(!ObjectUtils.isEmpty(dto.getId()) && !id.equals(dto.getId())) {
                    return null;
                }
                try {
                    province = provinceRepository.getOne(id);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            if(ObjectUtils.isEmpty(province)) {
                province = new Province();
            }

            province.setName(dto.getName());

            province = provinceRepository.save(province);

            List<District> districts = districtService.saveOrUpdateList(dto.getDistrictDtos(), province);

            province.setDistricts(districts);

            province = provinceRepository.save(province);

            if(!ObjectUtils.isEmpty(province)) {
                return new ProvinceDto(province);
            }
        }
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        if(!ObjectUtils.isEmpty(id)) {
            provinceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
