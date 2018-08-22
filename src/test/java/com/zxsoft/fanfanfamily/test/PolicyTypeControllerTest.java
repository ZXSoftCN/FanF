package com.zxsoft.fanfanfamily.test;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.RequestBuilder;

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