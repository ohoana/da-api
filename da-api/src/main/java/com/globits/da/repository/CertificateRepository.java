package com.globits.da.repository;

import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    @Query("SELECT new com.globits.da.dto.CertificateDto(entity) FROM Certificate entity")
    List<CertificateDto> getAll();
}
