package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.domain.Town;
import com.globits.da.dto.TownDto;
import com.globits.da.service.TownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/town")
public class RestTownController {
    @Autowired
    private TownService townService;

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/towns", method = RequestMethod.GET)
    public ResponseEntity<List<TownDto>> getAll() {
        List<TownDto> result = townService.getAll();
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<TownDto> getById(@PathVariable UUID id) {
        TownDto result = townService.getById(id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/district/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<TownDto>> getByDistrictId(@PathVariable UUID id) {
        List<TownDto> result = townService.getByDistrictId(id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TownDto> addTown(@RequestBody TownDto dto) {
        TownDto result = townService.saveOrUpdate(dto, null);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<TownDto> updateTown(@RequestBody TownDto dto,
                                                      @PathVariable UUID id) {
        TownDto result = townService.saveOrUpdate(dto, id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteById(@PathVariable UUID id) {
        Boolean result = townService.delete(id);
        return ResponseEntity.ok()
                .body(result);
    }
}