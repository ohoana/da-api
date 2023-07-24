package com.globits.da.dto;

import com.globits.core.domain.BaseObject;
import com.globits.da.domain.Province;
import org.springframework.util.ObjectUtils;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProvinceDto extends BaseObject {
    @NotBlank(message = "Name must not be empty")
    private String name;
    private List<DistrictDto> districtDtoList;

    public ProvinceDto() {
    }

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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<DistrictDto> getDistrictDtoList() {
        return districtDtoList;
    }
}
