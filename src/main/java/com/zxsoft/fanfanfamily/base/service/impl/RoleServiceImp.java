package com.zxsoft.fanfanfamily.base.service.impl;

import com.zxsoft.fanfanfamily.base.domain.Role;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.service.RoleService;
import com.zxsoft.fanfanfamily.base.repository.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImp extends BaseServiceImpl<Role> implements RoleService {
    private final String resPathName = "role";
    @Autowired
    private RoleDao roleDao;


    @Override
    public JpaRepository<Role, String> getBaseDao() {
        return roleDao;
    }

    @Override
    public String uploadAvatarExtend(Role role, String fileName, String postfix, byte[] bytes) {
        return null;
    }

    @Override
    public Path loadAvatar(Role role, AvatorLoadFactor factor) {
        return null;
    }

    @Override
    public String uploadAvatarExtend(Role role, MultipartFile file) {
        return null;
    }

    @Override
    public List<Role> findAll() {
        return roleDao.queryAllByIdNotNull();
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleDao.queryRolesByIdNotNull(pageable);
    }

    @Override
    public Optional<Role> getByKey(String key) {
        return roleDao.findFirstByRoleNameContaining(key);
    }
}
