package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.CertificateDto;
import com.globits.da.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.SaslServer;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/certificate")
public class RestCertificateController {

    @Autowired
    private CertificateService certificateService;


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
    public ResponseEntity<CertificateDto> addCertificate(@RequestBody CertificateDto dto) {
        CertificateDto result = certificateService.saveOrUpdate(dto, null);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CertificateDto> updateCertificate(@RequestBody CertificateDto dto,
                                                            @PathVariable UUID id) {
        CertificateDto result = certificateService.saveOrUpdate(dto, id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> updateCertificate(@PathVariable UUID id) {
        Boolean result = certificateService.delete(id);
        return ResponseEntity.ok()
                .body(result);
    }
}
