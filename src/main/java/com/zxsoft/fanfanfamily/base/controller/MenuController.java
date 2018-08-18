package com.zxsoft.fanfanfamily.base.controller;

import com.zxsoft.fanfanfamily.base.domain.Menu;
import com.zxsoft.fanfanfamily.base.domain.Organization;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.service.MenuService;
import com.zxsoft.fanfanfamily.base.service.OrganizationService;
import com.zxsoft.fanfanfamily.base.sys.FanfAppBody;
import com.zxsoft.fanfanfamily.config.converter.FanFResponseBodyBuilder;
import com.zxsoft.fanfanfamily.config.converter.FanfAppData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/menu")
public class MenuController extends BaseRestControllerImpl<Menu> {


    @Autowired
    private MenuService menuService;

    @Override
    public BaseService getBaseService() {
        return menuService;
    }

    @Override
    public Class<Menu> getEntityType() {
        return Menu.class;
    }

    @PostMapping("/addwithparent/{parentId}")
    public FanfAppData addWithParent(@PathVariable(name = "parentId") Menu parentMenu,
                                     @RequestBody Menu menu) {
        try {
            Optional<Menu> itemMenu = menuService.addWithParent(menu, parentMenu);
            if (itemMenu.isPresent()) {
                return FanFResponseBodyBuilder.ok(String.format("创建菜单【%s】成功！", menu.getName()), itemMenu.get());
            }
        } catch (Exception ex) {
            return FanFResponseBodyBuilder.error(String.format("创建菜单失败：%s", ex.getMessage()), null);
        }
        return FanFResponseBodyBuilder.error("创建菜单失败！", null);
    }

    @PostMapping("/addbatchwithparent/{parentId}")
    public FanfAppData addBatchWithParent(@PathVariable(name = "parentId") Menu parentMenu,
                                          @RequestBody List<Menu> menus) {
        try {
            List<Menu> lstMenu = menuService.addBatchWithParent(menus, parentMenu);

            return FanFResponseBodyBuilder.ok(String.format("创建菜单成功:%d", lstMenu.size()), lstMenu);

        } catch (Exception ex) {
            return FanFResponseBodyBuilder.error(String.format("创建菜单失败：%s", ex.getMessage()), null);
        }
    }

    @PostMapping("/addbatch")
    public FanfAppData addBatch(@RequestBody List<Menu> menus) {
        try {
            List<Menu> lstMenu = menuService.addBatch(menus);

            return FanFResponseBodyBuilder.ok(String.format("创建菜单成功:%d", lstMenu.size()), lstMenu);

        } catch (Exception ex) {
            return FanFResponseBodyBuilder.error(String.format("创建菜单失败：%s", ex.getMessage()), null);
        }
    }

    @RequestMapping("/queryTopMenuAllTree")
    @FanfAppBody
    public ResponseEntity<List<Menu>> queryTopMenuAllTree() {

        List<Menu> lstMenu = menuService.queryTopMenuAllTree();

        if (lstMenu != null) {
            return ResponseEntity.ok(lstMenu);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
