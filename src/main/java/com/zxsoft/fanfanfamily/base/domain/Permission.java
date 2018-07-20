package com.zxsoft.fanfanfamily.base.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "sys_Permission")
public class Permission implements Serializable {
    private static final long serialVersionUID = -1603670512627094071L;
    @Id
    @GeneratedValue(generator = "uuid2" )   //指定生成器名称
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator" )  //生成器名称，uuid生成类
    @Column(name = "id",nullable = false,length = 36)
    private String id;//主键.
    @Column(name = "name",unique =true)
    private String name;//名称.
    @Column(name = "resourceType",columnDefinition="varchar(255) default ''")
    private String resourceType;//资源类型，[menu|button]
    @Column(name = "url",columnDefinition="varchar(255) default ''")
    private String url;//资源路径.
    @Column(name = "permission",columnDefinition="varchar(255) default ''")
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    @Column(name = "parentId",columnDefinition="varchar(255) default ''")
    private String parentId; //父编号
    @Column(name = "parentIds",columnDefinition="varchar(500) default ''")
    private String parentIds; //父编号列表
    @Column(name = "isEnable",columnDefinition="bit default 1")
    private Boolean isEnable = Boolean.FALSE;
    @ManyToMany
    @JoinTable(name="sys_RolePermission",joinColumns={@JoinColumn(name="permissionId",referencedColumnName = "id")},
            inverseJoinColumns={@JoinColumn(name="roleId",referencedColumnName = "id")})
    private List<Role> roles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public Boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}