package com.globits.da.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.globits.core.domain.BaseObject;
import com.globits.da.domain.CertificateMap;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class CertificateMapDto extends BaseObject {
    @NotNull(message = "Employee must not be null")
    private UUID employeeId;
    @NotNull(message = "Province must not be null")
    private UUID provinceId;
    @NotNull(message = "Certificate must not be null")
    private UUID certificateId;
    @NotNull(message = "Begin Date must not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginDate;
    @NotNull(message = "Expired Date must not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireDate;

    public CertificateMapDto() {
    }

    public CertificateMapDto(CertificateMap certificateMap) {
        if(!ObjectUtils.isEmpty(certificateMap)) {
            this.setId(certificateMap.getId());
            this.certificateId = certificateMap.getCertificate().getId();
            this.provinceId = certificateMap.getProvince().getId();
            this.employeeId = certificateMap.getEmployee().getId();
            this.beginDate = certificateMap.getBeginDate();
            this.expireDate = certificateMap.getExpireDate();
        }
    }

    public UUID getEmployeeId() {
        return employeeId;
    }
    public UUID getProvinceId() {
        return provinceId;
    }
    public UUID getCertificateId() {
        return certificateId;
    }
    public LocalDateTime getBeginDate() {
        return beginDate;
    }
    public LocalDateTime getExpireDate() {
        return expireDate;
    }
}
