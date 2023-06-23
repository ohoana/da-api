package com.globits.da.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private EntityManager manager;

    @Override
    public EmployeeDto findByCode(String code) {
        return employeeRepository.findByCode(code);
    }

    @Override
    public Page<EmployeeDto> getPage(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        return employeeRepository.getListPage(pageable);
    }

    @Override
    public Page<EmployeeDto> searchEmployee(EmployeeSearchDto searchDto) {
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

        List<EmployeeDto> employeeDtoList = sqlQuery.getResultList();
        long count = (long) sqlCountQuery.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<EmployeeDto> result = new PageImpl<>(employeeDtoList, pageable, count);
        return result;
    }

    @Override
    public List<EmployeeDto> getAllEmployee() {
        return employeeRepository.getAllEmployee();
    }

    @Override
    public EmployeeDto saveOrUpdate(EmployeeDto employeeDto, UUID id){
        if(!ObjectUtils.isEmpty(employeeDto)) {
            Employee employee = null;
            if (!ObjectUtils.isEmpty(id)) {
                if(!ObjectUtils.isEmpty(employeeDto.getDistrictId()) && !id.equals(employeeDto.getId())) {
                    return null;
                }
                try {
                    employee = employeeRepository.getOne(id);
                } catch(EntityNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            if(ObjectUtils.isEmpty(employee)) {
                employee = new Employee();
            }

            employee.setCode(employeeDto.getCode());
            employee.setName(employeeDto.getName());
            employee.setEmail(employeeDto.getEmail());
            employee.setPhone(employeeDto.getPhone());
            employee.setAge(employeeDto.getAge());
            if(!ObjectUtils.isEmpty(employeeDto.getTownId())
            && !ObjectUtils.isEmpty(employeeDto.getProvinceId())
            && !ObjectUtils.isEmpty(employeeDto.getDistrictId())
            && isValidEmployee(employeeDto)) {
                employee.setProvince(provinceRepository.getOne(employeeDto.getProvinceId()));
                employee.setDistrict(districtRepository.getOne(employeeDto.getDistrictId()));
                employee.setTown(townRepository.getOne(employeeDto.getTownId()));
            }

            employee = employeeRepository.save(employee);
            if(!ObjectUtils.isEmpty(employee)) {
                return new EmployeeDto(employee);
            }
        }
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        if(!ObjectUtils.isEmpty(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Workbook getExcel() {
        Workbook workbook = WriteExcelFile.writeToExcelFile(employeeRepository.getAllEmployee());
        return workbook;
    }

    @Override
    public Boolean isValidEmployee(EmployeeDto employeeDto) {
        Province province = provinceRepository.getOne(employeeDto.getProvinceId());
        District district = districtRepository.getOne(employeeDto.getDistrictId());
        Town town = townRepository.getOne(employeeDto.getTownId());
        HashMap<String, String> errors = new HashMap<>();
        if(!town.getDistrict().getId().equals(district.getId())) {
            errors.put("townId", "Town must belong to a district");
            throw new InvalidDtoException(errors);
        }
        if(!district.getProvince().getId().equals(province.getId())) {
            errors.put("districtId", "District must belong to a province");
            throw new InvalidDtoException(errors);
        }
        return true;
    }
}
