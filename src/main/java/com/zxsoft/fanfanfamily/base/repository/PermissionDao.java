package com.zxsoft.fanfanfamily.base.repository;

import com.zxsoft.fanfanfamily.base.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionDao extends JpaRepository<Permission,String> {

}
