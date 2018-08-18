package com.zxsoft.fanfanfamily.base.service;

import com.zxsoft.fanfanfamily.base.domain.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuService extends BaseService<Menu> {

    Optional<Menu> addWithParent(Menu menu,String parentMenuId);
    Optional<Menu> modifyWithParent(Menu menu,String parentMenuId);

    Optional<Menu> addWithParent(Menu menu,Menu parentMenu);
    Optional<Menu> modifyWithParent(Menu menu,Menu parentMenu);

    List<Menu> addBatchWithParent(List<Menu> menus,Menu parentMenu);
    List<Menu> addBatch(List<Menu> menus);

    List<Menu> queryTopMenuAllTree();
    List<Menu> queryTopMenuOnly();
}
