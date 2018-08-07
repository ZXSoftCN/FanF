package com.zxsoft.fanfanfamily.base.controller;

import com.zxsoft.fanfanfamily.base.domain.Role;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/role")
public class RoleController extends BaseRestControllerImpl<Role> {

    @Autowired
    private RoleService roleService;

    @Override
    public BaseService getBaseService() {
        return roleService;
    }

    @Override
    public Class<Role> getEntityType() {
        return Role.class;
    }


}
