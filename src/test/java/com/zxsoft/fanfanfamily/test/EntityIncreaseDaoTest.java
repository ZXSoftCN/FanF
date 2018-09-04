package com.zxsoft.fanfanfamily.test;

import com.alibaba.fastjson.JSON;
import com.zxsoft.fanfanfamily.base.domain.EntityIncrease;
import com.zxsoft.fanfanfamily.base.repository.EntityIncreaseDao;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.*;

public class EntityIncreaseDaoTest extends BaseTest {


    @Autowired
    private EntityIncreaseDao entityIncreaseDao;

    @Test
    public void findFirstByEntityNameIgnoreCase() {
        Optional<EntityIncrease> itemQuery = entityIncreaseDao.findFirstByEntityNameIgnoreCase("menu");
        if (itemQuery.isPresent()) {

            System.out.println(JSON.toJSONString(itemQuery.get(), true));
            Assert.assertNotNull(itemQuery.get());
        }else{
            Assert.fail("未能查询到");
        }

    }

    @Test
    public void add() {
        EntityIncrease entity = new EntityIncrease();
        entity.setEntityName("Menu");
        entity.setPrefix("Men");

        entityIncreaseDao.save(entity);
    }

    @Test
    public void updateCodeNumMax() {
        Optional<EntityIncrease> itemQuery = entityIncreaseDao.findFirstByEntityNameIgnoreCase("menu");
        if (itemQuery.isPresent()) {

            System.out.println(JSON.toJSONString(itemQuery.get(), true));
            entityIncreaseDao.updateCodeNumMax(itemQuery.get().getEntityName(),2);
            Assert.assertTrue(true);
        }else{
            Assert.fail("未能查询到");
        }
    }

    @Test
    public void updateSortNoMax() {
        Optional<EntityIncrease> itemQuery = entityIncreaseDao.findFirstByEntityNameIgnoreCase("menu");
        if (itemQuery.isPresent()) {

            System.out.println(JSON.toJSONString(itemQuery.get(), true));
            entityIncreaseDao.updateSortNoMax(itemQuery.get(), 1);
            Assert.assertTrue(true);
        }else{
            Assert.fail("未能查询到");
        }
    }
}