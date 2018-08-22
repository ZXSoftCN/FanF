package com.zxsoft.fanfanfamily.mort.service.impl;

import com.zxsoft.fanfanfamily.base.domain.mort.PolicyType;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.service.impl.BaseServiceImpl;
import com.zxsoft.fanfanfamily.mort.repository.PolicyTypeDao;
import com.zxsoft.fanfanfamily.mort.service.PolicyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Service
public class PolicyTypeServiceImp extends BaseServiceImpl<PolicyType> implements PolicyTypeService {
    private final String resPathName = "policyType";
    @Autowired
    private PolicyTypeDao policyTypeDao;

    @Override
    public List<PolicyType> findAll() {
        return policyTypeDao.findAllByIdIsNotNullOrderBySortNo();
    }

    @Override
    public JpaRepository<PolicyType, String> getBaseDao() {
        return policyTypeDao;
    }

    @Override
    public Path uploadAvatarExtend(PolicyType policyType, String fileName, String postfix, byte[] bytes) {
        return null;
    }

    @Override
    public Path loadAvatar(PolicyType policyType, AvatorLoadFactor factor) {
        return null;
    }

    @Override
    public Path uploadAvatarExtend(PolicyType policyType, MultipartFile file) {
        return null;
    }
}
