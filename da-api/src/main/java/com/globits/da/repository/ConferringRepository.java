package com.globits.da.repository;

import com.globits.da.domain.Conferring;
import com.globits.da.dto.ConferringDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConferringRepository extends JpaRepository<Conferring, UUID> {

    @Query("SELECT new com.globits.da.dto.ConferringDto(entity) FROM Conferring entity")
    List<ConferringDto> getAll();

    @Query("SELECT new com.globits.da.dto.ConferringDto(entity) FROM Conferring entity " +
            "WHERE entity.employee.id = ?1")
    List<ConferringDto> getByEmployeeId(UUID employeeId);

    @Query("SELECT COUNT(entity) FROM Conferring entity " +
            "WHERE entity.expireDate >= current_date AND entity.employee.id = ?1 " +
            "AND entity.certificate.id = ?2")
    Integer countCertificateInUse(UUID employeeId, UUID certificateId);

    @Query("SELECT new com.globits.da.dto.ConferringDto(entity) FROM Conferring entity " +
            "WHERE entity.employee.id = ?1 AND entity.certificate.id = ?2 AND entity.province.id = ?3 " +
            "AND entity.expireDate >= current_date ")
    ConferringDto getCertificateInUseByProvinceId(UUID employeeId, UUID certificateId, UUID provinceId);
}
