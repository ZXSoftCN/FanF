package com.zxsoft.fanfanfamily.base.service.impl;

import com.zxsoft.fanfanfamily.base.domain.Menu;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.repository.MenuDao;
import com.zxsoft.fanfanfamily.base.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {

    private final String resPathName = "menu";
//    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MenuDao menuDao;

//    public MenuServiceImpl(){
//    }

    //<editor-fold desc="私有方法">
    //</editor-fold>

    @Override
    protected void initPath() {
        super.initPath();
        rootUploadPath = super.getPath().resolve(resPathName);
        avatarUploadPath = super.getPath().resolve(super.avatar);
    }

    @Override
    public JpaRepository<Menu, String> getBaseDao() {
        return menuDao;
    }

    @Override
    public Path uploadAvatarExtend(Menu menu, String fileName, String postfix, byte[] bytes) {
        return null;
    }

    @Override
    public Path uploadAvatarExtend(Menu menu, MultipartFile file) {
        return null;
    }

    @Override
    public Path loadAvatar(Menu menu, AvatorLoadFactor factor) {

        return null;
    }

    @Override
    public Optional<Menu> addWithParent(Menu menu, String parentMenuId) {

        Optional<Menu> parentMenu = menuDao.findById(parentMenuId);
        if (parentMenu.isPresent()) {
            return addWithParent(menu,parentMenu.get());
        }
        return addWithParent(menu,(Menu)null);
    }

    @Override
    public Optional<Menu> modifyWithParent(Menu menu, String parentMenuId) {
        Optional<Menu> parentMenu = menuDao.findById(parentMenuId);
        if (parentMenu.isPresent()) {
            return modifyWithParent(menu,parentMenu.get());
        }
        return modifyWithParent(menu,(Menu)null);
    }

    @Override
    public Optional<Menu> addWithParent(Menu menu, Menu parentMenu) {
        if (menu == null) {
            return Optional.empty();
        }
        if (parentMenu != null) {
            menu.setParentMenu(parentMenu);
        }
        Menu newItem = menuDao.save(menu);
        return Optional.of(newItem);
    }

    @Override
    public Optional<Menu> modifyWithParent(Menu menu, Menu parentMenu) {
        if (menu == null) {
            return Optional.empty();
        }
        if (parentMenu != null) {
            menu.setParentMenu(parentMenu);
        }
        Menu newItem = menuDao.save(menu);
        return Optional.of(newItem);
    }

    @Override
    public List<Menu> addBatchWithParent(List<Menu> menus, Menu parentMenu) {
        if (parentMenu != null) {
            menus.forEach(item -> item.setParentMenu(parentMenu));
        }
        return menuDao.saveAll(menus);
    }

    @Override
    public List<Menu> addBatch(List<Menu> menus) {
        return menuDao.saveAll(menus);
    }

    /**
     * 查询顶级菜单及其子菜单（最多到六级）
     * @return
     */
    @Override
    public List<Menu> queryTopMenuAllTree() {
        List<Menu> topMenus = menuDao.queryAllByParentMenuIsNullOrderBySortNo();
        for (Menu item1 : topMenus) {//一级
            if (item1.getSubMenus() == null || item1.getSubMenus().size() == 0) {
                continue;
            }
            for (Menu item2 : item1.getSubMenus()) {//二级
                if (item2.getSubMenus() == null || item2.getSubMenus().size() == 0) {
                    continue;
                }
                item2.setSubMenus(item2.getSubMenus());//手工加载
                for (Menu item3 : item2.getSubMenus()) {//三级
                    if (item3.getSubMenus() == null || item3.getSubMenus().size() == 0) {
                        continue;
                    }
                    item3.setSubMenus(item3.getSubMenus());
                    for (Menu item4 : item3.getSubMenus()) {//四级
                        if (item4.getSubMenus() == null || item4.getSubMenus().size() == 0) {
                            continue;
                        }
                        item4.setSubMenus(item4.getSubMenus());
                        for (Menu item5 : item4.getSubMenus()) {//五级
                            if (item5.getSubMenus() == null || item5.getSubMenus().size() == 0) {
                                continue;
                            }
                            item5.setSubMenus(item5.getSubMenus());//六级
                            //可继续往下加。
                        }
                    }
                }
            }
        }
        return topMenus;
    }

    @Override
    public List<Menu> queryTopMenuOnly() {
        return menuDao.queryAllByParentMenuIsNullOrderBySortNo();
    }
}
