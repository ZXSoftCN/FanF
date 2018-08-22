package com.zxsoft.fanfanfamily.base.domain.mort;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.SimpleEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

@Entity
@Table(name = "sys_PolicyType")
public class PolicyType extends SimpleEntity {
    private static final long serialVersionUID = -2741106629975767926L;

    private int sortNo;

    @GenericGenerator(name = "sortGene", strategy = "native")
    @GeneratedValue(generator = "sortGene")
    @Column(columnDefinition = "int UNSIGNED")
    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }
}
