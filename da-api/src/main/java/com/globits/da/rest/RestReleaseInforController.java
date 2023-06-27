package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.ReleaseInforDto;
import com.globits.da.service.ReleaseInforService;
import com.globits.da.utils.exception.InvalidDtoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/conferring")
public class RestReleaseInforController {

    @Autowired
    private ReleaseInforService releaseInforService;

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/conferrings", method = RequestMethod.GET)
    public ResponseEntity<List<ReleaseInforDto>> getAll() {
        List<ReleaseInforDto> result = releaseInforService.getAll();
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<ReleaseInforDto>> getByEmployee(@PathVariable UUID id) {
        List<ReleaseInforDto> result = releaseInforService.getByEmployeeId(id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReleaseInforDto> addCertificateToEmployee(@Valid @RequestBody ReleaseInforDto dto) {
        ReleaseInforDto result = null;
        if(releaseInforService.isValidDto(dto)){
            result = releaseInforService.saveOrUpdate(dto, null);
        }
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ReleaseInforDto> updateCertificateToEmployee(@Valid @RequestBody ReleaseInforDto dto,
                                                                       @PathVariable UUID id) {
        ReleaseInforDto result = null;
        if(releaseInforService.isValidDto(dto)){
            result = releaseInforService.saveOrUpdate(dto, id);
        }
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteCertificateOfEmployee(@PathVariable UUID id) {
        Boolean result = releaseInforService.deleteById(id);
        return ResponseEntity.ok()
                .body(result);
    }

}
