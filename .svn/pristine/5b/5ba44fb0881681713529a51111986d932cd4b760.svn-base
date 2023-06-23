package com.globits.da.service.impl;

import com.globits.da.domain.District;
import com.globits.da.domain.Town;
import com.globits.da.dto.TownDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.TownRepository;
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
public class TownServiceImpl implements TownService {

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Override
    public TownDto getById(UUID id) {
        try {
            Town town = townRepository.getOne(id);
            return new TownDto(town);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<TownDto> getAll() {
        return townRepository.getAll();
    }

    @Override
    public List<TownDto> getByDistrictId(UUID districtId) {
        return townRepository.getTownByDistrictId(districtId);
    }

    @Override
    public TownDto saveOrUpdate(TownDto dto, UUID id) {
        if(!ObjectUtils.isEmpty(dto)) {
            Town town = null;
            if(!ObjectUtils.isEmpty(id)) {
                if(!ObjectUtils.isEmpty(dto.getId()) && !id.equals(dto.getId())) {
                    return null;
                }
                try {
                    town = townRepository.getOne(id);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            if(ObjectUtils.isEmpty(town)) {
                town = new Town();
            }

            town.setName(dto.getName());

            try {
                if(!ObjectUtils.isEmpty(dto.getDistrictId())) {
                    District district = districtRepository.getOne(dto.getDistrictId());
                    town.setDistrict(district);
                } else {
                    return null;
                }
            } catch (EntityNotFoundException | InvalidDataAccessApiUsageException e) {
                e.printStackTrace();
                return null;
            }

            town = townRepository.save(town);
            if(!ObjectUtils.isEmpty(town)) {
                return new TownDto(town);
            }
        }
        return null;
    }

    @Override
    public List<Town> saveOrUpdateList(List<TownDto> dtos, District district) {
        if(!ObjectUtils.isEmpty(dtos)) {
            List<Town> towns = new ArrayList<>();
            for(TownDto dto : dtos) {
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
            return towns;
        }
        return null;
    }


    @Override
    public Boolean delete(UUID id) {
        if(!ObjectUtils.isEmpty(id)) {
            townRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
