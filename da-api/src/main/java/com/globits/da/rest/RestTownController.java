package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.TownDto;
import com.globits.da.service.TownService;
import com.globits.da.validator.marker.OnCreate;
import com.globits.da.validator.marker.OnUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/town")
public class RestTownController {
    private final TownService townService;

    public RestTownController(TownService townService) {
        this.townService = townService;
    }

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
    public ResponseEntity<TownDto> add(@Validated(OnCreate.class) @RequestBody TownDto dto) {
        if(!townService.isValidDto(dto)) {
            return ResponseEntity.badRequest().build();
        }
        TownDto result = townService.saveOrUpdate(dto, null);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<TownDto> update(@Validated(OnUpdate.class) @RequestBody TownDto dto,
                                                      @PathVariable UUID id) {
        if(!townService.isValidDto(dto)) {
            return ResponseEntity.badRequest().build();
        }
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
