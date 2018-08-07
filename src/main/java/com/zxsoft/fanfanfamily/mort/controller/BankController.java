package com.zxsoft.fanfanfamily.mort.controller;

import com.zxsoft.fanfanfamily.base.controller.BaseRestControllerImpl;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.mort.repository.BankDao;
import com.zxsoft.fanfanfamily.mort.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/bank")
public class BankController  extends BaseRestControllerImpl<Bank> {


    @Autowired
    private BankService bankService;

    @Override
    public BaseService getBaseService() {
        return bankService;
    }

    @Override
    public Class<Bank> getEntityType() {
        return Bank.class;
    }

}
