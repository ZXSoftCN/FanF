package com.zxsoft.fanfanfamily.base.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import com.zxsoft.fanfanfamily.base.service.EntityIncreaseService;
import com.zxsoft.fanfanfamily.mort.service.RegionService;
import com.zxsoft.fanfanfamily.common.SpringUtil;
import com.zxsoft.fanfanfamily.mort.repository.RegionDao;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/*
*
 */
@Entity
@Table(name = "sys_region")
@NamedEntityGraph(name = "Region.lazy",
        attributeNodes = {@NamedAttributeNode("resources"),@NamedAttributeNode("banks"),
                @NamedAttributeNode("organizations"),@NamedAttributeNode("parentRegion")})
public class Region extends BaseEntity {
    private static final long serialVersionUID = -8301655546503914165L;

    private String name;
    private String code;
    private String description;
    //地区logo
    private String logoUrl;
    private Region parentRegion;
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

    @JSONField(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH})
    @JoinColumn(name = "parentRegionId",columnDefinition = "varchar(36) DEFAULT ''",
            foreignKey = @ForeignKey(name = "none",value = ConstraintMode.NO_CONSTRAINT) )
    public Region getParentRegion() {
        return parentRegion;
    }

    public void setParentRegion(Region parentRegion) {
        this.parentRegion = parentRegion;
    }

    @Transient
    public String getParentRegionId() {
        if (parentRegion != null) {
            return parentRegion.getId();
        }
        return "";
    }
    @Transient
    public void setParentRegionId(String parentRegionId) {
        if (!StringUtils.isEmpty(parentRegionId)) {
            RegionDao regionDao = (RegionDao) SpringUtil.getBean("regionDao");

            Optional<Region> regionOp = regionDao.findById(parentRegionId);
            if (regionOp.isPresent()) {
                this.parentRegion = regionOp.get();
            }
        }
    }

    @Column(name = "logoUrl")
    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @JSONField(serialize = false)
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
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

//    @JSONField(serialize=false)
    @OneToMany(cascade = {CascadeType.REMOVE},mappedBy = "region",fetch = FetchType.LAZY)
    public Set<RegionResource> getResources() {
        return resources;
    }

    public void setResources(Set<RegionResource> resources) {
        this.resources = resources;
    }

//    @JSONField(serialize=false)
    @OneToMany(cascade = {CascadeType.REMOVE},mappedBy = "region",fetch = FetchType.LAZY)
    public Set<Organization> getOrganizations() {
        return organizations;
    }


    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return Objects.equals(getId(), region.getId());
    }

    @Override
    public void onSetDefault() {
        super.onSetDefault();
        RegionService regionService = (RegionService)SpringUtil.getBean("regionServiceImpl");
        this.setCode(regionService.getNewCode());
    }

    @Override
    public void onPostPersist() {
        EntityIncreaseService entityIncreaseService = (EntityIncreaseService)SpringUtil.getBean("entityIncreaseServiceImpl");
        entityIncreaseService.updateCodeMaxNum("region",this.code);
        super.onPostPersist();
    }

    @Override
    public String toString() {
        if (this.getId() != null) {
            return this.getId();
        }
        return super.toString();
    }

    public void addBank(Bank bank) {
        if (bank == null || banks.contains(bank)) {
            return;
        }
        banks.add(bank);
        bank.getRegions().add(this);
    }

    public void removeBank(Bank bank) {
        if (bank == null || !banks.contains(bank)) {
            return;
        }
        banks.remove(bank);
        bank.getRegions().remove(this);
    }
}
