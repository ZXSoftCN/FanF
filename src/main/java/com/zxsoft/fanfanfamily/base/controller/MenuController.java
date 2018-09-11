package com.zxsoft.fanfanfamily.base.controller;

import com.zxsoft.fanfanfamily.base.domain.Menu;
import com.zxsoft.fanfanfamily.base.domain.Organization;
import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import com.zxsoft.fanfanfamily.base.domain.vo.MenuWithChildDTO;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.service.MenuService;
import com.zxsoft.fanfanfamily.base.service.OrganizationService;
import com.zxsoft.fanfanfamily.base.sys.FanfAppBody;
import com.zxsoft.fanfanfamily.config.converter.FanFResponseBodyBuilder;
import com.zxsoft.fanfanfamily.config.converter.FanfAppData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
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
    public ResponseEntity<List<MenuWithChildDTO>> queryTopMenuAllTree() {

        List<MenuWithChildDTO> lstMenu = menuService.queryTopMenuAllTree();

        if (lstMenu != null) {
            return ResponseEntity.ok(lstMenu);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @FanfAppBody
    @RequestMapping(value = "/queryParams")
    public ResponseEntity<Page<Menu>> queryPageParams(@PageableDefault(size = 15,page = 0,sort = "sortNo",direction = Sort.Direction.ASC)
                                                                  Pageable pageable, @RequestParam(name = "name", required = false,defaultValue = "") String name,
                                                          @RequestParam(name = "createTime", required = false) String[] arrCreateTime) {
        /*DateTime[] createTimes = new DateTime[arrCreateTime.length];
        for (int i = 0; i < arrCreateTime.length; i++) {
            createTimes[i] = DateTime.parse(arrCreateTime[i]);
        }*/
        //请求页码默认以第1页开始，所以除了录入0页码时，均向前翻一页。
        if (pageable.getPageNumber() > 0) {
            pageable = pageable.previousOrFirst();
        }
        Page<Menu> pageColl = menuService.findMenuByCreateTime(name,arrCreateTime,pageable);

        return ResponseEntity.ok(pageColl);
    }

}
