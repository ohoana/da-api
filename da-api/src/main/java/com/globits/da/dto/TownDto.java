package com.globits.da.dto;

import com.globits.core.domain.BaseObject;
import com.globits.da.consts.MessageConst;
import com.globits.da.domain.Town;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TownDto extends BaseObject {
    @NotBlank(message = MessageConst.NOT_EMPTY,
            groups = {OnCreate.class, OnUpdate.class})
    private String name;
    @NotNull(message = MessageConst.NOT_NULL,
            groups = {OnCreate.class})
    private UUID districtId;

    public TownDto(Town town) {
        if(!ObjectUtils.isEmpty(town)) {
            this.setId(town.getId());
            this.name = town.getName();
            if(!ObjectUtils.isEmpty(town.getDistrict())) {
                this.districtId = town.getDistrict().getId();
            }
        }
    }
}
