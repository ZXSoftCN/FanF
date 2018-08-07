package com.zxsoft.fanfanfamily.base.controller;

import com.zxsoft.fanfanfamily.base.domain.Permission;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/permission")
public class PermissionController extends BaseRestControllerImpl<Permission> {


    @Autowired
    private PermissionService permissionService;

    @Override
    public BaseService getBaseService() {
        return permissionService;
    }

    @Override
    public Class<Permission> getEntityType() {
        return Permission.class;
    }


}
