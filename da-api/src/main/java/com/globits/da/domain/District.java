package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_district")
public class District extends BaseObject {
    private static final long serialVersionUID = 1L;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;
    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
    private List<Town> towns;
    @OneToMany(mappedBy = "district")
    private List<Employee> employees;
}
