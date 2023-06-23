package com.globits.da.dto;

import com.globits.core.domain.BaseObject;
import com.globits.da.domain.Town;
import org.springframework.data.annotation.Transient;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class TownDto extends BaseObject {
    @NotBlank(message = "Name must not be empty")
    private String name;

    @NotNull(message = "District must not be null")
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
