package com.globits.da.domain;

import com.globits.core.domain.BaseObject;

import javax.persistence.*;
import java.util.List;

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
    @OneToMany(mappedBy = "province")
    private List<CertificateMap> releases;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<District> getDistricts() {
        return districts;
    }
    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }
}
