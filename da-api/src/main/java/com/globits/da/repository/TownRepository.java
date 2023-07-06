package com.globits.da.repository;

import com.globits.da.domain.Town;
import com.globits.da.dto.TownDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TownRepository extends JpaRepository<Town, UUID> {
    @Query("SELECT new com.globits.da.dto.TownDto(entity) FROM Town entity")
    List<TownDto> getAll();
    @Query("SELECT new com.globits.da.dto.TownDto(entity) FROM Town entity WHERE entity.district.id = ?1")
    List<TownDto> getByDistrictId(UUID districtId);
}
