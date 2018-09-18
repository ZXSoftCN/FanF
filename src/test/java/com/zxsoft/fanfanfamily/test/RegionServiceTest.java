package com.zxsoft.fanfanfamily.test;

import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.mort.service.RegionService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assume;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class RegionServiceTest extends BaseTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RegionService regionService;


    @Test
    public void testSync() {

        String code = "re-20180913-00043";
        String[] strSplit = StringUtils.split(code,"-");
        if (strSplit.length < 1) {
            return;
        }
        String lastCodeNum = "";
        if (strSplit.length > 1) {
            lastCodeNum = strSplit[strSplit.length - 1];
        } else{
            lastCodeNum = strSplit[0];
        }
        if (StringUtils.isEmpty(lastCodeNum)) {
            return;
        }
        Long codeMax = Long.parseLong(lastCodeNum);
        Assume.assumeTrue(codeMax > 0);
    }

    @Test
    public void getPath(){
        logger.info("logger level:Info.Beggining--------------");
        String strPath = regionService.getPath().toAbsolutePath().toString();
        String strBasePath = regionService.getOriginPath().toAbsolutePath().toString();
        System.out.println(String.format("BaseService rootPath:%s",strBasePath));
        System.out.println(String.format("RegionService rootPath:%s",strPath));
    }

    @Test
    public void addResource() {

    }

    @Test
    public void modifyResource() {
    }

    @Test
    public void modifyResourceFile() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void getById() {
        Optional<Region> item = regionService.getById("402883e664b6d10e0164b6d13b5c0000");
        if (item.isPresent()) {
            System.out.println("OK");
            System.out.println(item.get().getName());
        }

    }
}