package com.globits.da.validation;

import com.globits.da.commons.ApiValidatorError;
import com.globits.da.consts.MessageConst;
import com.globits.da.consts.RegexConst;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.exception.InvalidInputException;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Component
public class ValidationEmployee {
    private final EmployeeRepository employeeRepository;
    private final RegexValidator regexValidator;
    private final ValidationLocation validationLocation;

    public ValidationEmployee(EmployeeRepository employeeRepository, RegexValidator regexValidator, ValidationLocation validationLocation) {
        this.employeeRepository = employeeRepository;
        this.regexValidator = regexValidator;
        this.validationLocation = validationLocation;
    }

    private void checkCodeValid(String code, UUID id) throws InvalidInputException {
        if(!regexValidator.isMatches(RegexConst.CODE_REGEX, code)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("code")
                    .data(code)
                    .message(MessageConst.NOT_VALID)
                    .build();
            throw new InvalidInputException(apiValidatorError);
        }
        if(employeeRepository.existsByCode(code, id)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("code")
                    .data(code)
                    .message(MessageConst.NOT_DUPLICATE)
                    .build();
            throw new InvalidInputException(apiValidatorError);
        }
    }

    private void checkEmailValid(String email) throws InvalidInputException {
        if(!regexValidator.isMatches(RegexConst.EMAIL_REGEX, email)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("email")
                    .data(email)
                    .message(MessageConst.NOT_VALID)
                    .build();
            throw new InvalidInputException(apiValidatorError);
        }
    }

    private void checkPhoneValid(String phone) throws InvalidInputException {
        if(!regexValidator.isMatches(RegexConst.PHONE_REGEX, phone)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("phone")
                    .data(phone)
                    .message(MessageConst.NOT_VALID)
                    .build();
            throw new InvalidInputException(apiValidatorError);
        }
    }

    private void checkAgeValid(Integer age) throws InvalidInputException {
        if(age <= 0) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("age")
                    .data(age)
                    .message(MessageConst.NOT_VALID)
                    .build();
            throw new InvalidInputException(apiValidatorError);
        }
    }

    private void checkNameValid(String name) throws InvalidInputException {
        if(ObjectUtils.isEmpty(name)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("name")
                    .data(name)
                    .message(MessageConst.NOT_EMPTY)
                    .build();
            throw new InvalidInputException(apiValidatorError);
        }
    }

    private void checkProvinceIdValid(UUID provinceId) throws InvalidInputException {
        if(ObjectUtils.isEmpty(provinceId)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("provinceId")
                    .data(provinceId)
                    .message(MessageConst.NOT_EMPTY)
                    .build();
            throw new InvalidInputException(apiValidatorError);
        }
    }

    private void checkDistrictIdValid(UUID districtId) throws InvalidInputException {
        if(ObjectUtils.isEmpty(districtId)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("districtId")
                    .data(districtId)
                    .message(MessageConst.NOT_EMPTY)
                    .build();
            throw new InvalidInputException(apiValidatorError);
        }
    }

    private void checkTownIdValid(UUID townId) throws InvalidInputException {
        if(ObjectUtils.isEmpty(townId)) {
            ApiValidatorError apiValidatorError = ApiValidatorError.builder()
                    .field("townId")
                    .data(townId)
                    .message(MessageConst.NOT_EMPTY)
                    .build();
            throw new InvalidInputException(apiValidatorError);
        }
    }

    public void checkEmployeeValid(EmployeeDto employeeDto, Class<?> group) throws InvalidInputException {
        checkCodeValid(employeeDto.getCode(), employeeDto.getId());
        checkNameValid(employeeDto.getName());
        checkEmailValid(employeeDto.getEmail());
        checkPhoneValid(employeeDto.getPhone());
        checkAgeValid(employeeDto.getAge());
        if(group.equals(OnCreate.class) ||
                (group.equals(OnUpdate.class)
                        && !ObjectUtils.isEmpty(employeeDto.getProvinceId())
                        && !ObjectUtils.isEmpty(employeeDto.getDistrictId())
                        && !ObjectUtils.isEmpty(employeeDto.getTownId()))) {
            checkProvinceIdValid(employeeDto.getProvinceId());
            checkDistrictIdValid(employeeDto.getDistrictId());
            checkTownIdValid(employeeDto.getTownId());
            validationLocation.checkLocationValid(employeeDto.getProvinceId(),
                    employeeDto.getDistrictId(),
                    employeeDto.getTownId(), group);
        }
    }
}
