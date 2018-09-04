package com.zxsoft.fanfanfamily.base.service;


import com.zxsoft.fanfanfamily.base.domain.Permission;
import com.zxsoft.fanfanfamily.base.domain.Role;
import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import com.zxsoft.fanfanfamily.base.domain.vo.UserPermissionDTO;
import com.zxsoft.fanfanfamily.base.domain.vo.UserPermissionInner;
import com.zxsoft.fanfanfamily.base.domain.vo.UserPermissionNativeDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserInfoService  extends BaseService<UserInfo> {

    UserInfo addUserInfo(String userName, String name, String password) throws EmptyResultDataAccessException;

    List<Permission> findPermissionByUserInfo(UserInfo userInfo);
    Page<Permission> findPermissionByUserInfo(UserInfo userInfo, Pageable pageAble);

    Page<Permission> findPermissionByAccountNameLike(String accountName, Pageable pageAble);
    Page<Role> findAllRoleByAccountName(String accountName, Pageable pageAble);
    Page<Role> findAllRoleByUserInfo(UserInfo userInfo, Pageable pageAble);

    Page<Role> findAvailableRoleByUserInfo(UserInfo userInfo, Pageable pageAble);

    Optional<UserInfo> findByUsername(String userName);

    List<UserInfo> getUserInfoList(Map<String, Integer> pageParam);

    List<UserPermissionNativeDTO> findUserPermission(String userName);
    Optional<UserPermissionDTO> findUserInfoPermission(String userName);
}
