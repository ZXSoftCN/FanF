package com.zxsoft.fanfanfamily.base.domain.mort;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.common.SpringUtil;
import com.zxsoft.fanfanfamily.mort.repository.BankDao;
import com.zxsoft.fanfanfamily.mort.repository.LimitStatusDao;
import com.zxsoft.fanfanfamily.mort.repository.PolicyTypeDao;
import com.zxsoft.fanfanfamily.mort.repository.RegionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/*
*
 */
@Entity
@Table(name = "mort_BankLoanPolicy")
@NamedEntityGraph(name = "BankLoanPolicy.lazy",
        attributeNodes = {
                @NamedAttributeNode("loanPolicyNeedConditions"),@NamedAttributeNode("loanPolicyNeedAttaches")})
public class BankLoanPolicy extends BaseEntity {

    private static final long serialVersionUID = 4482466420523292457L;

    private String code;
    private Short approvedDay;//审批天数
    private Short loanDay;//放款天数
    private String description;//简述
    private Double adjustRange;//上浮下调幅度
    private Date effectiveDate;//政策调整生效日期

    @Autowired
    private SpringUtil springUtil;
    //区域
    private String regionId;
    private Optional<Region> region;
    //银行
    private String bankId;
    private Optional<Bank> bank;
    //政策类型
    private String policyTypeId;
    private Optional<PolicyType> policyType;
    //额度状态：充足、紧张
    private String limitStatusId;
    private Optional<LimitStatus> limitStatus;

    private Set<LoanPolicyNeedCondition> loanPolicyNeedConditions = new HashSet<>();//准入条件
    private Set<LoanPolicyNeedAttach> loanPolicyNeedAttaches = new HashSet<>();//所需资料

    @Column(name = "code",unique = true,nullable = false,columnDefinition = "varchar(36)")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "approveDay",nullable = false,columnDefinition = "smallint")
    public Short getApprovedDay() {
        return approvedDay;
    }

    public void setApprovedDay(Short approvedDay) {
        this.approvedDay = approvedDay;
    }

    @Column(name = "loanDay",nullable = false,columnDefinition = "smallint")
    public Short getLoanDay() {
        return loanDay;
    }

    public void setLoanDay(Short loanDay) {
        this.loanDay = loanDay;
    }

    @Column(name = "description",nullable = false,columnDefinition = "varchar(1000)")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "adjustRange", scale = 4,length = 10)
    public Double getAdjustRange() {
        return adjustRange;
    }

    public void setAdjustRange(Double adjustRange) {
        this.adjustRange = adjustRange;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd" )
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "effectiveDate")
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }


    @Column(name = "regionId",columnDefinition = "varchar(36)")
    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
    @Transient
    public Optional<Region> getRegion() {
        if (regionId != null && !regionId.isEmpty()) {
            RegionDao refDao = (RegionDao)springUtil.getBean("regionDao");
            return refDao.findById(regionId);
        } else {
            return region.empty();
        }
    }
    @Column(name = "bankId",columnDefinition = "varchar(36)")
    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    @Transient
    public Optional<Bank> getBank() {
        if (bankId != null && !bankId.isEmpty()) {
            BankDao refDao = (BankDao)springUtil.getBean("bankDao");
            return refDao.findById(bankId);
        } else {
            return bank.empty();
        }
    }

    @Column(name = "policyTypeId",columnDefinition = "varchar(36)")
    public String getPolicyTypeId() {
        return policyTypeId;
    }

    public void setPolicyTypeId(String policyTypeId) {
        this.policyTypeId = policyTypeId;
    }
    @Transient
    public Optional<PolicyType> getPolicyType() {
        if (policyTypeId != null && !policyTypeId.isEmpty()) {
            PolicyTypeDao refDao = (PolicyTypeDao)springUtil.getBean("policyTypeDao");
            return refDao.findById(policyTypeId);
        } else {
            return policyType.empty();
        }
    }

    @Column(name = "limitStatusId",columnDefinition = "varchar(36)")
    public String getLimitStatusId() {
        return limitStatusId;
    }

    public void setLimitStatusId(String limitStatusId) {
        this.limitStatusId = limitStatusId;
    }
    @Transient
    public Optional<LimitStatus> getLimitStatus() {
        if (limitStatusId != null && !limitStatusId.isEmpty()) {
            LimitStatusDao refDao = (LimitStatusDao)springUtil.getBean("limitStatusDao");
            return refDao.findById(limitStatusId);
        } else {
            return limitStatus.empty();
        }
    }

    @OneToMany(cascade = {CascadeType.REMOVE},mappedBy = "bankLoanPolicy",fetch = FetchType.LAZY)
    public Set<LoanPolicyNeedCondition> getLoanPolicyNeedConditions() {
        return loanPolicyNeedConditions;
    }

    public void setLoanPolicyNeedConditions(Set<LoanPolicyNeedCondition> loanPolicyNeedConditions) {
        this.loanPolicyNeedConditions = loanPolicyNeedConditions;
    }

    @OneToMany(cascade = {CascadeType.REMOVE},mappedBy = "bankLoanPolicy",fetch = FetchType.LAZY)
    public Set<LoanPolicyNeedAttach> getLoanPolicyNeedAttaches() {
        return loanPolicyNeedAttaches;
    }

    public void setLoanPolicyNeedAttaches(Set<LoanPolicyNeedAttach> loanPolicyNeedAttaches) {
        this.loanPolicyNeedAttaches = loanPolicyNeedAttaches;
    }
}
