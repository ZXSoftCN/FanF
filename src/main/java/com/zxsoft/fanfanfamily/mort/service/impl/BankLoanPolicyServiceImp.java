package com.zxsoft.fanfanfamily.mort.service.impl;

import com.zxsoft.fanfanfamily.base.domain.mort.BankLoanPolicy;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.service.impl.BaseServiceImpl;
import com.zxsoft.fanfanfamily.mort.repository.BankLoanPolicyDao;
import com.zxsoft.fanfanfamily.mort.service.BankLoanPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Service
public class BankLoanPolicyServiceImp extends BaseServiceImpl<BankLoanPolicy> implements BankLoanPolicyService {
    private final String resPathName = "bankLoanPolicy";
    @Autowired
    private BankLoanPolicyDao bankLoanPolicyDao;


    @Override
    public JpaRepository<BankLoanPolicy, String> getBaseDao() {
        return bankLoanPolicyDao;
    }

    @Override
    public String uploadAvatarExtend(BankLoanPolicy organization, String fileName, String postfix, byte[] bytes) {
        return null;
    }

    @Override
    public Path loadAvatar(BankLoanPolicy organization, AvatorLoadFactor factor) {
        return null;
    }

    @Override
    public String uploadAvatarExtend(BankLoanPolicy organization, MultipartFile file) {
        return null;
    }
}
