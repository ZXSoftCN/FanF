package com.zxsoft.fanfanfamily.base.domain;

import com.zxsoft.fanfanfamily.base.domain.mort.Bank;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/*
*
 */
@Entity
@Table(name = "sys_region")
@NamedEntityGraph(name = "Region.lazy",
        attributeNodes = {@NamedAttributeNode("resources"),@NamedAttributeNode("banks")})
public class Region extends BaseEntity {
    private static final long serialVersionUID = -8301655546503914165L;

    private String name;
    private String code;
    private String description;
    //地区logo
    private String logoUrl;
    private Set<RegionResource> resources = new HashSet<>();
    private Set<Bank> banks = new HashSet<>();
    private Set<Organization> organizations = new HashSet<>();

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "code",nullable = false,unique = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "logoUrl")
    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "sys_region_bank",
            joinColumns = {@JoinColumn(name = "regionId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "bankId", referencedColumnName ="id",
                    foreignKey = @ForeignKey(name = "FK_BankToRegion"))})
    public Set<Bank> getBanks() {
        return banks;
    }

    public void setBanks(Set<Bank> banks) {
        this.banks = banks;
    }

    //    @JSONField(serialize=true)
    @OneToMany(cascade = {CascadeType.REMOVE},mappedBy = "region",fetch = FetchType.LAZY)
    public Set<RegionResource> getResources() {
        return resources;
    }

    public void setResources(Set<RegionResource> resources) {
        this.resources = resources;
    }

    @OneToMany(cascade = {CascadeType.REMOVE},mappedBy = "region",fetch = FetchType.LAZY)
    public Set<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }

}
