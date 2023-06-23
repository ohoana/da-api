package com.globits.da.domain;

import com.globits.core.domain.BaseObject;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_certificate")
public class Certificate extends BaseObject {
    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "certificate")
    private List<Conferring> conferrings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
