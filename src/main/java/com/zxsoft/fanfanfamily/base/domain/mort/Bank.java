package com.zxsoft.fanfanfamily.base.domain.mort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.Region;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sys_bank")
public class Bank extends BaseEntity {

    private static final long serialVersionUID = -2727918037387072944L;


    private String code;
    private String name;
    private Bank parentBank;
    //    private Set<Bank> subBanks = new HashSet<Bank>();
    private String iconUrl;
    private String fullName;
    //审批天数
    private int approvedDay;
    //放款天数
    private int loanDay;
    //最近一次数据更新日期：使用BaseEntity上的lastUpdateTime;

    //所属城市或地区
    private Set<Region> regions = new HashSet<>();

    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH})
    @JoinColumn(name = "parentBankId",columnDefinition = "varchar(36) DEFAULT ''")
    public Bank getParentBank() {
        return parentBank;
    }

    public void setParentBank(Bank parentBank) {
        this.parentBank = parentBank;
    }

    @Column(name = "icnoUrl")
    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    @Column(name = "fullName")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "apporovedDay")
    public int getApprovedDay() {
        return approvedDay;
    }

    public void setApprovedDay(int approvedDay) {
        this.approvedDay = approvedDay;
    }

    @Column(name = "loanDay")
    public int getLoanDay() {
        return loanDay;
    }

    public void setLoanDay(int loanDay) {
        this.loanDay = loanDay;
    }

    //cascade = CascadeType.DETACH,
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.LAZY,mappedBy = "banks")
    public Set<Region> getRegions() {
        return regions;
    }

    public void setRegions(Set<Region> regions) {
        this.regions = regions;
    }

    //辅助方法：用于添加与地区的多对多关系。在Bank实例新增时添加Region保存，会因为cascade是Merge
    //造成系统检查认为bank保存前还是detach状态，而无法更新到Region的banks属性，提示异常。
    @Deprecated
    private void addRegions(Set<Region> regions) {
        this.regions.addAll(regions);
    }
}
