package com.globits.da.service.impl;

import com.globits.da.commons.ApiMessageError;
import com.globits.da.consts.MessageConst;
import com.globits.da.domain.Province;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.exception.InvalidInputException;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.ProvinceService;
import com.globits.da.utils.InjectParam;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProvinceServiceImpl implements ProvinceService {
    private final ProvinceRepository provinceRepository;
    private final InjectParam injectParam;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository,
                               InjectParam injectParam) {
        this.provinceRepository = provinceRepository;
        this.injectParam = injectParam;
    }

    @Override
    public ProvinceDto getById(UUID id) {
        try {
            Province province = provinceRepository.getOne(id);
            return new ProvinceDto(province);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @Override
    public List<ProvinceDto> getAll() {
        return provinceRepository.getAll();
    }

    @Override
    public ProvinceDto save(ProvinceDto dto) throws InvalidInputException {
        if(ObjectUtils.isEmpty(dto)) {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.DTO_NOT_NULL)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
        Province province = new Province();
        injectParam.setProvinceValue(province, dto);
        province = provinceRepository.save(province);
        return new ProvinceDto(province);
    }

    @Override
    public ProvinceDto update(ProvinceDto dto, UUID id) throws InvalidInputException {
        if(ObjectUtils.isEmpty(dto)) {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.DTO_NOT_NULL)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
        if(!id.equals(dto.getId())) {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.ID_UPDATE_NOT_MATCH)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
        Optional<Province> provinceOpt = provinceRepository.findById(dto.getId());
        if(provinceOpt.isPresent()) {
            Province province = provinceOpt.get();
            injectParam.setProvinceValue(province, dto);
            province = provinceRepository.save(province);
            return new ProvinceDto(province);
        } else {
            ApiMessageError apiMessageError = ApiMessageError.builder()
                    .message(MessageConst.NOT_FOUND)
                    .build();
            throw new InvalidInputException(apiMessageError);
        }
    }

    @Override
    public Boolean delete(UUID id) {
        if(!ObjectUtils.isEmpty(id) && provinceRepository.existsById(id)) {
            provinceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
