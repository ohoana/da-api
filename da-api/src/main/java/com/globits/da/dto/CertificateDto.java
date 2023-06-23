package com.globits.da.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.globits.core.domain.BaseObject;
import com.globits.da.domain.Certificate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class CertificateDto extends BaseObject {
    private String name;

    public CertificateDto() {
    }

    public CertificateDto(Certificate certificate) {
        if(certificate != null) {
            this.setId(certificate.getId());
            this.name = certificate.getName();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
