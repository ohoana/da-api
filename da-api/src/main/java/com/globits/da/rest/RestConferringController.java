package com.globits.da.rest;

import com.globits.da.AFFakeConstants;
import com.globits.da.dto.ConferringDto;
import com.globits.da.service.ConferringService;
import com.globits.da.utils.exception.InvalidDtoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/conferring")
public class RestConferringController {

    @Autowired
    private ConferringService conferringService;

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/conferrings", method = RequestMethod.GET)
    public ResponseEntity<List<ConferringDto>> getAll() {
        List<ConferringDto> result = conferringService.getAll();
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<ConferringDto>> getByEmployee(@PathVariable UUID id) {
        List<ConferringDto> result = conferringService.getByEmployeeId(id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ConferringDto> addCertificateToEmployee(@RequestBody ConferringDto conferringDto) {
        ConferringDto result = conferringService.saveOrUpdate(conferringDto, null);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ConferringDto> updateCertificateToEmployee(@RequestBody ConferringDto conferringDto,
                                                                     @PathVariable UUID id) {
        ConferringDto result = conferringService.saveOrUpdate(conferringDto, id);
        return ResponseEntity.ok()
                .body(result);
    }

    @Secured({AFFakeConstants.ROLE_ADMIN})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteCertificateOfEmployee(@PathVariable UUID id) {
        Boolean result = conferringService.deleteById(id);
        return ResponseEntity.ok()
                .body(result);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDtoException.class)
    public Map<String, String> handleExceptionDto(InvalidDtoException e) {
        return e.getErrors();
    }
}
