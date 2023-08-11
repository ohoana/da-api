package com.globits.da.validation;

import com.globits.da.commons.ApiMessageError;
import com.globits.da.commons.ApiValidatorError;
import com.globits.da.consts.MessageConst;
import com.globits.da.exception.InvalidInputException;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.repository.TownRepository;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Component
public class ValidationLocation {
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final TownRepository townRepository;

    public ValidationLocation(ProvinceRepository provinceRepository,
                              DistrictRepository districtRepository,
                              TownRepository townRepository) {
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.townRepository = townRepository;
    }

    private void checkProvinceValid(UUID provinceId) throws InvalidInputException {
        if(ObjectUtils.isEmpty(provinceId) || !provinceRepository.existsById(provinceId)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("province")
                    .data(provinceId)
                    .message(MessageConst.NOT_FOUND)
                    .build();
            throw new InvalidInputException(apiValidatorError);
        }
    }

    private void checkDistrictValid(UUID districtId) throws InvalidInputException {
        if(ObjectUtils.isEmpty(districtId) || !districtRepository.existsById(districtId)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("province")
                    .data(districtId)
                    .message(MessageConst.NOT_FOUND)
                    .build();
            throw new InvalidInputException(apiValidatorError);
        }
    }

    private void checkTownValid(UUID townId) throws InvalidInputException {
        if(ObjectUtils.isEmpty(townId) || !townRepository.existsById(townId)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("town")
                    .data(townId)
                    .message(MessageConst.NOT_FOUND)
                    .build();
            throw new InvalidInputException(apiValidatorError);
        }
    }

    public void checkLocationValid(UUID provinceId, UUID districtId, UUID townId, Class<?> group) throws InvalidInputException {
        if(group.equals(OnCreate.class) ||
                (group.equals(OnUpdate.class)
                        && !ObjectUtils.isEmpty(provinceId)
                        && !ObjectUtils.isEmpty(districtId)
                        && !ObjectUtils.isEmpty(townId))) {
            checkProvinceValid(provinceId);
            checkDistrictValid(districtId);
            checkTownValid(townId);
            if (!districtRepository.isDistrictInProvince(provinceId, districtId)) {
                ApiMessageError apiMessageError = ApiMessageError.builder().
                        message(MessageConst.DISTRICT_NOT_BELONG_TO_PROVINCE)
                        .build();
                throw new InvalidInputException(apiMessageError);
            }
            if (!townRepository.isTownInDistrict(townId, districtId)) {
                ApiMessageError apiMessageError = ApiMessageError.builder().
                        message(MessageConst.TOWN_NOT_BELONG_TO_DISTRICT)
                        .build();
                throw new InvalidInputException(apiMessageError);
            }
        }
    }
}
