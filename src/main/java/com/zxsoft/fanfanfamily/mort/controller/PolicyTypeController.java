package com.zxsoft.fanfanfamily.mort.controller;

import com.zxsoft.fanfanfamily.base.controller.BaseRestControllerImpl;
import com.zxsoft.fanfanfamily.base.domain.mort.LoanPolicyNeedCondition;
import com.zxsoft.fanfanfamily.base.domain.mort.PolicyType;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.mort.service.LoanPolicyNeedConditionService;
import com.zxsoft.fanfanfamily.mort.service.PolicyTypeService;
import com.zxsoft.fanfanfamily.mort.service.impl.PolicyTypeServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/policytype")
public class PolicyTypeController extends BaseRestControllerImpl<PolicyType> {


    @Autowired
    private PolicyTypeService policyTypeService;

    @Override
    public BaseService<PolicyType> getBaseService() {
        return policyTypeService;
    }

    @Override
    public Class<PolicyType> getEntityType() {
        return PolicyType.class;
    }


}
