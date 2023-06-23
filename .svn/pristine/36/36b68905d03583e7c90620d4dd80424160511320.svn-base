package com.globits.da.service.impl;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.domain.Town;
import com.globits.da.dto.DistrictDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.DistrictService;
import com.globits.da.service.ProvinceService;
import com.globits.da.service.TownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private TownService townService;

    @Override
    public DistrictDto getById(UUID id) {
        try {
            District district = districtRepository.getOne(id);
            return new DistrictDto(district);
        } catch (EntityNotFoundException e) {
            return null;
        }
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
    public DistrictDto saveOrUpdate(DistrictDto dto, UUID id) {
        if(!ObjectUtils.isEmpty(dto)) {
            District district = null;
            if(!ObjectUtils.isEmpty(id)) {
                if(!ObjectUtils.isEmpty(dto.getId()) && !id.equals(dto.getId())) {
                    return null;
                }
                try {
                    district = districtRepository.getOne(id);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            if(ObjectUtils.isEmpty(district)) {
                district = new District();
            }

            try {
                Province province = provinceRepository.getOne(dto.getProvinceId());
                district.setProvince(province);
            } catch (EntityNotFoundException | InvalidDataAccessApiUsageException e) {
                e.printStackTrace();
                return null;
            }

            district.setName(dto.getName());
            district = districtRepository.save(district);

            List<Town> towns = townService.saveOrUpdateList(dto.getTownDtos(), district);
            district.setTowns(towns);

            district = districtRepository.save(district);

            if(!ObjectUtils.isEmpty(district)) {
                return new DistrictDto(district);
            }

        }
        return null;
    }

    @Override
    public List<District> saveOrUpdateList(List<DistrictDto> dtos, Province province) {
        List<District> districts = new ArrayList<>();
        if(!ObjectUtils.isEmpty(dtos)) {
            for(DistrictDto dto : dtos) {
                District district = null;
                try {
                    district = districtRepository.getOne(dto.getId());
                    district.setName(dto.getName());
                    district.setProvince(province);
                } catch (EntityNotFoundException e) {
                    district = new District();
                    district.setName(dto.getName());
                    district.setProvince(province);
                } finally {
                    district = districtRepository.save(district);
                    List<Town> towns = townService.saveOrUpdateList(dto.getTownDtos(), district);
                    district.setTowns(towns);
                    districts.add(district);
                }
            }
            return districts;
        }
        return null;
    }


    @Override
    public Boolean delete(UUID id) {
        if(!ObjectUtils.isEmpty(id)) {
            districtRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
