package com.zxsoft.fanfanfamily.base.domain.mort;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.SimpleEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sys_LimitStatus")
public class LimitStatus extends SimpleEntity {

    private static final long serialVersionUID = -2087805439623542827L;
}
