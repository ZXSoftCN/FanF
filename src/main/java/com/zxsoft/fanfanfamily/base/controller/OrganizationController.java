package com.zxsoft.fanfanfamily.base.controller;

import com.zxsoft.fanfanfamily.base.domain.Organization;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/organization")
public class OrganizationController extends BaseRestControllerImpl<Organization> {


    @Autowired
    private OrganizationService organizationService;

    @Override
    public BaseService getBaseService() {
        return organizationService;
    }

    @Override
    public Class<Organization> getEntityType() {
        return Organization.class;
    }


}
