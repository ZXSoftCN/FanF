package com.zxsoft.fanfanfamily.base.domain.mort;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.Organization;

import javax.persistence.*;
import java.io.IOException;
import java.lang.reflect.Type;

@Entity
@Table(name = "sys_Employee")
public class Employee extends BaseEntity {
    private static final long serialVersionUID = -8404468001727359111L;

    private String code;//编码
    private String name;
    private String aliasName;
    private Organization organization;
    private String introduction;
    private String iconUrl;


    private String outterOrganizationId;

    @Column(name = "code",unique = true,nullable = false,columnDefinition = "varchar(36)")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "name",nullable = false,columnDefinition = "varchar(100)")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "aliasName",nullable = false,columnDefinition = "varchar(100)")
    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @JSONField(serialize = false,deserialize = false)
    @ManyToOne
    @JoinColumn(name = "organizationId", foreignKey = @ForeignKey(name = "none", value =ConstraintMode.NO_CONSTRAINT),
            columnDefinition = "varchar(36) DEFAULT ''")
    public Organization getOrganization() {
        return organization;
    }

    @JSONField(serialize = false,deserialize = false)
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Column(name = "introduction",columnDefinition = "varchar(5000)")
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Column(name = "icnoUrl")
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /*
    只用于对外增加一个序列化为OrganizationId的属性。
     */
    @Transient
    @JSONField(deserialize = false)
    public String getOrganizationId() {

        return getOrganization() == null ? "" : getOrganization().getId();
    }

    /*
     用于外部直接传入organizationId后，系统根据id获取Organization实体对setOrganization属性进行赋值。
     不用于内部方法使用。内部使用getOrganization直接访问实体。
     */
    @Transient
    @JSONField(serialize = false)
    public String getOutterOrganizationId() {
        return outterOrganizationId;
    }

    @Transient
    @JSONField(serialize = false,alternateNames = {"outterOrganizationId","organizationId"})
    public void setOutterOrganizationId(String outterOrganizationId) {
        this.outterOrganizationId = outterOrganizationId;
    }
}
