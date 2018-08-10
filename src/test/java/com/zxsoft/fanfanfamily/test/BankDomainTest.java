package com.zxsoft.fanfanfamily.test;

import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import com.zxsoft.fanfanfamily.base.repository.OrganizationDao;
import com.zxsoft.fanfanfamily.base.repository.OrganizationTypeDao;
import com.zxsoft.fanfanfamily.common.EntityManagerUtil;
import com.zxsoft.fanfanfamily.mort.repository.BankDao;
import com.zxsoft.fanfanfamily.mort.repository.RegionDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BankDomainTest extends BaseTest {

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
    @Autowired
    private RegionDao regionDao;

    @Test
    @Rollback(value = false)
    public void createBank() {
        Bank itemNew = new Bank();
        itemNew.setCode("B001");
        itemNew.setName("工商银行");
        itemNew.setFullName("中国工商银行");
        itemNew.setApprovedDay(15);
        itemNew.setLoanDay(20);
        itemNew.setIconUrl("/bank/001.icon");


        bankDao.save(itemNew);
        Optional<Region> itemRegion = regionDao.queryFirstByCode("003");
        if (itemRegion.isPresent()) {
            itemRegion.get().getBanks().add(itemNew);
            regionDao.save(itemRegion.get());
        }
    }

    @Test
    @Rollback(value = false)
    public void createBankB() {
        Bank itemNew = new Bank();
        itemNew.setCode("B002");
        itemNew.setName("建设银行");
        itemNew.setFullName("中国建设银行");
        itemNew.setApprovedDay(10);
        itemNew.setLoanDay(18);
        itemNew.setIconUrl("/bank/002.icon");

        Bank itemSubNew = new Bank();
        itemSubNew.setCode("B003");
        itemSubNew.setName("达路分行");
        itemSubNew.setFullName("中国建设银行东莞八达路分行");
        itemSubNew.setApprovedDay(10);
        itemSubNew.setLoanDay(18);
        itemSubNew.setIconUrl("/bank/003.icon");
        itemSubNew.setParentBank(itemNew);

        Set<Bank> collBank = new HashSet<>();
        collBank.add(itemNew);
        collBank.add(itemSubNew);

        bankDao.saveAll(collBank);
    }

    /*
     *通过bank建立同region的关系
     */
    @Test
    @Rollback(value = false)
    public void createBankC() {
        Bank itemNew = new Bank();
        itemNew.setCode("B004");
        itemNew.setName("招商银行");
        itemNew.setFullName("中国招商银行");
        itemNew.setApprovedDay(10);
        itemNew.setLoanDay(12);
        itemNew.setIconUrl("/bank/004.icon");

        Set<Bank> collBank = new HashSet<>();
        collBank.add(itemNew);

        bankDao.saveAll(collBank);

        Optional<Region> itemRegion = regionDao.queryFirstByCode("002");
        if (itemRegion.isPresent()) {
            itemRegion.get().getBanks().add(itemNew);
            regionDao.save(itemRegion.get());
        }


    }

    @Test
    @Rollback(value = false)
    public void createBankRegion() {
        Optional<Bank> itemBank = bankDao.findFirstByCodeIgnoreCase("B001");
        Optional<Bank> itemBank2 = bankDao.findFirstByCodeIgnoreCase("B002");
        Optional<Bank> itemBank3 = bankDao.findFirstByCodeIgnoreCase("B003");
        Optional<Region> itemRegion = regionDao.queryFirstByCode("003");
        Optional<Region> itemRegion2 = regionDao.queryFirstByCode("002");
        Set<Bank> collBank = new HashSet<>();
        Set<Bank> collBank2 = new HashSet<>();

        boolean isAdd = itemBank.isPresent()?collBank.add(itemBank.get()):false;
        collBank.add(itemBank2.orElse(null));

        collBank2.add(itemBank2.orElse(null));
        collBank2.add(itemBank3.orElse(null));
        if (itemRegion.isPresent()) {
//            Set<Bank> collExists = itemRegion.get().getBanks();
            for (Bank item : collBank) {
                if (!itemRegion.get().getBanks().contains(item)) {
                    itemRegion.get().getBanks().add(item);
                }
            }
//            itemRegion.get().getBanks().addAll(collBank);
            regionDao.save(itemRegion.get());
        }
        if (itemRegion2.isPresent()) {
            for (Bank item : collBank2) {
                if (!itemRegion2.get().getBanks().contains(item)) {
                    itemRegion2.get().getBanks().add(item);
                }
            }
            regionDao.save(itemRegion2.get());
        }
    }
}
