package com.zxsoft.fanfanfamily.base.service.impl;

import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.domain.vo.UserPermissionDTO;
import com.zxsoft.fanfanfamily.base.domain.vo.UserPermissionInner;
import com.zxsoft.fanfanfamily.base.domain.vo.UserPermissionNativeDTO;
import com.zxsoft.fanfanfamily.common.JPAUtil;
import com.zxsoft.fanfanfamily.common.RandomGeneratorUtil;
import com.zxsoft.fanfanfamily.base.domain.Permission;
import com.zxsoft.fanfanfamily.base.domain.Role;
import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import com.zxsoft.fanfanfamily.base.repository.UserInfoDao;
import com.zxsoft.fanfanfamily.base.service.UserInfoService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
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
import java.util.ArrayList;
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

//    @Override
//    public List<UserInfo> findAll() {
//        return userInfoDao.findAll();
//    }
//
//    @Override
//    public Page<UserInfo> findAll(Pageable pageable) {
//        return userInfoDao.findAll(pageable);
//    }

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

    @Override
    public List<UserPermissionNativeDTO> findUserPermission(String userName) {
        Session session = entityManagerUtil.getHibernateSession();
        try {
            List<UserPermissionNativeDTO> lstNative = session
                    .createNativeQuery("SELECT DISTINCT u.id,u.name,u.userName,u.state,p.name AS pName,p.resourceType,p.url FROM sys_userinfo u \n" +
                            "LEFT JOIN sys_userrole tu ON u.id = tu.userInfoId\n" +
                            "LEFT JOIN sys_role r ON r.id = tu.roleId\n" +
                            "LEFT JOIN sys_rolepermission tr ON tr.roleId = r.id\n" +
                            "LEFT JOIN sys_permission p ON tr.permissionId = p.id\n" +
                            "WHERE u.userName = :userName").setParameter("userName", userName)
                    .setResultTransformer(Transformers.aliasToBean(UserPermissionNativeDTO.class)).list();
            return lstNative;
        }
        finally {
            session.close();
        }

    }

    @Override
    public Optional<UserPermissionDTO> findUserInfoPermission(String userName) {
        Session session = entityManagerUtil.getHibernateSession();
        try {
//            List<Object> entities = session.createNativeQuery(
//                    "SELECT DISTINCT {u.*}, {pr.*} " +
//                            "FROM sys_userinfo u left join sys_userrole pt on u.id = pt.userInfoId " +
//                            " left join sys_role pr on pt.roleId = pr.id")
//                    .addEntity( "u", UserInfo.class)
//                    .addEntity( "pr", Role.class)
//                    .list();
            List<Object> lstUserPermission = session.createNativeQuery(
                    "SELECT DISTINCT {u.*},{p.*}\n" +
                            "from sys_userinfo u\n" +
                            "  left join sys_userrole tu on u.id = tu.userInfoId\n" +
                            "  left join sys_role r on r.id = tu.roleId\n" +
                            "  left join sys_rolepermission tr on tr.roleId = r.id\n" +
                            "left join sys_permission p on tr.permissionId = p.id\n" +
                            "WHERE u.userName = :userName" ).setParameter("userName", userName)
                    .addEntity( "u", UserInfo.class)
                    .addEntity( "p", Permission.class)
                    .list();

            UserPermissionInner innerDTO = null;
            int i = 0;
            List<Permission> lstPermission =new ArrayList<>();
            for (Object obj : lstUserPermission) {
                if (obj.getClass().isArray()) {
                    Object[] rlts = (Object[])obj;
                    UserInfo itemUser = (UserInfo)rlts[0];
                    Permission itemPermission = (Permission)rlts[1];
                    if (i++ < 1) {
                        innerDTO = new UserPermissionInner();
                        innerDTO.setId(itemUser.getId());
                        innerDTO.setName(itemUser.getName());
                        innerDTO.setUserName(itemUser.getUserName());
                        innerDTO.setState(itemUser.getState());
                    }
                    if (itemPermission != null) {
                        lstPermission.add(itemPermission);
                    }
                }
            }
            if (innerDTO != null) {
                innerDTO.setPermissions(lstPermission);
            }

            UserPermissionDTO dto = new UserPermissionDTO();
            dto.setInner(innerDTO);
            return Optional.ofNullable(dto);
//            return Optional.ofNullable(innerDTO);
        }
        finally {
            session.close();
        }
    }
}