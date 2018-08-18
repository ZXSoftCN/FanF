package com.zxsoft.fanfanfamily.base.repository;

import com.zxsoft.fanfanfamily.base.domain.Role;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleDao extends JpaRepository<Role,String> {

    Optional<Role> findFirstByRoleNameContaining(String nameLike);
    List<Role> findAllByRoleNameLike(String rolename);
    Page<Role> findAllByRoleNameLikeOrderById(String rolename, Pageable pageable);

    List<Role> findAllByIsEnableTrueOrderById();
    Page<Role> findAllByIsEnableTrueOrderById(Pageable pageable);

    //扩展
    @EntityGraph(attributePaths = { "permissions"})
    List<Role> queryAllByIdNotNull();
    @EntityGraph(attributePaths = { "permissions"})
    Page<Role> queryRolesByIdNotNull(Pageable pageable);

}
