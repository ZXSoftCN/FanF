package com.zxsoft.fanfanfamily.test;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.RequestBuilder;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PolicyTypeControllerTest extends BaseTest {

    @Test
    public void addPolicy() {
        //language=JSON
        String requestBody = "{\"name\":\"A类政策\"}";
        RequestBuilder request;
        request = post("/api/policytype/addEntity")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        try {
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(print());
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    @Test
    public void addBatch() {
        //language=JSON
        String requestBody = "[{\"name\":\"A类政策\"},{\"name\":\"B类政策\"},{\"name\":\"C类政策\"}]";
        RequestBuilder request;
        request = post("/api/policytype/saveBatch")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        try {
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(print());
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }
    @Test
    public void queryAll() {
        DateTime dt =  DateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyMMdd");
        String dateFormat = dt.toString("yyMMdd");
        System.out.println(dateFormat);
        String strPrefix = "CAS";
        String strSequ = "-";
        int iLength = 7;
        String maxNumPlus = "1" + StringUtils.repeat("0",iLength);
        int currNum = 234;
        Long maxNumLong = Long.decode(maxNumPlus) + currNum;
        String maxNum = maxNumLong.toString().substring(1);
        List<String> lstJoin = new ArrayList<>();
        lstJoin.add(strPrefix);
        lstJoin.add(dateFormat);
        lstJoin.add(maxNum);

        String code = StringUtils.join(lstJoin.iterator(),strSequ);
        System.out.println(code);

        RequestBuilder request;
        request = get("/api/policytype/queryAll")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.TEXT_PLAIN);
        try {
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(print());
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

}