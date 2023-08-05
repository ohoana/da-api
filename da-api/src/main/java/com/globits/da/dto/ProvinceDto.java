package com.globits.da.dto;

import com.globits.core.domain.BaseObject;
import com.globits.da.consts.MessageConst;
import com.globits.da.domain.Province;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProvinceDto extends BaseObject {
    @NotBlank(message = MessageConst.NOT_EMPTY)
    private String name;
    private List<DistrictDto> districtDtoList;

    public ProvinceDto(Province province) {
        if(!ObjectUtils.isEmpty(province)) {
            this.setId(province.getId());
            this.name = province.getName();
            if(!ObjectUtils.isEmpty(province.getDistricts())) {
                this.districtDtoList = province.getDistricts().stream().map(DistrictDto::new).collect(Collectors.toList());
            } else {
                this.districtDtoList = new ArrayList<>();
            }
        }
    }
}
