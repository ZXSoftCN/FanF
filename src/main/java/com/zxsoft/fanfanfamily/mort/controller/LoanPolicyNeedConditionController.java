package com.zxsoft.fanfanfamily.mort.controller;

import com.zxsoft.fanfanfamily.base.controller.BaseRestControllerImpl;
import com.zxsoft.fanfanfamily.base.domain.mort.LoanPolicyNeedCondition;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.mort.service.LoanPolicyNeedConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/loanPolicyNeedCondition")
public class LoanPolicyNeedConditionController extends BaseRestControllerImpl<LoanPolicyNeedCondition> {

    @Autowired
    private LoanPolicyNeedConditionService loanPolicyNeedConditionService;

    @Override
    public BaseService<LoanPolicyNeedCondition> getBaseService() {
        return loanPolicyNeedConditionService;
    }

    @Override
    public Class<LoanPolicyNeedCondition> getEntityType() {
        return LoanPolicyNeedCondition.class;
    }


}
