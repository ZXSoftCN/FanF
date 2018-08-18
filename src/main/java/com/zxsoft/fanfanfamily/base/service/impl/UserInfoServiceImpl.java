package com.zxsoft.fanfanfamily.base.service.impl;

import com.zxsoft.fanfanfamily.base.domain.mort.Employee;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.common.JPAUtil;
import com.zxsoft.fanfanfamily.common.RandomGeneratorUtil;
import com.zxsoft.fanfanfamily.base.domain.Permission;
import com.zxsoft.fanfanfamily.base.domain.Role;
import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import com.zxsoft.fanfanfamily.base.repository.UserInfoDao;
import com.zxsoft.fanfanfamily.base.service.UserInfoService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("userInfoService")
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfo>  implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public JpaRepository<UserInfo, String> getBaseDao() {
        return userInfoDao;
    }


    //<editor-fold desc="私有方法">
    private void modifyIcon(UserInfo userInfo, Path path) {
        try {
            String strOld = userInfo.getIconUrl();
            if (strOld != null && !strOld.isEmpty()) {
                if (userInfo.getIconUrl().startsWith("file:/")) {
                    strOld = userInfo.getIconUrl().replaceFirst("file:/", "");
                }
                Path pathOld = Paths.get(strOld);
                Files.deleteIfExists(pathOld);
            }
            userInfo.setIconUrl(path.toString());
            userInfoDao.save(userInfo);
        }catch (IOException ex){
            logger.error(String.format("%s Failed to store file:%s.%s",
                    this.getClass().getName(),ex.getMessage(), System.lineSeparator()));//System.lineSeparator()换行符
        }
    }
    
    @Override
    public Path uploadAvatarExtend(UserInfo userInfo, String fileName, String postfix, byte[] bytes) {
        return null;
    }


    @Override
    public Path loadAvatar(UserInfo userInfo, AvatorLoadFactor factor) {
        String strUrl = userInfo.getIconUrl();
        return loadAvatarInner(strUrl,factor);
    }

    @Override
    public Path uploadAvatarExtend(UserInfo userInfo, MultipartFile file) {
        Path itemNew = uploadAvatar(file);
        if (itemNew == null) {
            return null;
        }
        //将Path路径保存至Region的IconUrl属性
        modifyIcon(userInfo,itemNew);

        return itemNew;
    }

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
    public Optional<UserInfo> findByUsername(String userName) {
        return userInfoDao.findByUserName(userName);
    }

    @Override
    public List<UserInfo> findAll() {
        return userInfoDao.queryAllByIdNotNull();
    }

    @Override
    public Page<UserInfo> findAll(Pageable pageable) {
        return userInfoDao.queryUserInfosByIdNotNull(pageable);
    }

    @Override
    public Optional<UserInfo> getByKey(String key) {
        return userInfoDao.findFirstByUserNameContaining(key);
    }

    @Override
    public UserInfo add(UserInfo userInfo) {
        if (userInfo.getUserName() == null || userInfo.getUserName().isEmpty()) {
            userInfo.setUserName(userInfo.getName());
        }
        return addUserInfo(userInfo.getUserName(),userInfo.getName(),userInfo.getPassword());
    }

    @Override
    public UserInfo addUserInfo(String userName, String name, String password) throws EmptyResultDataAccessException {
        if (userName == null || userName.isEmpty()) {
            throw new EmptyResultDataAccessException("账号不允许为空！",10);
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
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