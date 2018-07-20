package com.zxsoft.fanfanfamily.mort.service;


import com.zxsoft.fanfanfamily.base.domain.Permission;
import com.zxsoft.fanfanfamily.base.domain.Role;
import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserInfoService {

    UserInfo addUserInfo(String userName, String name, String password) throws EmptyResultDataAccessException;

    List<Permission> findPermissionByUserInfo(UserInfo userInfo);
    Page<Permission> findPermissionByUserInfo(UserInfo userInfo, Pageable pageAble);

    Page<Permission> findPermissionByAccountNameLike(String accountName, Pageable pageAble);
    Page<Role> findAllRoleByAccountName(String accountName, Pageable pageAble);
    Page<Role> findAllRoleByUserInfo(UserInfo userInfo, Pageable pageAble);

    Page<Role> findAvailableRoleByUserInfo(UserInfo userInfo, Pageable pageAble);

    Optional<UserInfo> findByUserName(String userName);

    List<UserInfo> getUserInfoList(Map<String, Integer> pageParam);

}
