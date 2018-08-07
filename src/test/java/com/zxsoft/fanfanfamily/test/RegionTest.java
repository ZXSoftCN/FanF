package com.zxsoft.fanfanfamily.test;

import com.zxsoft.fanfanfamily.base.domain.Organization;
import com.zxsoft.fanfanfamily.base.domain.OrganizationType;
import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.RegionResource;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import com.zxsoft.fanfanfamily.base.domain.mort.Employee;
import com.zxsoft.fanfanfamily.base.domain.mort.LimitStatus;
import com.zxsoft.fanfanfamily.config.EntityManagerUtil;
import com.zxsoft.fanfanfamily.mort.repository.LimitStatusDao;
import com.zxsoft.fanfanfamily.base.repository.OrganizationDao;
import com.zxsoft.fanfanfamily.base.repository.OrganizationTypeDao;
import com.zxsoft.fanfanfamily.mort.repository.*;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.util.*;

public class RegionTest extends BaseTest {

    @Autowired
    private RegionDao regionDao;
    @Autowired
    private RegionRescourceDao regionRescourceDao;

    @Autowired
    private PolicyTypeDao policyTypeDao;
    @Autowired
    private LimitStatusDao limitStatusDao;
    @Autowired
    private BankLoanPolicyDao bankLoanPolicyDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private LoanPolicyNeedConditionDao loanPolicyNeedConditionDao;
    @Autowired
    private LoanPolicyNeedAttachDao loanPolicyNeedAttachDao;
    @Autowired
    private BankDao bankDao;
    @Autowired
    private OrganizationTypeDao organizationTypeDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private EntityManagerUtil entityManagerUtil;
    @Autowired
    private EntityManager entityManager;


    @Test
    @Rollback(value = false)
    public void createBaseEntityA(){
        LimitStatus itemA = new LimitStatus();
        itemA.setName("充足");
        LimitStatus itemB = new LimitStatus();
        itemB.setName("紧张");
        Set<LimitStatus> collLimit = new HashSet<>();
        collLimit.add(itemA);
        collLimit.add(itemB);
        limitStatusDao.saveAll(collLimit);
    }

    @Test
    @Rollback(value = false)
    public void createBaseEntity(){
        OrganizationType itemType = new OrganizationType();
        itemType.setName("分公司");
        OrganizationType itemType2 = new OrganizationType();
        itemType2.setName("加盟店");
        Set<OrganizationType> collType = new HashSet<>();
        collType.add(itemType);
        collType.add(itemType2);

        organizationTypeDao.saveAll(collType);
    }

    @Test
    @Rollback(value = true)
    public void createDefaultRegion(){
        Region region = new Region();
        region.setCode("002");
        region.setName("广州");
        region.setLogoUrl("/resources/img/region/001.icon");

        regionDao.save(region);
    }

    @Test
    @Rollback(value = false)
    public void createCascadeRegion() {
        Region reg = new Region();
        reg.setCode("003");
        reg.setName("深圳");
        reg.setLogoUrl("/icon/test.icon");
        regionDao.save(reg);

        RegionResource resItem1 = new RegionResource();
        resItem1.setRegion(reg);
        resItem1.setType("icon");
        resItem1.setResUrl("/icon/1.icon");

        RegionResource resItem2 = new RegionResource();
        resItem2.setRegion(reg);
        resItem2.setType("icon");
        resItem2.setResUrl("/icon/2.icon");

        HashSet<RegionResource> coll = new HashSet<>();
        coll.add(resItem1);
        coll.add(resItem2);

        regionRescourceDao.saveAll(coll);
    }

    @Test
    public void queryRegionCascade(){
//        Region region = createBaseEntity();

//        Employee employee = entityManager.createQuery(
//                "select e " +
//                        "from Employee e " +
//                        "left join fetch e.projects " +
//                        "where " +
//                        "	e.username = :username and " +
//                        "	e.password = :password",
//                Employee.class)
//                .setParameter( "username", username)
//                .setParameter( "password", password)
//                .getSingleResult();
    }

    @Test
    @Rollback(value = false)
    public void createOrganization(){
        Organization item = new Organization();
        item.setCode("901");
        item.setName("一号店");

        Optional<OrganizationType> itemType = organizationTypeDao.findFirstByNameContaining("分公司");
        if (itemType.isPresent()) {
            item.setOrganizationTypeId(itemType.get().getId());
        }
        Optional<Region> itemRegion = regionDao.findFirstByCodeOrName("","深圳");
        if (itemRegion.isPresent()) {
            item.setRegion(itemRegion.get());
        }

        organizationDao.save(item);
    }
    @Test
    @Rollback(value = false)
    public void createSubOrganization(){
        Organization item = new Organization();
        item.setCode("902");
        item.setName("一号店01铺");

        Optional<OrganizationType> itemType = organizationTypeDao.findFirstByNameContaining("分");
        if (itemType.isPresent()) {
            item.setOrganizationTypeId(itemType.get().getId());
        }
        Optional<Region> itemRegion = regionDao.findFirstByCodeOrName("","深圳");
        if (itemRegion.isPresent()) {
            item.setRegion(itemRegion.get());
        }
        Optional<Organization> itemOrg = organizationDao.findFirstByNameContaining("一");
        if (itemOrg.isPresent()) {
            item.setParentOrg(itemOrg.get());
        }

        organizationDao.save(item);
    }

    @Test
    public void queryOrganization(){
        DateTime dtQuery = DateTime.parse("2018-07-29");
        Date dtItem = dtQuery.toDate();
        List<Organization> itemQuery = organizationDao.customQueryByTestDate(dtItem);
        if (itemQuery.size() > 0) {
            System.out.println("OK");
        } else {
            System.out.println("failure");
        }
    }

    @Test
    public void queryOrgByEM() {
        String orgId = "befe70e5-b83b-46b3-9ccd-c295eab6c60b";
//        Organization itemOrg = entityManager.getReference(Organization.class,orgId);
        Organization itemOrg = entityManager.find(Organization.class,orgId);
        Assert.assertNotEquals(null, itemOrg);
    }

    @Test
    public void queryOrganizatonType(){
        Optional<OrganizationType> itemQuery = organizationTypeDao.findFirstByNameContaining("分公司");
        if (itemQuery.isPresent()) {
            System.out.println("OK");
        } else {
            System.out.println("failure");
        }
    }

    @Test
    public void queryParentOrg() {
        List<Organization> lstAll;
        Page<Organization> plstQuery;
        Pageable pageable = PageRequest.of(0,2);
        lstAll = organizationDao.customQueryByParentOrgCode("901");
//        lstAll = organizationDao.queryByParentOrgNameContaining("店");
        plstQuery = organizationDao.queryByParentOrgCode("901",pageable);
        if (lstAll.size() > 0) {
            System.out.println("Ok");
        } else {
            System.out.println("failure");
        }

        if (plstQuery.getSize() > 0) {
            System.out.println("Page Ok");
        } else {
            System.out.println("Page failure");
        }
    }

    @Test
    @Rollback(value = false)
    public void createEmployee() {
        Employee itemNew = new Employee();
        itemNew.setCode("E002");
        itemNew.setName("李四");
        itemNew.setAliasName("小子");
        itemNew.setIntroduction("巴拉巴拉巴拉");

        Optional<Organization> itemOrg = organizationDao.findFirstByCodeOrName("901","");
        itemNew.setOrganization(itemOrg.orElse(null));

        employeeDao.save(itemNew);
    }

    @Test
    public void queryEmployeeByOrg() {
        List<Employee> lstQuery;
        lstQuery = employeeDao.queryAllByOrganization_Code("901");

        if (lstQuery.size() > 0) {
            System.out.println("OK");
            for (Employee item : lstQuery) {
                System.out.println(item.getName());
            }
        } else {
            System.out.println("failure");
        }
    }

    @Test
    public void queryRegionDao(){
        Pageable page = PageRequest.of(0,2);
        Optional<Region> item = regionDao.findById("e3a9bbec-b519-4364-9d6c-55c08cbe7e32");
        Optional<Region> rltRegion = regionDao.findFirstByCode("003");
        Optional<Region> rltRegion2 = regionDao.queryFirstByCode("003");
        if (item.isPresent()) {
            System.out.println(item.get().getCode());
        }
    }


}