package com.zxsoft.fanfanfamily.mort.repository;

import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
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

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface RegionDao extends JpaRepository<Region, String>,RegionDaoCustom {

    //一组惯用查询方法
    Optional<Region> findFirstByCode(String code);
    Optional<Region> findFirstByName(String name);
    //------------!!!   Containing 等价于Sql查询中的like.而不是find*ByNameLike.
    Optional<Region> findFirstByNameContaining(String namePart);
    Optional<Region> findFirstByCodeOrName(String code,String name);
    Page<Region> findByNameContaining(String nameLike, Pageable page);
    List<Region> findAllByNameContaining(String nameLike);
    Page<Region> findByCodeStartingWith(String codeStart,Pageable page);
    List<Region> findAllByCodeStartingWith(String codeStart);

    List<Region> findAllByIdNotNullOrderByCode();
    List<Region> findAllByParentRegionIsNullOrderByCode();
    
    //扩展
    List<Region> findRegionsByBanksIn(Bank bank);
    @EntityGraph(attributePaths = { "resources","banks","organizations" })
    Region queryRegionById(String id);
    @EntityGraph(attributePaths = { "resources"})
    Page<Region> queryRegionsByIdIsNotNull(Pageable page);
    @EntityGraph(attributePaths = { "resources","banks","organizations" })
    Page<Region> queryAllByIdIsNotNull(Pageable pageable);
    @EntityGraph(attributePaths = { "resources","banks","organizations" })
    List<Region> queryAllByBanksIn(Bank bank);
    @EntityGraph(attributePaths = { "resources","banks","organizations" })
    Optional<Region> queryFirstByCode(String code);
    @EntityGraph(attributePaths = { "resources","banks","organizations" })
    Optional<Region> queryFirstByNameContaining(String nameLike);

    @Transactional
    @Modifying
    @Query("update Region u set u.logoUrl = ?1 where u.id = ?2")
    int modifyLogoUrlById(String logoUrl, String id);

    @EntityGraph(attributePaths = { "parentRegion"})
    @Query(value = "select u from Region u where u.parentRegion.id = ?1 order by u.code")
    List<Region> customQueryAllByParentRegionId(String id);

//    @Query("select r.code,count(s.Id) from region r left join bank s on r.id = s.regsionId where r.id = ?1 group by r")
//    @Query("select r as region,count(s.id) as BankCount from Region r left join Bank s on r.id = s.region\n" +
//            "where r.id = :id group by r")
//    Page<RegionCount> findRegionCountById(@Param("id") String id, Pageable pageable);


}
