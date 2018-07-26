package com.zxsoft.fanfanfamily.test;

import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.RegionResource;
import com.zxsoft.fanfanfamily.mort.repository.RegionDao;
import com.zxsoft.fanfanfamily.mort.repository.RegionRescourceDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.*;

import static org.junit.Assert.*;

public class RegionTest extends BaseTest {

    @Autowired
    private RegionDao regionDao;
    @Autowired
    private RegionRescourceDao regionRescourceDao;

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
    @Rollback(value = false)
    public void deleteCascadeRegion(){
        Optional<Region> reg = regionDao.findRegionByCode("003");
        if (reg.isPresent()) {
            regionDao.delete(reg.get());
        }
    }

    @Test
    @Rollback(value = false)
    public void updateCascadeRegion(){
        Optional<Region> reg = regionDao.findRegionByCode("003");
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

}