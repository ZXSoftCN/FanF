package com.zxsoft.fanfanfamily.test;

import com.zxsoft.fanfanfamily.base.domain.Organization;
import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.RegionResource;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import com.zxsoft.fanfanfamily.base.domain.mort.Employee;
import com.zxsoft.fanfanfamily.mort.repository.*;
import com.zxsoft.fanfanfamily.base.repository.OrganizationDao;
import com.zxsoft.fanfanfamily.base.repository.OrganizationTypeDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RegionModifyTest extends BaseTest {

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
    private EntityManager entityManager;

    @Test
    @Rollback(value = false)
    public void updateCascadeRegion(){
        Optional<Region> reg = regionDao.findFirstByCode("003");
        if (reg.isPresent()) {
            Set<RegionResource> coll = reg.get().getResources();
            RegionResource[] arrRes = new RegionResource[]{};
            coll.toArray(arrRes);
            List<RegionResource> lst = new ArrayList<>(coll);
            RegionResource item = lst.get(0);
            item.setResUrl("/icon/4.icon");

            reg.get().setName("深圳（修改4）");
//            regionDao.save(reg.get());//都会级联修改，特别注意级联删除
            regionRescourceDao.save(item);
//            System.out.println(item.getResUrl());
        }
    }

    @Test
    @Rollback(value = false)
    public void modifyEmployee() {
        Optional<Employee> itemModify = employeeDao.findFirstByCode("E001");
        if (!itemModify.isPresent()) {
            System.out.println("modify failure");
            return;
        }
        itemModify.get().setAliasName("小三儿");

        Optional<Organization> itemOrg = organizationDao.findFirstByCodeOrName("901","");
        itemModify.get().setOrganization(itemOrg.orElse(null));

        employeeDao.save(itemModify.get());
    }

    @Test
    @Rollback(value = true)
    public void deleteBank() {
        Optional<Bank> itemDel = bankDao.findFirstByCodeIgnoreCase("B002");

        String strQuery = "select e from Region e left join fetch e.banks where e.id = :id";
        List<Region> lstRegionCustom = entityManager.createQuery(strQuery)
                                        .setParameter("id",itemDel.get().getId())
                                        .getResultList();
        
        List<Region> lstRegion = regionDao.queryAllByBanksIn(itemDel.isPresent() ? itemDel.get(): null);
//        List<Region> lstRegionJoin = entityManager.createQuery("select r from Region r left join fetch r.banks b " +
//                "where b.id = :bankId")
//                .setParameter("bankId",itemDel.get().getId())
//                .getResultList();
//        for (Region item : lstRegionJoin) {
//            item.getBanks().remove(itemDel);a
//            regionDao.saveAndFlush(item);
//        }

//        for (Region item : lstRegion) {
//            Region itemIncludeBanks =  entityManager.createQuery(
//                    strQuery,
//                    Region.class)
//                    .setParameter( "id", item.getId())
//                    .getSingleResult();
//            itemIncludeBanks.getBanks().remove(itemDel);
//
//            regionDao.saveAndFlush(item);
//        }
//        regionDao.saveAll(lstRegion);
//        regionDao.flush();
        bankDao.delete(itemDel.orElse(null));
    }

    @Test
    @Rollback(value = false)
    public void deleteCascadeRegion(){
        Optional<Region> reg = regionDao.findFirstByCode("002");
        if (reg.isPresent()) {
            regionDao.delete(reg.get());
        }
    }
}
