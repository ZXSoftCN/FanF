package com.zxsoft.fanfanfamily.mort.service;

import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.mort.domain.vo.BankWithChildDTO;

import java.util.List;

public interface BankService extends BaseService<Bank> {

    List<BankWithChildDTO> queryTree();
    List<Bank> querySubs(String id);
}
