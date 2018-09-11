package com.zxsoft.fanfanfamily.mort.service.impl;

import com.zxsoft.fanfanfamily.base.domain.mort.LoanPolicyNeedAttach;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.service.impl.BaseServiceImpl;
import com.zxsoft.fanfanfamily.mort.repository.LoanPolicyNeedAttachDao;
import com.zxsoft.fanfanfamily.mort.service.LoanPolicyNeedAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Service
public class LoanPolicyNeedAttachServiceImp extends BaseServiceImpl<LoanPolicyNeedAttach> implements LoanPolicyNeedAttachService {
    private final String resPathName = "loanPolicyNeedAttach";
    @Autowired
    private LoanPolicyNeedAttachDao loanPolicyNeedAttachDao;


    @Override
    public JpaRepository<LoanPolicyNeedAttach, String> getBaseDao() {
        return loanPolicyNeedAttachDao;
    }

    @Override
    public String uploadAvatarExtend(LoanPolicyNeedAttach organization, String fileName, String postfix, byte[] bytes) {
        return null;
    }

    @Override
    public Path loadAvatar(LoanPolicyNeedAttach organization, AvatorLoadFactor factor) {
        return null;
    }

    @Override
    public String uploadAvatarExtend(LoanPolicyNeedAttach organization, MultipartFile file) {
        return null;
    }
}
