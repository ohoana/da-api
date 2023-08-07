package com.globits.da.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.globits.core.domain.BaseObject;
import com.globits.da.consts.MessageConst;
import com.globits.da.consts.RegexConst;
import com.globits.da.domain.CertificateMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CertificateMapDto extends BaseObject {
    @NotNull(message = MessageConst.NOT_NULL)
    private UUID employeeId;
    @NotNull(message = MessageConst.NOT_NULL)
    private UUID provinceId;
    @NotNull(message = MessageConst.NOT_NULL)
    private UUID certificateId;
    @NotNull(message = MessageConst.NOT_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = RegexConst.DATE_REGEX)
    private LocalDateTime beginDate;
    @NotNull(message = MessageConst.NOT_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = RegexConst.DATE_REGEX)
    private LocalDateTime expireDate;
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
}
