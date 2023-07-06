package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.District;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.domain.Town;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.search.EmployeeSearchDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.repository.TownRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.utils.WriteExcelFile;
import com.globits.da.utils.exception.InvalidDtoException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, UUID> implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final TownRepository townRepository;
    private final Validator validator;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               ProvinceRepository provinceRepository,
                               DistrictRepository districtRepository,
                               TownRepository townRepository,
                               Validator validator) {
        this.employeeRepository = employeeRepository;
        this.provinceRepository = provinceRepository;
        this.districtRepository = districtRepository;
        this.townRepository = townRepository;
        this.validator = validator;
    }

    @Override
    public EmployeeDto findByCode(String code) {
        return employeeRepository.findByCode(code);
    }

    @Override
    public Page<EmployeeDto> getPage(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return employeeRepository.getPage(pageable);
    }

    @Override
    public Page<EmployeeDto> search(EmployeeSearchDto searchDto) {
        if(ObjectUtils.isEmpty(searchDto)) {
            return null;
        }

        int pageIndex = Math.max(searchDto.getPageIndex() - 1, 0);
        int pageSize = searchDto.getPageSize();

        String whereSql = "";

        String sql = "select new com.globits.da.dto.EmployeeDto(entity) from Employee as entity where (1=1) ";
        String sqlCount = "select count(entity.id) from Employee as entity where (1=1) ";

        String orderBySql = "order by entity.id desc";

        if(searchDto.getName() != null && StringUtils.hasText(searchDto.getName())) {
            whereSql += "and (entity.name like :name) ";
        }

        if(searchDto.getEmail() != null && StringUtils.hasText(searchDto.getEmail())) {
            whereSql += "and (entity.email like :email) ";
        }

        if(searchDto.getCode() != null && StringUtils.hasText(searchDto.getCode())) {
            whereSql += "and (entity.code like :code) ";
        }

        if(searchDto.getPhone() != null && StringUtils.hasText(searchDto.getPhone())) {
            whereSql += "and (entity.phone like :phone) ";
        }

        sql += whereSql + orderBySql;
        sqlCount += whereSql;

        Query sqlQuery = manager.createQuery(sql, EmployeeDto.class);
        Query sqlCountQuery = manager.createQuery(sqlCount);

        if(searchDto.getName() != null && StringUtils.hasText(searchDto.getName())) {
            sqlQuery.setParameter("name", "%" + searchDto.getName() + "%");
            sqlCountQuery.setParameter("name", "%" + searchDto.getName() + "%");
        }

        if(searchDto.getEmail() != null && StringUtils.hasText(searchDto.getEmail())) {
            sqlQuery.setParameter("email", "%" + searchDto.getEmail() + "%");
            sqlCountQuery.setParameter("email", "%" + searchDto.getEmail() + "%");
        }

        if(searchDto.getCode() != null && StringUtils.hasText(searchDto.getCode())) {
            sqlQuery.setParameter("code", "%" + searchDto.getCode() + "%");
            sqlCountQuery.setParameter("code", "%" + searchDto.getCode() + "%");
        }

        if(searchDto.getPhone() != null && StringUtils.hasText(searchDto.getPhone())) {
            sqlQuery.setParameter("phone", "%" + searchDto.getPhone() + "%");
            sqlCountQuery.setParameter("phone", "%" + searchDto.getPhone() + "%");
        }

        int offset = pageIndex * pageSize;
        sqlQuery.setFirstResult(offset);
        sqlQuery.setMaxResults(pageSize);

        @SuppressWarnings("unchecked")
        List<EmployeeDto> employeeDtoList = sqlQuery.getResultList();
        long count = (long) sqlCountQuery.getSingleResult();
        if(pageSize > 0) {
            Pageable pageable = PageRequest.of(pageIndex, pageSize);
            return new PageImpl<>(employeeDtoList, pageable, count);
        }
        return null;
    }

    @Override
    public List<EmployeeDto> getAll() {
        return employeeRepository.getAll();
    }

    @Override
    @Transactional
    public List<EmployeeDto> save(List<EmployeeDto> employeeDtoList) {
        if(!ObjectUtils.isEmpty(employeeDtoList)) {
            List<Employee> employees = new ArrayList<>();
            for(EmployeeDto employeeDto : employeeDtoList) {
                Employee employee = new Employee();

                employee.setCode(employeeDto.getCode());
                employee.setName(employeeDto.getName());
                employee.setEmail(employeeDto.getEmail());
                employee.setPhone(employeeDto.getPhone());
                employee.setAge(employeeDto.getAge());
                employee.setProvince(provinceRepository.getOne(employeeDto.getProvinceId()));
                employee.setDistrict(districtRepository.getOne(employeeDto.getDistrictId()));
                employee.setTown(townRepository.getOne(employeeDto.getTownId()));

                employees.add(employee);
            }
            List<Employee> employeeIterator = employeeRepository.saveAll(employees);
            return employeeIterator.stream().map(EmployeeDto::new).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<EmployeeDto> update(List<EmployeeDto> employeeDtoList) {
        if(!ObjectUtils.isEmpty(employeeDtoList)) {
            List<Employee> employees = new ArrayList<>();
            for(EmployeeDto employeeDto : employeeDtoList) {
                Employee employee = employeeRepository.getOne(employeeDto.getId());

                try {
                    employee.setCode(employeeDto.getCode());
                    employee.setName(employeeDto.getName());
                    employee.setEmail(employeeDto.getEmail());
                    employee.setPhone(employeeDto.getPhone());
                    employee.setAge(employeeDto.getAge());
                    if(!ObjectUtils.isEmpty(employeeDto.getTownId())
                            && !ObjectUtils.isEmpty(employeeDto.getDistrictId())
                            && !ObjectUtils.isEmpty(employeeDto.getProvinceId())) {
                        employee.setProvince(provinceRepository.getOne(employeeDto.getProvinceId()));
                        employee.setDistrict(districtRepository.getOne(employeeDto.getDistrictId()));
                        employee.setTown(townRepository.getOne(employeeDto.getTownId()));
                    }
                    employees.add(employee);
                } catch (EntityNotFoundException e) {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("Employee", "Not found with given ID");
                    throw new InvalidDtoException(errors);
                }
            }
            List<Employee> employeeIterator = employeeRepository.saveAll(employees);
            return employeeIterator.stream().map(EmployeeDto::new).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Boolean deleteById(UUID id) {
        if(!ObjectUtils.isEmpty(id) && employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Workbook getExcel() {
        return WriteExcelFile.writeToExcelFile(employeeRepository.getAll());
    }

    @Override
    public Boolean isValidEmployee(List<EmployeeDto> employeeDtoList, Class<?> group) {
        HashMap<String, String> errors = new HashMap<>();
        int line = 1;
        for(EmployeeDto employeeDto : employeeDtoList) {
            Set<ConstraintViolation<EmployeeDto>> violations = validator.validate(employeeDto, group);
            if(!violations.isEmpty()) {
                for(ConstraintViolation<EmployeeDto> constraintViolation : violations) {
                    errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
                }
            }

            String sql = "SELECT COUNT(entity) FROM Employee as entity WHERE entity.code = :code ";
            Employee employee = null;
            if (employeeRepository.existsById(employeeDto.getId())) {
                employee = employeeRepository.getOne(employeeDto.getId());
                sql += "AND entity.id != :id";
            }

            Query query = manager.createQuery(sql);

            query.setParameter("code", employeeDto.getCode());

            if(!ObjectUtils.isEmpty(employee)) {
                query.setParameter("id", employeeDto.getId());
            }

            long count = (long) query.getSingleResult();

            if(count > 0) {
                errors.put("code", "Code must not be duplicated");
            }
            Province province = null;
            District district = null;
            Town town = null;
            if(!ObjectUtils.isEmpty(employeeDto.getProvinceId())) {
                if (provinceRepository.existsById(employeeDto.getProvinceId())) {
                    province = provinceRepository.getOne(employeeDto.getProvinceId());
                } else {
                    errors.put("Province", "Province not found!");
                }
            }
            if (!ObjectUtils.isEmpty(employeeDto.getDistrictId())) {
                if (districtRepository.existsById(employeeDto.getDistrictId())) {
                    district = districtRepository.getOne(employeeDto.getDistrictId());
                } else {
                    errors.put("District", "District not found!");
                }
            }
            if (!ObjectUtils.isEmpty(employeeDto.getTownId())) {
                if (townRepository.existsById(employeeDto.getTownId())) {
                    town = townRepository.getOne(employeeDto.getTownId());
                } else {
                    errors.put("Town", "Town not found!");
                }
            }
            if (!ObjectUtils.isEmpty(district) && !ObjectUtils.isEmpty(town)
                    && !district.getId().equals(town.getDistrict().getId())) {
                errors.put("townId", "Town must belong to a district");
            }
            if (!ObjectUtils.isEmpty(province) && !ObjectUtils.isEmpty(district)
                    && !province.getId().equals(district.getProvince().getId())) {
                errors.put("districtId", "District must belong to a province");
            }
            line++;
            if(errors.size() > 0) {
                Map<String, String> errorLine = new HashMap<>();
                errorLine.put("Employee in line " + line, errors.values().toString());
                throw new InvalidDtoException(errorLine);
            }
        }
        return true;
    }
}
