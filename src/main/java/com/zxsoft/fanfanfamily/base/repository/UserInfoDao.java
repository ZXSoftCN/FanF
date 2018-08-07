package com.zxsoft.fanfanfamily.base.repository;

import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoDao extends JpaRepository<UserInfo,String> {

    @Override
    Optional<UserInfo> findById(String id);

    Optional<UserInfo> findByUsername(String userName);


}
