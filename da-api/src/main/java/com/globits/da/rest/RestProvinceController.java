package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/province")
public class RestProvinceController {
    @Autowired
    private ProvinceService provinceService;

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/provinces", method = RequestMethod.GET)
    public ResponseEntity<List<ProvinceDto>> getAll() {
        List<ProvinceDto> result = provinceService.getAll();
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProvinceDto> getById(@PathVariable UUID id) {
        ProvinceDto result = provinceService.getById(id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ProvinceDto> addProvince(@RequestBody ProvinceDto dto) {
        ProvinceDto result = provinceService.saveOrUpdate(dto, null);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ProvinceDto> updateProvice(@RequestBody ProvinceDto dto,
                                                     @PathVariable UUID id) {
        ProvinceDto result = provinceService.saveOrUpdate(dto, id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteById(@PathVariable UUID id) {
        Boolean result = provinceService.delete(id);
        return ResponseEntity.ok()
                .body(result);
    }
}
