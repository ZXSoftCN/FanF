package com.zxsoft.fanfanfamily.base.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "sys_UserInfo")
public class UserInfo extends BaseEntity {

    private static final long serialVersionUID = -5804977308683352078L;

//    private String id;
//
//    @Id
//    @GeneratedValue(generator = "uuid2" )   //指定生成器名称
//    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator" )  //生成器名称，uuid生成类
//    @Column(name = "id",nullable = false,length = 36)
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String uid) {
//        this.id = uid;
//    }

    private String userName;//帐号

    private String name;//名称（昵称或者真实姓名，不同系统不同定义）

    private String password; //密码;

    private String salt;//加密密码的盐

    private byte state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.

    private String iconUrl;

    private Set<Role> roleList;// 一个用户具有多个角色


    @Column(name = "userName",unique =true)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "name",nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore//fastjson Ignore
    @JSONField(serialize = false)
    @Column(name = "password",columnDefinition="varchar(255) default '123456'")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "salt",nullable = false)
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Column(name = "state",columnDefinition="TINYINT default 0")
    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    @ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "sys_UserRole", joinColumns = { @JoinColumn(name = "userInfoId",referencedColumnName = "id") },
            inverseJoinColumns ={@JoinColumn(name = "roleId",referencedColumnName = "id") })
    public Set<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(Set<Role> roleList) {
        this.roleList = roleList;
    }

    /**
     * 密码盐.
     * @return
     */
    @Transient
    @JsonIgnore
    @JSONField(serialize = false)
    public String getCredentialsSalt(){
        return this.userName +this.salt;
    }
    //重新对盐重新进行了定义，用户名+salt，这样就更加不容易被破解


    @Column(name = "icnoUrl")
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}