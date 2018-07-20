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


    private String bankCode;
    private String bankName;
    private Bank parentBank;
    //    private Set<Bank> subBanks = new HashSet<Bank>();
    private String iconUrl;
    private String fullName;
    //审批天数
    private int approvedDay;
    //放款天数
    private int loanDay;
    //额度状态
    private String limitStatus;
    //最近一次数据更新日期：使用BaseEntity上的lastUpdateTime;

    //所属城市或地区
    private Region region;

    @Column(name = "bankCode")
    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Column(name = "bankName")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH})
    @JoinColumn(name = "parentBankId",columnDefinition = "varchar(36) DEFAULT ''")
    public Bank getParentBank() {
        return parentBank;
    }

    public void setParentBank(Bank parentBank) {
        this.parentBank = parentBank;
    }

//    @JsonIgnore
//    @OneToMany(mappedBy ="id",fetch = FetchType.LAZY,cascade = {CascadeType.DETACH},targetEntity = Bank.class)
//    public Set<Bank> getSubBanks() {
//        return subBanks;
//    }
//
//    public void setSubBanks(Set<Bank> subBanks) {
//        this.subBanks = subBanks;
//    }


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

    @Column(name = "limitStatus")
    public String getLimitStatus() {
        return limitStatus;
    }

    public void setLimitStatus(String limitStatus) {
        this.limitStatus = limitStatus;
    }

    @ManyToOne
    @JoinColumn(name = "regionId", foreignKey = @ForeignKey(name = "none", value =ConstraintMode.NO_CONSTRAINT),
            columnDefinition = "varchar(36) DEFAULT ''")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
