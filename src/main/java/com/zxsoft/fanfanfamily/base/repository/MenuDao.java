package com.zxsoft.fanfanfamily.base.repository;

import com.zxsoft.fanfanfamily.base.domain.Menu;
import com.zxsoft.fanfanfamily.base.domain.Menu;
import com.zxsoft.fanfanfamily.base.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MenuDao extends JpaRepository<Menu, String> {

    //一组惯用查询方法
    Optional<Menu> findFirstByName(String name);
    //------------!!!   Containing 等价于Sql查询中的like.而不是find*ByNameLike.
    Optional<Menu> findFirstByNameContaining(String namePart);
    Page<Menu> findByNameContaining(String nameLike, Pageable page);
    List<Menu> findAllByNameContaining(String nameLike);

    List<Menu> findAllByIdIsNotNullOrderBySortNo();
    List<Menu> findAllByParentMenuIsNullOrderBySortNo();

    Page<Menu> findAllByNameContainingAndCreateTimeAfterAndCreateTimeBefore(String name, Date startTime, Date endTime, Pageable pageable);

    @Query(value = "select u from Menu u order by sortNo")
    List<Menu> customQuerySingleMenus();

    //扩展
    List<Menu> findAllByParentMenuEqualsOrderBySortNo(Menu menu);

    List<Menu> queryAllByParentMenu(String parentMenu);//根据父级菜单查子菜单
    List<Menu> queryAllByParentMenuIsNullOrderBySortNo();//顶级菜单

//    List<Menu> findAllByParentMenuIdOrderBySortNo(String id);

    @EntityGraph(attributePaths = { "parentMenu"})
    @Query(value = "select u from Menu u where u.parentMenu.id = ?1 order by u.sortNo")
    List<Menu> customQueryAllByParentMenuId(String id);

}
