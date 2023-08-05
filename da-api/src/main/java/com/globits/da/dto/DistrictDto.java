package com.globits.da.dto;

import com.globits.core.domain.BaseObject;
import com.globits.da.consts.MessageConst;
import com.globits.da.domain.District;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DistrictDto extends BaseObject {
    @NotBlank(message = MessageConst.NOT_EMPTY,
            groups = {OnCreate.class, OnUpdate.class})
    private String name;
    @NotNull(message = MessageConst.NOT_NULL,
            groups = {OnCreate.class})
    private UUID provinceId;
    private List<TownDto> townDtoList;

    public DistrictDto(District district) {
        if(!ObjectUtils.isEmpty(district)) {
            this.setId(district.getId());
            this.name = district.getName();
            this.provinceId = district.getProvince().getId();
            if(!ObjectUtils.isEmpty(district.getTowns())) {
                this.townDtoList = district.getTowns().stream()
                        .map(TownDto::new).collect(Collectors.toList());
            } else {
                this.townDtoList = new ArrayList<>();
            }
        }
    }

}
