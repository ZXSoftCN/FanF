package com.zxsoft.fanfanfamily.base.repository;

import com.zxsoft.fanfanfamily.base.domain.Menu;
import com.zxsoft.fanfanfamily.base.domain.Menu;
import com.zxsoft.fanfanfamily.base.domain.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MenuDao extends JpaRepository<Menu, String> {

    //一组惯用查询方法
    Optional<Menu> findFirstByName(String name);
    //------------!!!   Containing 等价于Sql查询中的like.而不是find*ByNameLike.
    Optional<Menu> findFirstByNameContaining(String namePart);
    Page<Menu> findByNameContaining(String nameLike, Pageable page);
    List<Menu> findAllByNameContaining(String nameLike);

    //扩展
    List<Menu> findAllByParentMenuEquals(Menu menu);

    List<Menu> queryAllByParentMenu(String parentMenu);//根据父级菜单查子菜单
    List<Menu> queryAllByParentMenuIsNull();//顶级菜单

}
