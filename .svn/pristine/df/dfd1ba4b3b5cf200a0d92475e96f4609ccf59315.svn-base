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
    @Query("select new com.globits.da.dto.TownDto(entity) from Town entity")
    List<TownDto> getAll();

    @Query("select new com.globits.da.dto.TownDto(entity) from Town entity where entity.district.id = ?1")
    List<TownDto> getTownByDistrictId(UUID districtId);
}
