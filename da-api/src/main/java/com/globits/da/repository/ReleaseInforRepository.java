package com.globits.da.repository;

import com.globits.da.domain.ReleaseInfor;
import com.globits.da.dto.ReleaseInforDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReleaseInforRepository extends JpaRepository<ReleaseInfor, UUID> {

    @Query("SELECT new com.globits.da.dto.ReleaseInforDto(entity) FROM ReleaseInfor entity")
    List<ReleaseInforDto> getAll();

    @Query("SELECT new com.globits.da.dto.ReleaseInforDto(entity) FROM ReleaseInfor entity " +
            "WHERE entity.employee.id = ?1")
    List<ReleaseInforDto> getByEmployeeId(UUID employeeId);

    @Query("SELECT COUNT(entity.id) FROM ReleaseInfor entity " +
            "WHERE entity.expireDate >= current_date AND entity.employee.id = ?1 " +
            "AND entity.certificate.id = ?2")
    Integer countCertificateInUse(UUID employeeId, UUID certificateId);

    @Query("SELECT new com.globits.da.dto.ReleaseInforDto(entity) FROM ReleaseInfor entity " +
            "WHERE entity.employee.id = ?1 AND entity.certificate.id = ?2 AND entity.province.id = ?3 " +
            "AND entity.expireDate >= current_date ")
    ReleaseInforDto getCertificateInUseByProvinceId(UUID employeeId, UUID certificateId, UUID provinceId);
}
