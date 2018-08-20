package com.zxsoft.fanfanfamily.base.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxsoft.fanfanfamily.base.repository.MenuDao;
import com.zxsoft.fanfanfamily.common.SpringUtil;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "sys_Menu")
public class Menu extends SimpleEntity {
    private static final long serialVersionUID = 3784575834854689037L;

    private int sortNo;
    private String pathKey;//菜单路径key
    private String iconType;//Icon图标
    private String iconClassName;//Icon来源ClassName图标，作为IconType的补充
    private Menu parentMenu;
    private Boolean status;

    @Autowired
    private SpringUtil springUtil;
    private List<Menu> subMenus = new ArrayList<>();

    @Column(name = "sortNo",columnDefinition = "int default(0)")
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
    @JoinColumn(name = "parentMenuId",columnDefinition = "varchar(36) DEFAULT ''")
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

    @Column(name = "status",columnDefinition = "tinyint(1) DEFAULT 1")
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Transient
    public List<Menu> getSubMenus() {
        if (getId() != null) {
            MenuDao menuDao = (MenuDao)springUtil.getBean("menuDao");

            return menuDao.findAllByParentMenuEqualsOrderBySortNo(this);
        }
        return null;
    }

    public void setSubMenus(List<Menu> subMenus) {
        this.subMenus = subMenus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(getId(), menu.getId());
    }
}
