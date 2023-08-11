package com.globits.da.utils;

import com.globits.da.domain.*;
import com.globits.da.dto.*;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.repository.TownRepository;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InjectParam {
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final TownRepository townRepository;

    public InjectParam(ProvinceRepository provinceRepository, DistrictRepository districtRepository, TownRepository townRepository) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.townRepository = townRepository;
    }

    public void setEmployeeValue(Employee employee, EmployeeDto employeeDto, Class<?> group) {
        employee.setCode(employeeDto.getCode());
        employee.setName(employeeDto.getName());
        employee.setAge(employeeDto.getAge());
        employee.setPhone(employeeDto.getPhone());
        employee.setEmail(employeeDto.getEmail());
        if(group.isInstance(OnCreate.class) ||
                (group.isInstance(OnUpdate.class)
                        && !ObjectUtils.isEmpty(employeeDto.getProvinceId())
                        && !ObjectUtils.isEmpty(employeeDto.getDistrictId())
                        && !ObjectUtils.isEmpty(employeeDto.getTownId()))) {
            employee.setProvince(provinceRepository.getOne(employeeDto.getProvinceId()));
            employee.setDistrict(districtRepository.getOne(employeeDto.getDistrictId()));
            employee.setTown(townRepository.getOne(employeeDto.getTownId()));
        }
    }

    public void setProvinceValue(Province province, ProvinceDto provinceDto) {
        province.setName(provinceDto.getName());
        List<District> districts = provinceDto.getDistrictDtoList().stream()
                .map(item -> {
                    Optional<District> districtOpt = districtRepository.findById(item.getId());
                    District district = districtOpt.orElseGet(District::new);
                    setDistrictValue(district, item, province);
                    return district;
                })
                .collect(Collectors.toList());
        province.setDistricts(districts);
    }

    public void setDistrictValue(District district, DistrictDto districtDto, Province province) {
        district.setName(districtDto.getName());
        if(!ObjectUtils.isEmpty(province)) {
            district.setProvince(province);
        }
        List<Town> towns = districtDto.getTownDtoList().stream()
                .map(item -> {
                    Optional<Town> townOpt = townRepository.findById(item.getId());
                    Town town = townOpt.orElseGet(Town::new);
                    setTownValue(town, item, district);
                    return town;
                }).collect(Collectors.toList());
        district.setTowns(towns);

    }

    public void setTownValue(Town town, TownDto townDto, District district) {
        town.setName(townDto.getName());
        if(!ObjectUtils.isEmpty(district)) {
            town.setDistrict(district);
        }
    }

    public void setCertificateValue(Certificate certificate, CertificateDto certificateDto) {
        certificate.setName(certificate.getName());
    }

    public void updateQuery(EmployeeSearchDto searchDto, String query) {
        if(searchDto.getName() != null && StringUtils.hasText(searchDto.getName())) {
            query += "and (entity.name like :name) ";
        }
        if(searchDto.getEmail() != null && StringUtils.hasText(searchDto.getEmail())) {
            query += "and (entity.email like :email) ";
        }
        if(searchDto.getCode() != null && StringUtils.hasText(searchDto.getCode())) {
            query += "and (entity.code like :code) ";
        }
        if(searchDto.getPhone() != null && StringUtils.hasText(searchDto.getPhone())) {
            query += "and (entity.phone like :phone) ";
        }
    }

    public void setParamQuery(EmployeeSearchDto searchDto, Query sqlQuery, Query sqlCountQuery) {
        if(searchDto.getName() != null && StringUtils.hasText(searchDto.getName())) {
            sqlQuery.setParameter("name", "%" + searchDto.getName() + "%");
            sqlCountQuery.setParameter("name", "%" + searchDto.getName() + "%");
        }
        if(searchDto.getEmail() != null && StringUtils.hasText(searchDto.getEmail())) {
            sqlQuery.setParameter("email", "%" + searchDto.getEmail() + "%");
            sqlCountQuery.setParameter("email", "%" + searchDto.getEmail() + "%");
        }
        if(searchDto.getCode() != null && StringUtils.hasText(searchDto.getCode())) {
            sqlQuery.setParameter("code", "%" + searchDto.getCode() + "%");
            sqlCountQuery.setParameter("code", "%" + searchDto.getCode() + "%");
        }
        if(searchDto.getPhone() != null && StringUtils.hasText(searchDto.getPhone())) {
            sqlQuery.setParameter("phone", "%" + searchDto.getPhone() + "%");
            sqlCountQuery.setParameter("phone", "%" + searchDto.getPhone() + "%");
        }
    }
}
