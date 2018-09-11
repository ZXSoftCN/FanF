package com.zxsoft.fanfanfamily.base.service.impl;

import com.zxsoft.fanfanfamily.base.domain.Permission;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.repository.PermissionDao;
import com.zxsoft.fanfanfamily.base.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Service
public class PermissionServiceImp extends BaseServiceImpl<Permission> implements PermissionService {
    private final String resPathName = "permission";
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public JpaRepository<Permission, String> getBaseDao() {
        return permissionDao;
    }

    @Override
    public String uploadAvatarExtend(Permission permission, String fileName, String postfix, byte[] bytes) {
        return null;
    }

    @Override
    public Path loadAvatar(Permission permission, AvatorLoadFactor factor) {
        return null;
    }

    @Override
    public String uploadAvatarExtend(Permission permission, MultipartFile file) {
        return null;
    }
}
