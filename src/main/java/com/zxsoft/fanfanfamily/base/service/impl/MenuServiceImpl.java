package com.zxsoft.fanfanfamily.base.service.impl;

import com.zxsoft.fanfanfamily.base.domain.Menu;
import com.zxsoft.fanfanfamily.base.domain.Permission;
import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.domain.vo.MenuWithChildDTO;
import com.zxsoft.fanfanfamily.base.domain.vo.UserPermissionDTO;
import com.zxsoft.fanfanfamily.base.domain.vo.UserPermissionInner;
import com.zxsoft.fanfanfamily.base.repository.EntityIncreaseDao;
import com.zxsoft.fanfanfamily.base.repository.MenuDao;
import com.zxsoft.fanfanfamily.base.service.MenuService;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {

    private final String resPathName = "menu";
//    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //静态变量存储最大值
    private static final AtomicInteger atomicSortNo = new AtomicInteger();

    @Autowired
    private EntityIncreaseDao entityIncreaseDao;
    @Autowired
    private MenuDao menuDao;

//    public MenuServiceImpl(){
//    }

    //<editor-fold desc="私有方法">
    //</editor-fold>

    /**
     * @Author  javaloveiphone
     * @Description :初始化设置菜单编号最大值
     * @throws Exception
     * void
     */
    @PostConstruct
    public void initMax(){
        initSortNoMax();
    }

    @Override
    public String getEntityName() {
        return resPathName;
    }

    @Override
    public AtomicInteger getSortNoMax() {
        return atomicSortNo;
    }

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
    public String uploadAvatarExtend(Menu menu, String fileName, String postfix, byte[] bytes) {
        return null;
    }

    @Override
    public String uploadAvatarExtend(Menu menu, MultipartFile file) {
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

//    @Override
//    public Menu modify(Menu menu) {
//        Optional<Menu> rlt = modifyWithParent(menu,menu.getParentMenuId());
//        return rlt.orElse(null);
//    }

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

    private void fetchSubMenus(MenuWithChildDTO menu) {
        if (menu.getSubMenus() != null && menu.getSubMenus().size() > 0) {
            for (MenuWithChildDTO item : menu.getSubMenus()) {
                fetchSubMenus(item);
            }
        }
    }

    /**
     * 查询顶级菜单及其子菜单
     * @return
     */
    @Override
    public List<MenuWithChildDTO> queryTopMenuAllTree() {
        List<MenuWithChildDTO> lstRlt = new ArrayList<>();
        List<Menu> topMenus = menuDao.findAllByParentMenuIsNullOrderBySortNo();
        for (Menu item : topMenus) {
            MenuWithChildDTO dtoItem = MenuWithChildDTO.convert(item);
            fetchSubMenus(dtoItem);
            lstRlt.add(dtoItem);
        }
        return lstRlt;
    }

    @Override
    public List<Menu> queryTopMenuOnly() {
        return menuDao.queryAllByParentMenuIsNullOrderBySortNo();
    }

    @Override
    public List<Menu> findAll() {
        return menuDao.findAllByIdIsNotNullOrderBySortNo();
    }

    @Override
    public Page<Menu> findMenuByCreateTime(String name, String[] dateTimes, Pageable pageable) {
        try {
            Date startTime = DateUtils.parseDate("2000-01-01","yyyy-MM-dd");
            Date endTime = DateUtils.parseDate("2999-12-31","yyyy-MM-dd");
            if (dateTimes != null && dateTimes.length > 0) {
                startTime = DateUtils.parseDate(dateTimes[0],"yyyy-MM-dd");
                endTime = DateUtils.parseDate(dateTimes[1],"yyyy-MM-dd");
            }
            Page<Menu> infos = menuDao.findAllByNameContainingAndCreateTimeAfterAndCreateTimeBefore(name, startTime, endTime, pageable);
            return infos;
        } catch (ParseException ex) {
            return null;
        }
    }
}
