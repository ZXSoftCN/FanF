package com.zxsoft.fanfanfamily.base.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.zxsoft.fanfanfamily.base.domain.Menu;
import com.zxsoft.fanfanfamily.base.repository.MenuDao;
import com.zxsoft.fanfanfamily.common.SpringUtil;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ReflectionUtils.COPYABLE_FIELDS;
import static org.springframework.util.ReflectionUtils.makeAccessible;

public class MenuWithChildDTO extends SimpleEntityDTO {
    private int sortNo;
    private String pathKey;//菜单路径key
    private String iconType;//Icon图标
    private String iconClassName;//Icon来源ClassName图标，作为IconType的补充
    private String parentMenu;
    private Boolean status;
    private Boolean showMenu;
    private List<MenuWithChildDTO> subMenus = new ArrayList<>();

    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

    public String getPathKey() {
        return pathKey;
    }

    public void setPathKey(String pathKey) {
        this.pathKey = pathKey;
    }

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType;
    }

    public String getIconClassName() {
        return iconClassName;
    }

    public void setIconClassName(String iconClassName) {
        this.iconClassName = iconClassName;
    }

    public String getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(String parentMenu) {
        this.parentMenu = parentMenu;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getShowMenu() {
        return showMenu;
    }

    public void setShowMenu(Boolean showMenu) {
        this.showMenu = showMenu;
    }

    public List<MenuWithChildDTO> getSubMenus() {
        if (getId() != null) {
            MenuDao menuDao = (MenuDao) SpringUtil.getBean("menuDao");
            List<Menu> lstSub = menuDao.customQueryAllByParentMenuId(getId());
            if (lstSub.size() > 0) {
                List<MenuWithChildDTO> lstRlt = new ArrayList<>();
//                for (Menu item : lstSub) {
//                    lstRlt.add(MenuWithChildDTO.convert(item));
//                }
                lstSub.forEach(item -> lstRlt.add(MenuWithChildDTO.convert(item)));
                return lstRlt;
            }
        }
        return null;
    }

    public void setSubMenus(List<MenuWithChildDTO> subMenus) {
        this.subMenus = subMenus;
    }

    /**
     * 将menu转化成包含SubMenus的简易DTO。注意：parentMenu是懒加载无法直接获取到ID，所以做了特别处理。
     * @param menu
     * @return
     */
    public static MenuWithChildDTO convert(Menu menu) {
        MenuWithChildDTO item = new MenuWithChildDTO();
        PropertyDescriptor arrSrcProSet[] = ReflectUtils.getBeanSetters(menu.getClass());
        PropertyDescriptor arrDestProSet[] = ReflectUtils.getBeanSetters(item.getClass());

        try {
            for (int i = 0; i < arrSrcProSet.length; i++) {
                for (int j = 0; j < arrDestProSet.length; j++) {
                    if (arrSrcProSet[i].getName().equalsIgnoreCase(arrDestProSet[j].getName())) {
                        if (arrSrcProSet[i].getName().equalsIgnoreCase("parentMenu")) {
                            Menu parentMenu = menu.getParentMenu();
                            if (parentMenu != null) {
                                arrDestProSet[j].getWriteMethod().invoke(item, parentMenu.toString());
                            }
                        } else {
                            Object srcValue = arrSrcProSet[i].getReadMethod().invoke(menu);
                            arrDestProSet[j].getWriteMethod().invoke(item,srcValue);
                        }
                    }
                }
            }
        } catch (InvocationTargetException ex) {
            item = null;
        }catch (IllegalAccessException ex) {
            item = null;
        }

        return item;
    }
}
