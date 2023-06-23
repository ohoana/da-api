package com.globits.da.repository;

import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    @Query("select new com.globits.da.dto.EmployeeDto(em) from Employee em")
    List<EmployeeDto> getAllEmployee();

    @Query("select new com.globits.da.dto.EmployeeDto(em) from Employee em")
    Page<EmployeeDto> getListPage(Pageable pageable);

    @Query("select new com.globits.da.dto.EmployeeDto(entity) from Employee entity where  entity.code = ?1")
    EmployeeDto findByCode(String code);
}
