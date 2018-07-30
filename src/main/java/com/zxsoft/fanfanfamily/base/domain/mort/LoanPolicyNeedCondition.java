package com.zxsoft.fanfanfamily.base.domain.mort;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "mort_LoanPolicyNeedCondition")
public class LoanPolicyNeedCondition extends BaseEntity {

    private static final long serialVersionUID = 3743716223654529590L;

    private Short seqNo;
    private String content;
    private BankLoanPolicy bankLoanPolicy;

    @Column(name = "seqNo",unique = true,nullable = false,columnDefinition = "smallint")
    public Short getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Short seqNo) {
        this.seqNo = seqNo;
    }

    @Column(name = "content",nullable = false,columnDefinition = "varchar(1000)")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
