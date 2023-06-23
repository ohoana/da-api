package com.globits.da.domain;

import com.globits.core.domain.BaseObject;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_town")
public class Town extends BaseObject {
    private static final long serialVersionUID = 1L;
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @OneToMany(mappedBy = "town")
    private List<Employee> employees;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }
}
