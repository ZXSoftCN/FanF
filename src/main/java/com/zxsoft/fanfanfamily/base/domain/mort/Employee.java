package com.zxsoft.fanfanfamily.base.domain.mort;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.Organization;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "organizationId", foreignKey = @ForeignKey(name = "none", value =ConstraintMode.NO_CONSTRAINT),
            columnDefinition = "varchar(36) DEFAULT ''")
    public Organization getOrganization() {
        return organization;
    }

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
}
