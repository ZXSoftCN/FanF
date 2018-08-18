package com.zxsoft.fanfanfamily.base.repository;

import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInfoDao extends JpaRepository<UserInfo,String> {

    @Override
    Optional<UserInfo> findById(String id);

    Optional<UserInfo> findByUserName(String userName);
    @EntityGraph(attributePaths = { "roleList"})
    Optional<UserInfo> findFirstByUserNameContaining(String userNameLike);

    //扩展方法
    @EntityGraph(attributePaths = { "roleList"})
    List<UserInfo> queryAllByIdNotNull();
    @EntityGraph(attributePaths = { "roleList"})
    Page<UserInfo> queryUserInfosByIdNotNull(Pageable pageable);

}
