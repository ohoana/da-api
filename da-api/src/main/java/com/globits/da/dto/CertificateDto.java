package com.globits.da.dto;

import com.globits.core.domain.BaseObject;
import com.globits.da.domain.Certificate;

import javax.validation.constraints.NotBlank;

public class CertificateDto extends BaseObject {
    @NotBlank(message = "Name must not be empty")
    private String name;

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
