package com.globits.da.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.globits.core.domain.BaseObject;
import com.globits.da.domain.ReleaseInfor;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReleaseInforDto extends BaseObject {

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

    public ReleaseInforDto() {
    }

    public ReleaseInforDto(ReleaseInfor releaseInfor) {
        if(!ObjectUtils.isEmpty(releaseInfor)) {
            this.setId(releaseInfor.getId());
            this.certificateId = releaseInfor.getCertificate().getId();
            this.provinceId = releaseInfor.getProvince().getId();
            this.employeeId = releaseInfor.getEmployee().getId();
            this.beginDate = releaseInfor.getBeginDate();
            this.expireDate = releaseInfor.getExpireDate();
        }
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public UUID getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(UUID provinceId) {
        this.provinceId = provinceId;
    }

    public UUID getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(UUID certificateId) {
        this.certificateId = certificateId;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }
}
