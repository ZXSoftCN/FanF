package com.zxsoft.fanfanfamily.mort.repository;

import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.mort.domain.custom.RegionDaoCustom;
import com.zxsoft.fanfanfamily.mort.domain.vo.RegionCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RegionDao extends JpaRepository<Region, String>,RegionDaoCustom {

    @Override
    Optional<Region> findById(String id);
    Optional<Region> findRegionByCode(String code);

    @Override
    List<Region> findAll(Sort sort);
    @Override
    Page<Region> findAll(org.springframework.data.domain.Pageable pageable);

    @EntityGraph(attributePaths = { "resources" })
    Page<Region> queryAllByIdIsNotNull(Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Region u set u.logoUrl = ?1 where u.id = ?2")
    int modifyLogoUrlById(String logoUrl, String id);

//    @Query("select r.code,count(s.Id) from region r left join bank s on r.id = s.regsionId where r.id = ?1 group by r")
    @Query("select r as region,count(s.id) as BankCount from Region r left join Bank s on r.id = s.region\n" +
            "where r.id = :id group by r")
    Page<RegionCount> findRegionCountById(@Param("id") String id, Pageable pageable);

}
