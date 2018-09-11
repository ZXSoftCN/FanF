package com.zxsoft.fanfanfamily.base.service.impl;

import com.zxsoft.fanfanfamily.base.domain.Organization;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.repository.OrganizationDao;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class OrganizationServiceImp extends BaseServiceImpl<Organization> implements OrganizationService {
    private final String resPathName = "organization";
    @Autowired
    private OrganizationDao organizationDao;

    @Override
    public JpaRepository<Organization, String> getBaseDao() {
        return organizationDao;
    }

    @Override
    public String uploadAvatarExtend(Organization organization, String fileName, String postfix, byte[] bytes) {
        return null;
    }

    @Override
    public Path loadAvatar(Organization organization, AvatorLoadFactor factor) {
        return null;
    }

    @Override
    public String uploadAvatarExtend(Organization organization, MultipartFile file) {
        return null;
    }
}
