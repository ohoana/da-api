package com.globits.da.dto;

import com.globits.core.domain.BaseObject;
import com.globits.da.domain.Province;
import org.springframework.security.core.parameters.P;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProvinceDto extends BaseObject {
    @NotBlank(message = "Name must not be empty")
    private String name;
    private List<DistrictDto> districtDtos;

    public ProvinceDto() {
        super();
    }

    public ProvinceDto(Province province) {
        if(!ObjectUtils.isEmpty(province)) {
            this.setId(province.getId());
            this.name = province.getName();
//            if(!ObjectUtils.isEmpty(province.getDistricts())) {
//                this.districtDtos = province.getDistricts().stream().map((item) -> {
//                    return new DistrictDto(item);
//                }).collect(Collectors.toList());
//            } else {
//                this.districtDtos = new ArrayList<>();
//            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DistrictDto> getDistrictDtos() {
        return districtDtos;
    }

    public void setDistrictDtos(List<DistrictDto> districtDtos) {
        this.districtDtos = districtDtos;
    }
}
