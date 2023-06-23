package com.globits.da.dto;

import com.globits.core.domain.BaseObject;
import com.globits.da.domain.Town;
import org.springframework.data.annotation.Transient;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

public class TownDto extends BaseObject {
    private String name;

    private UUID districtId;

    public TownDto() {
    }

    public TownDto(Town town) {
        if(!ObjectUtils.isEmpty(town)) {
            this.setId(town.getId());
            this.name = town.getName();
            if(!ObjectUtils.isEmpty(town.getDistrict())) {
                this.districtId = town.getDistrict().getId();
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getDistrictId() {
        return districtId;
    }

    public void setDistrictId(UUID districtId) {
        this.districtId = districtId;
    }
}
