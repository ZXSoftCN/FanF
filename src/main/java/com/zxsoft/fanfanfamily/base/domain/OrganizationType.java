package com.zxsoft.fanfanfamily.base.domain;

import javax.naming.Name;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
*   用于区分公司旗下各类组织类型：分公司、控股子公司、部门、加盟商、门店等。
*   后续可加入直营、合营、加盟等不同样形式下的政策发放。
 */
@Entity
@Table(name = "sys_OrganizationType")
public class OrganizationType extends SimpleEntity {
    private static final long serialVersionUID = 2727857488137976651L;
}
