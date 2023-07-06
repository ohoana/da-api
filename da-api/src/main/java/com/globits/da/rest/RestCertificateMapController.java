package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.CertificateMapDto;
import com.globits.da.service.CertificateMapService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conferring")
public class RestCertificateMapController {
    private final CertificateMapService certificateMapService;

    public RestCertificateMapController(CertificateMapService certificateMapService) {
        this.certificateMapService = certificateMapService;
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/conferrings", method = RequestMethod.GET)
    public ResponseEntity<List<CertificateMapDto>> getAll() {
        List<CertificateMapDto> result = certificateMapService.getAll();
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<CertificateMapDto>> getByEmployee(@PathVariable UUID id) {
        List<CertificateMapDto> result = certificateMapService.getByEmployeeId(id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CertificateMapDto> addCertificateToEmployee(@Valid @RequestBody CertificateMapDto dto) {
        CertificateMapDto result = null;
        if(certificateMapService.isValidDto(dto)){
            result = certificateMapService.saveOrUpdate(dto, null);
        }
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CertificateMapDto> updateCertificateToEmployee(@Valid @RequestBody CertificateMapDto dto,
                                                                         @PathVariable UUID id) {
        CertificateMapDto result = null;
        if(certificateMapService.isValidDto(dto)){
            result = certificateMapService.saveOrUpdate(dto, id);
        }
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteCertificateOfEmployee(@PathVariable UUID id) {
        Boolean result = certificateMapService.deleteById(id);
        return ResponseEntity.ok()
                .body(result);
    }

}
