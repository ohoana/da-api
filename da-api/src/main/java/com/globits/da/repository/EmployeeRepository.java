package com.globits.da.repository;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    @Query("SELECT new com.globits.da.dto.EmployeeDto(em) FROM Employee em")
    List<EmployeeDto> getAll();
    @Query("SELECT new com.globits.da.dto.EmployeeDto(em) FROM Employee em")
    Page<EmployeeDto> getPage(Pageable pageable);
    @Query("SELECT new com.globits.da.dto.EmployeeDto(entity) FROM Employee entity WHERE entity.code = ?1")
    EmployeeDto findByCode(String code);
    @Query("SELECT (count(entity) > 0) FROM Employee entity WHERE entity.code = :code AND entity.id != :id ")
    Boolean existsByCode(@Param("code") String code,@Param("id") UUID id);
}
