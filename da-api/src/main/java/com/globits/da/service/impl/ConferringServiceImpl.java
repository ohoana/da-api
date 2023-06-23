package com.globits.da.service.impl;

import com.globits.da.dto.ConferringDto;
import com.globits.da.repository.ConferringRepository;
import com.globits.da.service.ConferringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
public class ConferringServiceImpl implements ConferringService {

    @Autowired
    private ConferringRepository conferringRepository;

    @Override
    public List<ConferringDto> getAll() {
        return conferringRepository.getAll();
    }

    @Override
    public ConferringDto saveOrUpdate(ConferringDto conferringDto, UUID id) {
        if(!ObjectUtils.isEmpty(conferringDto)) {

        }
        return null;
    }
}
