package com.zxsoft.fanfanfamily.mort.controller;

import com.zxsoft.fanfanfamily.base.controller.BaseRestControllerImpl;
import com.zxsoft.fanfanfamily.base.domain.mort.LoanPolicyNeedAttach;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.mort.service.LoanPolicyNeedAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/loanPolicyNeedAttach")
public class LoanPolicyNeedAttachController extends BaseRestControllerImpl<LoanPolicyNeedAttach> {

    @Autowired
    private LoanPolicyNeedAttachService loanPolicyNeedAttachService;

    @Override
    public BaseService<LoanPolicyNeedAttach> getBaseService() {
        return loanPolicyNeedAttachService;
    }

    @Override
    public Class<LoanPolicyNeedAttach> getEntityType() {
        return LoanPolicyNeedAttach.class;
    }


}
