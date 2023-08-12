package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.CertificateDto;
import com.globits.da.exception.InvalidInputException;
import com.globits.da.service.CertificateService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/certificate")
public class RestCertificateController {
    private final CertificateService certificateService;

    public RestCertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/certificates", method = RequestMethod.GET)
    public ResponseEntity<List<CertificateDto>> getAll() {
        List<CertificateDto> result = certificateService.getAll();
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<CertificateDto> getById(@PathVariable UUID id) {
        CertificateDto result = certificateService.getById(id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CertificateDto> add(@Valid @RequestBody CertificateDto dto) throws InvalidInputException {
        CertificateDto result = certificateService.save(dto);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CertificateDto> update(@Valid @RequestBody CertificateDto dto,
                                                            @PathVariable UUID id) throws InvalidInputException {
        CertificateDto result = certificateService.update(dto, id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteById(@PathVariable UUID id) {
        Boolean result = certificateService.delete(id);
        return ResponseEntity.ok()
                .body(result);
    }
}
