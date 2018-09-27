package com.zxsoft.fanfanfamily.base.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxsoft.fanfanfamily.base.repository.EntityIncreaseDao;
import com.zxsoft.fanfanfamily.base.repository.MenuDao;
import com.zxsoft.fanfanfamily.base.service.MenuService;
import com.zxsoft.fanfanfamily.common.SpringUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "sys_Menu")
@NamedEntityGraph(name = "Menu.lazy",
        attributeNodes = {@NamedAttributeNode("parentMenu")})
public class Menu extends SimpleEntity {
    private static final long serialVersionUID = 4536510796054836998L;

    private int sortNo;
    private String pathKey;//菜单路径key
    private String iconType;//Icon图标
    private String iconClassName;//Icon来源ClassName图标，作为IconType的补充
    private Menu parentMenu;
    private Boolean status;
    private Boolean showMenu;
    private String componentPath;//记录菜单上绑定的组件页面(如../dashboard/index.js).供后续扩展动态加载菜单及组件路径。

//    @Autowired
//    private SpringUtil springUtil;
//    private List<Menu> subMenus = new ArrayList<>();

    @Column(name = "sortNo",columnDefinition = "int unsigned DEFAULT 0")
    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

    @Column(name = "pathKey",unique = true,nullable = false,columnDefinition = "varchar(255)")
    public String getPathKey() {
        return pathKey;
    }

    public void setPathKey(String pathKey) {
        this.pathKey = pathKey;
    }

    @Column(name = "iconType",columnDefinition = "varchar(36) DEFAULT ''")
    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType;
    }

    @Column(name = "iconClassName",columnDefinition = "varchar(36) DEFAULT ''")
    public String getIconClassName() {
        return iconClassName;
    }

    public void setIconClassName(String iconClassName) {
        this.iconClassName = iconClassName;
    }

    @JSONField(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH})
    @JoinColumn(name = "parentMenuId",columnDefinition = "varchar(36) DEFAULT ''",
            foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT))
    public Menu getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

    @Transient
    public String getParentMenuId() {
        if (parentMenu != null) {
            return parentMenu.getId();
        }
        return "";
    }
    @Transient
    public void setParentMenuId(String parentMenuId) {
        if (!StringUtils.isEmpty(parentMenuId)) {
            MenuDao menuDao = (MenuDao)SpringUtil.getBean("menuDao");

            Optional<Menu> menuOp = menuDao.findById(parentMenuId);
            if (menuOp.isPresent()) {
                this.parentMenu = menuOp.get();
            }
        }
    }

    @Column(name = "status",columnDefinition = "tinyint(1) DEFAULT 1")
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Column(name = "showMenu",columnDefinition = "tinyint(1) DEFAULT 1")
    public Boolean getShowMenu() {
        return showMenu;
    }

    public void setShowMenu(Boolean showMenu) {
        this.showMenu = showMenu;
    }



//    @Transient
//    public List<Menu> getSubMenus() {
//        if (getId() != null) {
//            MenuDao menuDao = (MenuDao)SpringUtil.getBean("menuDao");
//
//            List<Menu> lstSub = menuDao.findAllByParentMenuEqualsOrderBySortNo(this);
//            if (lstSub.size() > 0) {
//                return lstSub;
//            }
//        }
//        return null;
//    }
//
//    public void setSubMenus(List<Menu> subMenus) {
//        this.subMenus = subMenus;
//    }

    public String getComponentPath() {
        return componentPath;
    }

    public void setComponentPath(String componentPath) {
        this.componentPath = componentPath;
    }

    //region Override方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(getId(), menu.getId());
    }

    @Override
    public void onSetDefault() {
        super.onSetDefault();
        MenuService menuService = (MenuService)SpringUtil.getBean("menuServiceImpl");
        this.setSortNo(menuService.getNewSortNo());
        this.setIconType("appstore-o");
        this.setStatus(true);
    }

    @Override
    public void onPostPersist() {
        EntityIncreaseDao entityIncreaseDao = (EntityIncreaseDao)SpringUtil.getBean("entityIncreaseDao");
        entityIncreaseDao.updateSortNoMax("menu",this.sortNo);
        super.onPostPersist();
    }

    @Override
    public String toString() {
        if (this.getId() != null) {
            return this.getId();
        }
        return super.toString();
    }
    //endregion
}
