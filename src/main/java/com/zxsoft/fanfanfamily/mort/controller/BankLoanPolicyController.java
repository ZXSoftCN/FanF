package com.zxsoft.fanfanfamily.mort.controller;

import com.zxsoft.fanfanfamily.base.controller.BaseRestControllerImpl;
import com.zxsoft.fanfanfamily.base.domain.mort.BankLoanPolicy;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.mort.service.BankLoanPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/bankLoanPolicy")
public class BankLoanPolicyController extends BaseRestControllerImpl<BankLoanPolicy> {

    @Autowired
    private BankLoanPolicyService bankLoanPolicyService;

    @Override
    public BaseService<BankLoanPolicy> getBaseService() {
        return bankLoanPolicyService;
    }

    @Override
    public Class<BankLoanPolicy> getEntityType() {
        return BankLoanPolicy.class;
    }


}
