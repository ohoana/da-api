package com.globits.da.repository;

import com.globits.da.domain.CertificateMap;
import com.globits.da.dto.CertificateMapDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReleaseInforRepository extends JpaRepository<CertificateMap, UUID> {
    @Query("SELECT new com.globits.da.dto.CertificateMapDto(entity) FROM CertificateMap entity")
    List<CertificateMapDto> getAll();
    @Query("SELECT new com.globits.da.dto.CertificateMapDto(entity) FROM CertificateMap entity " +
            "WHERE entity.employee.id = ?1")
    List<CertificateMapDto> getByEmployeeId(UUID employeeId);
    @Query("SELECT COUNT(entity.id) FROM CertificateMap entity " +
            "WHERE entity.expireDate >= current_date AND entity.employee.id = ?1 " +
            "AND entity.certificate.id = ?2")
    Integer countCertificateInUse(UUID employeeId, UUID certificateId);
    @Query("SELECT new com.globits.da.dto.CertificateMapDto(entity) FROM CertificateMap entity " +
            "WHERE entity.employee.id = ?1 AND entity.certificate.id = ?2 AND entity.province.id = ?3 " +
            "AND entity.expireDate >= current_date ")
    CertificateMapDto getCertificateInUseByProvinceId(UUID employeeId, UUID certificateId, UUID provinceId);
}
