package com.globits.da.dto;

import com.globits.core.domain.BaseObject;
import com.globits.da.domain.District;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import org.springframework.security.core.parameters.P;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DistrictDto extends BaseObject {
    @NotBlank(message = "Name must not be empty",
            groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "Province must not be null",
            groups = {OnCreate.class})
    private UUID provinceId;

    List<TownDto> townDtos;

    public DistrictDto() {
    }

    public DistrictDto(District district) {
        if(!ObjectUtils.isEmpty(district)) {
            this.setId(district.getId());
            this.name = district.getName();
            this.provinceId = district.getProvince().getId();
            if(!ObjectUtils.isEmpty(district.getTowns())) {
                this.townDtos = district.getTowns().stream()
                        .map(item -> {
                            return new TownDto(item);
                        }).collect(Collectors.toList());
            } else {
                this.townDtos = new ArrayList<>();
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(UUID provinceId) {
        this.provinceId = provinceId;
    }

    public List<TownDto> getTownDtos() {
        return townDtos;
    }

    public void setTownDtos(List<TownDto> townDtos) {
        this.townDtos = townDtos;
    }
}
