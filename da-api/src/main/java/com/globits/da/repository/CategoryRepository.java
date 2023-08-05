package com.globits.da.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.globits.da.domain.Category;
import com.globits.da.dto.CategoryDto;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID>{
	@Query("SELECT count(entity.id) FROM Category entity WHERE entity.code =?1 AND (entity.id <> ?2 OR ?2 is null) ")
	Long checkCode(String code, UUID id);
	@Query("SELECT new com.globits.da.dto.CategoryDto(ed) FROM Category ed")
	Page<CategoryDto> getListPage( Pageable pageable);
	@Query("SELECT new com.globits.da.dto.CategoryDto(ed) FROM Category ed")
	List<CategoryDto> getAllCategory();
}
