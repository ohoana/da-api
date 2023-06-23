package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.DistrictDto;
import com.globits.da.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/district")
public class RestDistrictController {
    @Autowired
    private DistrictService districtService;

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "districts", method = RequestMethod.GET)
    public ResponseEntity<List<DistrictDto>> getAll() {
        List<DistrictDto> result = districtService.getAll();
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<DistrictDto> getById(@PathVariable UUID id) {
        DistrictDto result = districtService.getById(id);
        return ResponseEntity.ok()
                .body(result);
    }
    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/province/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<DistrictDto>> getByProvinceId(@PathVariable UUID id) {
        List<DistrictDto> result = districtService.getByProvinceId(id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<DistrictDto> addDistrict(@RequestBody DistrictDto dto) {
        DistrictDto result = districtService.saveOrUpdate(dto, null);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<DistrictDto> updateDistrict(@RequestBody DistrictDto dto,
                                                            @PathVariable UUID id) {
        DistrictDto result = districtService.saveOrUpdate(dto, id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteById(@PathVariable UUID id) {
        Boolean result = districtService.delete(id);
        return ResponseEntity.ok()
                .body(result);
    }
}
