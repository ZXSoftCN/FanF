package com.zxsoft.fanfanfamily.test;


import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import com.zxsoft.fanfanfamily.mort.domain.vo.RegionCount;
import com.zxsoft.fanfanfamily.mort.repository.BankDao;
import com.zxsoft.fanfanfamily.mort.repository.RegionDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

@Transactional
@Rollback(value = true)
@RunWith(SpringRunner.class)
@SpringBootTest
public class DomainBaseTestA {

    @Autowired
    private WebApplicationContext webContext;


    private MockMvc mockMvc;

    @Autowired
    private BankDao bankDao;

    @Autowired
    private RegionDao regionDao;

    @Before
    public void setupMockMvc(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
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
    public void createBankGroup(){
        Optional<Region> region = regionDao.findById("402883e664b6d10e0164b6d13b5c0000");
        if (region.isPresent()) {
            Bank bank1 = new Bank();
            bank1.setBankCode("01001");
            bank1.setBankName("01001");
            bank1.setRegion(region.get());

            Bank bank2 = new Bank();
            bank2.setBankCode("01002");
            bank2.setBankName("01002");
            bank2.setRegion(region.get());

            List<Bank> lstSave = new ArrayList<>();
            lstSave.add(bank1);
            lstSave.add(bank2);

            bankDao.saveAll(lstSave);
        }
    }

    @Test
    public void queryBankByRegion(){
        Region itemReg = regionDao.findById("402883e664b6d10e0164b6d13b5c0000").get();
        List<Bank> lstB = bankDao.findBanksByRegion(itemReg);
        if (lstB.size() > 0) {
            for (Bank item : lstB) {
                System.out.print(item.getBankName());
            }
        }

        Pageable pageable = PageRequest.of(0,1);

        Page<Bank> plstB = bankDao.queryBanksByRegion(itemReg,pageable);
        for (Bank item : plstB) {
            System.out.print(item.getBankName());
        }
    }

    @Test
    public void queryBankByRegionProperty(){
        Region itemReg = regionDao.findById("402883e664b6d10e0164b6d13b5c0000").get();

        Pageable pageable = PageRequest.of(0,1);

        Page<Bank> plstB = bankDao.queryBankByRegion_Code(itemReg.getCode(),pageable);
        for (Bank item : plstB) {
            System.out.print(item.getBankName());
        }
    }

    @Test
    public void queryCustomJdbcRegion(){
//        List<Region> lst = regionDao.customQuery();
        List<Region> lst2 = regionDao.customQueryCriteria();
    }
    @Test
    public void queryCustomRegion(){
        Pageable page = PageRequest.of(0,2);
        Page<RegionCount> plst = regionDao.findRegionCountById("402883e664b6d10e0164b6d13b5c0000",page);
        for (RegionCount item : plst){
            System.out.println(String.format("Region:%s,Count:%d",item.getRegion().getCode(),item.getBankCountNotNull()));
        }
    }

    @Test
    @Rollback(value = false)
    public void modifyRegion(){
        Optional<Region> itemR = regionDao.findRegionByCode("001");
        if (itemR.isPresent()) {
            Region item = itemR.get();
//            item.setLogoUrl(String.format(item.getLogoUrl() + "/%s","modify") );
//            regionDao.save(itemR.get());
            regionDao.modifyLogoUrlById(String.format(item.getLogoUrl() + "/%s","modify"),item.getId());
        }
    }

    @Test
    @Rollback(value = false)
    public void testCreateDefaultBank(){
        Bank bank = new Bank();
    }

    @Test
    @Rollback(value = false)
    public void testBank(){
        Bank bankP = new Bank();
        bankP.setBankCode("01");
        bankP.setBankName("PBank1");

        bankDao.save(bankP);

//        Bank bankC1 = new Bank();
//        bankC1.setBankCode("02");
//        bankC1.setBankName("CBank1");
//        bankC1.setParentBank(bankP);
//
//        Bank bankC2 = new Bank();
//        bankC2.setBankCode("03");
//        bankC2.setBankName("CBank2");
//        bankC2.setParentBank(bankP);
//
//        Set<Bank> collBank = new HashSet<>();
//        collBank.add(bankC1);
//        collBank.add(bankC2);
//
//        List<Bank> lstBank = bankDao.saveAll(collBank);

    }
}
