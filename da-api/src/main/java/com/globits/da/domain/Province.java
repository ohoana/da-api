package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_province")
public class Province extends BaseObject {
    private static final long serialVersionUID = 1L;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<District> districts;
    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
    private List<Employee> employees;
    @OneToMany(mappedBy = "province", cascade = {CascadeType.ALL})
    private List<CertificateMap> releases;
}
