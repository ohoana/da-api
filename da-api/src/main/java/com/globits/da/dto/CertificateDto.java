package com.globits.da.dto;

import com.globits.core.domain.BaseObject;
import com.globits.da.consts.MessageConst;
import com.globits.da.domain.Certificate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CertificateDto extends BaseObject {
    @NotBlank(message = MessageConst.NOT_EMPTY)
    private String name;
    public CertificateDto(Certificate certificate) {
        if(certificate != null) {
            this.setId(certificate.getId());
            this.name = certificate.getName();
        }
    }
}
