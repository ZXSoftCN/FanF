package com.zxsoft.fanfanfamily.mort.service.impl;

import com.zxsoft.fanfanfamily.common.JPAUtil;
import com.zxsoft.fanfanfamily.common.RandomGeneratorUtil;
import com.zxsoft.fanfanfamily.base.domain.Permission;
import com.zxsoft.fanfanfamily.base.domain.Role;
import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import com.zxsoft.fanfanfamily.mort.service.UserInfoService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public List<Permission> findPermissionByUserInfo(UserInfo userInfo) {
        return null;
    }

    @Override
    public Page<Permission> findPermissionByUserInfo(UserInfo userInfo, Pageable pageAble) {
        return null;
    }

    @Override
    public Page<Permission> findPermissionByAccountNameLike(String accountName, Pageable pageAble) {
        return null;
    }

    @Override
    public Page<Role> findAllRoleByAccountName(String accountName, Pageable pageAble) {
        return null;
    }

    @Override
    public Page<Role> findAllRoleByUserInfo(UserInfo userInfo, Pageable pageAble) {
        return null;
    }

    @Override
    public Page<Role> findAvailableRoleByUserInfo(UserInfo userInfo, Pageable pageAble) {
        return null;
    }

    @Override
    public Optional<UserInfo> findByUserName(String userName) {
        return userInfoDao.findByUsername(userName);
    }

    @Override
    public UserInfo addUserInfo(String userName, String name, String password) throws EmptyResultDataAccessException {
        if (userName == null || userName.isEmpty()) {
            throw new EmptyResultDataAccessException("账号不允许为空！",10);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userName);
        userInfo.setName(name);
        ByteSource byteSalt = RandomGeneratorUtil.getSecRng().nextBytes();
        String hashedPassword = new Sha256Hash(password,byteSalt.toBase64(),1024).toBase64();
        String salt = byteSalt.toBase64();
        userInfo.setPassword(hashedPassword);
        userInfo.setSalt(salt);

        userInfoDao.saveAndFlush(userInfo);



//
////创建一个测试密钥：
//        byte[] testKey = cipherService.generateNewKey().getEncoded();
////加密文件的字节：
//        byte[] fileBytes = null;
//        cipherService.encrypt(fileBytes, fileBytes);
//        byte[] encrypted = cipherService.encrypt(fileBytes, testKey).getBytes();

        return userInfo;
    }

    @Override
    public List<UserInfo> getUserInfoList(Map<String, Integer> pageParam) {
        Pageable pageItem = JPAUtil.getPageAble(pageParam);
        Page<UserInfo> plstUerInfo = userInfoDao.findAll(pageItem);
        return plstUerInfo.getContent();
    }
}