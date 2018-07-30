package com.zxsoft.fanfanfamily.base.domain.mort;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "mort_LoanPolicyNeedAttach")
public class LoanPolicyNeedAttach extends BaseEntity {

    private static final long serialVersionUID = 3743716223654529590L;

    private Short seqNo;//序号
    private String description;//资料描述
    private BankLoanPolicy bankLoanPolicy;//按揭政策

    @Column(name = "seqNo",unique = true,nullable = false,columnDefinition = "smallint")
    public Short getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Short seqNo) {
        this.seqNo = seqNo;
    }

    @Column(name = "description",nullable = false,columnDefinition = "varchar(1000)")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name = "LoanPolicyId",columnDefinition = "varchar(36) DEFAULT ''")
    public BankLoanPolicy getBankLoanPolicy() {
        return bankLoanPolicy;
    }

    public void setBankLoanPolicy(BankLoanPolicy bankLoanPolicy) {
        this.bankLoanPolicy = bankLoanPolicy;
    }
}
