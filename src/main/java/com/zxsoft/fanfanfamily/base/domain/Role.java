package com.zxsoft.fanfanfamily.base.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "sys_Role")
public class Role extends BaseEntity{

    private static final long serialVersionUID = -8037045542580771196L;

//    @Id
//    @GeneratedValue(generator = "uuid2" )   //指定生成器名称
//    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator" )  //生成器名称，uuid生成类
//    @Column(name = "id",nullable = false,length = 36)
//    private String id; // 编号
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    @Column(name = "roleName",unique =true)
    private String roleName; // 角色标识程序中判断使用,如"admin",这个是唯一的:
    @Column(name = "description",columnDefinition="varchar(500) default ''")
    private String description; // 角色描述,UI界面显示使用
    @Column(name = "isEnable",columnDefinition="bit default 1")
    private Boolean isEnable = Boolean.TRUE; // 是否可用,如果不可用将不会添加给用户

    //角色 -- 权限关系：多对多关系;
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="sys_RolePermission",joinColumns={@JoinColumn(name="roleId",referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="permissionId",referencedColumnName = "id")})
    private List<Permission> permissions;

    // 用户 - 角色关系定义;
    @ManyToMany
    @JoinTable(name="sys_UserRole",joinColumns={@JoinColumn(name="roleId",referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="userInfoId",referencedColumnName = "id")})
    private List<UserInfo> userInfos;// 一个角色对应多个用户


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }
}