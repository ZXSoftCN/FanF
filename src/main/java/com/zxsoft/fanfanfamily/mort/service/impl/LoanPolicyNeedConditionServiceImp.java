package com.zxsoft.fanfanfamily.mort.service.impl;

import com.zxsoft.fanfanfamily.base.domain.mort.LoanPolicyNeedCondition;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.service.impl.BaseServiceImpl;
import com.zxsoft.fanfanfamily.mort.repository.LoanPolicyNeedConditionDao;
import com.zxsoft.fanfanfamily.mort.service.LoanPolicyNeedConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Service
public class LoanPolicyNeedConditionServiceImp extends BaseServiceImpl<LoanPolicyNeedCondition> implements LoanPolicyNeedConditionService {
    private final String resPathName = "loanPolicyNeedCondition";
    @Autowired
    private LoanPolicyNeedConditionDao loanPolicyNeedConditionDao;


    @Override
    public JpaRepository<LoanPolicyNeedCondition, String> getBaseDao() {
        return loanPolicyNeedConditionDao;
    }

    @Override
    public String uploadAvatarExtend(LoanPolicyNeedCondition organization, String fileName, String postfix, byte[] bytes) {
        return null;
    }

    @Override
    public Path loadAvatar(LoanPolicyNeedCondition organization, AvatorLoadFactor factor) {
        return null;
    }

    @Override
    public String uploadAvatarExtend(LoanPolicyNeedCondition organization, MultipartFile file) {
        return null;
    }
}
