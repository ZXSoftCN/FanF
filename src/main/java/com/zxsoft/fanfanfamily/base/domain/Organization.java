package com.zxsoft.fanfanfamily.base.domain;

import com.zxsoft.fanfanfamily.base.domain.mort.Employee;
import com.zxsoft.fanfanfamily.base.repository.OrganizationTypeDao;
import com.zxsoft.fanfanfamily.common.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Optional;
import java.util.Set;

/*
*   分公司、部门、加盟商、门店等组织对象。
 */
@Entity
@Table(name = "sys_Organization")
@NamedEntityGraph(name = "Organization.lazy",
        attributeNodes = {@NamedAttributeNode("employees")})
public class Organization extends SimpleEntity{

    private static final long serialVersionUID = 4207316783208672066L;

    private String code;
    private Organization parentOrg;
    private Region region;
    private Set<Employee> employees;

    @Autowired
    private SpringUtil springUtil;
    private String organizationTypeId;
    private Optional<OrganizationType> organizationType;


    @Column(name = "code",unique = true,nullable = false,columnDefinition = "varchar(36)")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH})
    @JoinColumn(name = "parentOrgId",columnDefinition = "varchar(36) DEFAULT ''")
    public Organization getParentOrg() {
        return parentOrg;
    }

    public void setParentOrg(Organization parentOrg) {
        this.parentOrg = parentOrg;
    }

    @OneToMany(cascade = {CascadeType.REMOVE},mappedBy = "organization",fetch = FetchType.LAZY)
    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    @Column(name = "organizationTypeId",columnDefinition = "varchar(36)")
    public String getOrganizationTypeId() {
        return organizationTypeId;
    }

    public void setOrganizationTypeId(String organizationTypeId) {
        this.organizationTypeId = organizationTypeId;
    }

    @Transient
    public Optional<OrganizationType> getOrganizationType() {
        if (organizationTypeId != null && !organizationTypeId.isEmpty()) {
            OrganizationTypeDao refDao = (OrganizationTypeDao)springUtil.getBean("organizationTypeDao");
            return refDao.findById(organizationTypeId);
        } else {
            return organizationType.empty();
        }
    }

}
