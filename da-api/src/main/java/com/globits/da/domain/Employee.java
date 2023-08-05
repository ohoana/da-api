package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_employee")
public class Employee extends BaseObject {
    private static final long serialVersionUID = 1L;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "phone", nullable = false)
    private String phone;
    @Column(name = "age", nullable = false)
    private Integer age;
    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;
    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;
    @ManyToOne
    @JoinColumn(name = "town_id")
    private Town town;
    @OneToMany(mappedBy = "employee")
    private List<CertificateMap> certificates;
}
