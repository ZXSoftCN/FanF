package com.zxsoft.fanfanfamily.mort.repository;

import com.zxsoft.fanfanfamily.base.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleDao extends JpaRepository<Role,String> {

    List<Role> findAllByRoleNameLike(String rolename);
    Page<Role> findAllByRoleNameLikeOrderById(String rolename, Pageable pageable);

    List<Role> findAllByIsEnableTrueOrderById();
    Page<Role> findAllByIsEnableTrueOrderById(Pageable pageable);

}
