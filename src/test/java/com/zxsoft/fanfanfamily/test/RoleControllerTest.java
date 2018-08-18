package com.zxsoft.fanfanfamily.test;

import com.zxsoft.fanfanfamily.base.service.RoleService;
import com.zxsoft.fanfanfamily.base.service.impl.RoleServiceImp;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleControllerTest extends BaseTest {

    @Autowired
    private RoleService roleService;

    @Test
    public void addBatchRole() {
        String requestBody = "[{\"roleName\":\"超级管理员\"},{\"roleName\":\"用户\"},{\"roleName\":\"访客\"}]";
        RequestBuilder request;
        request = post("/api/role/saveBatch")
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
    public void queryRoles() {
        RequestBuilder request;
        request = get("/api/role/queryAll")
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