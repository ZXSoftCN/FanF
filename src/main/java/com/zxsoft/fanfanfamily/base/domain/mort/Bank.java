package com.zxsoft.fanfanfamily.base.domain.mort;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.repository.EntityIncreaseDao;
import com.zxsoft.fanfanfamily.base.service.EntityIncreaseService;
import com.zxsoft.fanfanfamily.common.SpringUtil;
import com.zxsoft.fanfanfamily.mort.repository.BankDao;
import com.zxsoft.fanfanfamily.mort.repository.RegionDao;
import com.zxsoft.fanfanfamily.mort.service.BankService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "sys_bank")
@NamedEntityGraph(name = "Bank.lazy",
        attributeNodes = { @NamedAttributeNode("parentBank"),@NamedAttributeNode("regions") })
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

    private String note;

    //所属城市或地区
    private Set<Region> regions = new HashSet<>();

    @Column(name = "code", unique = true, nullable = false)
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

    @JSONField(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH})
    @JoinColumn(name = "parentBankId",columnDefinition = "varchar(36) DEFAULT ''")
    public Bank getParentBank() {
        return parentBank;
    }

    public void setParentBank(Bank parentBank) {
        this.parentBank = parentBank;
    }

    @Column(name = "iconUrl")
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
    @JSONField(serialize = false)
    @ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.LAZY,mappedBy = "banks")
    public Set<Region> getRegions() {
        return regions;
    }

    public void setRegions(Set<Region> regions) {
        this.regions = regions;
    }

    @Column(name = "note",columnDefinition = "varchar(1000)")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    //region Transient
    @Transient
    public String getParenBankId() {
        if (parentBank != null) {
            return parentBank.getId();
        }
        return "";
    }
    @Transient
    public void setParentBankId(String parentBankId) {
        if (!StringUtils.isEmpty(parentBankId)) {
            BankDao bankDao = (BankDao) SpringUtil.getBean("bankDao");

            Optional<Bank> bankOp = bankDao.findById(parentBankId);
            if (bankOp.isPresent()) {
                this.parentBank = bankOp.get();
            }
        }
    }

    @Transient
    public Set<String> getRegionIds() {
        Set<String> regionIds = new HashSet<>();
        if (regions.size() > 0) {
            for (Region item : regions) {
                regionIds.add(item.getId());
            }
        }
        return regionIds;
    }

    @Transient
    public void setRegionIds(Set<String> ids) {
        if (ids.size() > 0) {
            RegionDao regionDao = (RegionDao) SpringUtil.getBean("regionDao");
            for (String id : ids) {
                Optional<Region> regionOp = regionDao.findById(id);
                if (regionOp.isPresent()) {
                    regions.add(regionOp.get());
                }
            }
        }

    }
    //endregion

    //辅助方法：用于添加与地区的多对多关系。在Bank实例新增时添加Region保存，会因为cascade是Merge
    //造成系统检查认为bank保存前还是detach状态，而无法更新到Region的banks属性，提示异常。
    @Deprecated
    private void addRegions(Set<Region> regions) {
        this.regions.addAll(regions);
    }


    //region Override方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Objects.equals(getId(), bank.getId());
    }

    @Override
    public void onSetDefault() {
        super.onSetDefault();
        BankService bankService = (BankService)SpringUtil.getBean("bankServiceImpl");
        this.setCode(bankService.getNewCode());
    }

    @Override
    public void onPostPersist() {
        EntityIncreaseService entityIncreaseService = (EntityIncreaseService)SpringUtil.getBean("entityIncreaseServiceImpl");
        entityIncreaseService.updateCodeMaxNum("bank",this.code);
        super.onPostPersist();
    }

    @Override
    public String toString() {
        if (this.getId() != null) {
            return this.getId();
        }
        return super.toString();
    }
    //endregion
}
