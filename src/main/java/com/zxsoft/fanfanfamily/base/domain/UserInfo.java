package com.zxsoft.fanfanfamily.base.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "sys_UserInfo")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -5804977308683352078L;
    @Id
    @GeneratedValue(generator = "uuid2" )   //指定生成器名称
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator" )  //生成器名称，uuid生成类
    @Column(name = "id",nullable = false,length = 36)
    private String id;
    @Column(name = "userName",unique =true)
    private String username;//帐号
    @Column(name = "name",nullable = false)
    private String name;//名称（昵称或者真实姓名，不同系统不同定义）
    @JsonIgnore//fastjson Ignore
    @Column(name = "password",columnDefinition="varchar(255) default '123456'")
    private String password; //密码;
    @Column(name = "salt",nullable = false)
    private String salt;//加密密码的盐
    @Column(name = "state",columnDefinition="TINYINT default 0")
    private byte state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.
    @ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "sys_UserRole", joinColumns = { @JoinColumn(name = "userInfoId",referencedColumnName = "id") },
            inverseJoinColumns ={@JoinColumn(name = "roleId",referencedColumnName = "id") })
    private List<Role> roleList;// 一个用户具有多个角色

    public String getId() {
        return id;
    }

    public void setId(String uid) {
        this.id = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    /**
     * 密码盐.
     * @return
     */
    public String getCredentialsSalt(){
        return this.username+this.salt;
    }
    //重新对盐重新进行了定义，用户名+salt，这样就更加不容易被破解
}