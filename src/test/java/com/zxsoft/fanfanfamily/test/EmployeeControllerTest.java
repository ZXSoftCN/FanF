package com.zxsoft.fanfanfamily.test;

import com.zxsoft.fanfanfamily.config.AppPropertiesConfig;
import com.zxsoft.fanfanfamily.mort.repository.BankDao;
import com.zxsoft.fanfanfamily.mort.repository.EmployeeDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerTest extends BaseTest {

    @Autowired
    private AppPropertiesConfig appPropertiesConfig;
    @Autowired
    private EmployeeDao employeeDao;

    @Test
    public void addEmployee() {
        List<String> lstOrgCC = appPropertiesConfig.getCrossOriginLocations();

        //language=JSON
        String requestBody = "{\"code\":\"002\",\"name\":\"阿三\",\"introduction\":\"say something\",\n" +
                "  \"organizationId\":\"9219f6f0-bd06-4ce6-ae5d-27a368948b78\"}";
        RequestBuilder request;
        request = post("/api/employee/add")
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
    public void moidifyEmployee() {

        String requestBody = "{\"id\":\"8d5fdff7-9e57-4fc6-a94a-e32caea46707\",\"code\":\"002\",\"name\":\"阿三\",\"introduction\":\"say something\",\n" +
                "  \"organizationId\":\"06c6bb26-a744-4843-b667-1c6472cd43e7\"}";
        RequestBuilder request;
        request = post("/api/employee/modify")
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
    public void deleteEmployee() {

        String requestBody = "{\"id\":\"8d5fdff7-9e57-4fc6-a94a-e32caea46707\"}";
        RequestBuilder request;
        request = post("/api/employee/delete")
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
    public void deleteBatchEmployee() {

        //language=JSON
        String requestBody = "{\"ids\":[\"448c2dc7-25c1-4968-9f83-9a1f6ac6cb90\",\"6760be38-ffc4-4be0-b738-d38d357c1da7\"]}";
        RequestBuilder request;
        request = post("/api/employee/deleteBatch")
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
    public void getEmployee() {
        RequestBuilder request;
        String regionId = "24f51bb5-b539-48b4-8c7a-69f6d1742926";// "402881e564c015100164c0154cbe0000";
        request = get(String.format("/api/employee/get/%s",regionId))
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

    @Test
    public void queryEmployee() {
        String requestBody = "{\"page\":0,\"size\":15,\"sort\":[\"code\",\"id\"],\"direction\":\"Sort.Direction.DESC\"}";
        RequestBuilder request;
        MultiValueMap<String,String> mapValue = new LinkedMultiValueMap<>();
        mapValue.add("page","0");
        mapValue.add("size","3");
        mapValue.add("sort","code,desc");
        mapValue.add("sort","id,asc");
        request = post("/api/employee/query")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .params(mapValue);
        /*
        *
        * .param("size","3")
                .param("page","0")
                .param("sort","code,desc");
         */

        try {
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(print());
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    @Test
    public void queryAllEmployee() {

        RequestBuilder request;

        request = post("/api/employee/queryAll")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        /*
        *
        * .param("size","3")
                .param("page","0")
                .param("sort","code,desc");
         */

        try {
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(print());
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    @Test
//    @Rollback(value = true)
    public void uploadEmployeeAvatar() throws IOException {
        //测试属性组
        String fileLocal="E:\\MyProject\\DGYH\\FanF-Family\\resource\\icon\\VIP.png";
        Path uploadFile = Paths.get(fileLocal);
        RequestBuilder request;

        MultiValueMap<String,String> multiParams = new LinkedMultiValueMap<>();
        multiParams.add("id","2fbe860a-885e-4f63-9c21-9c04bf32ee85");
        multiParams.add("fileName","VIP");
        multiParams.add("postfix","png");//附件后缀

        request = post("/api/employee/updateAvatar")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(Files.readAllBytes(uploadFile))
                .params(multiParams);
//                .requestAttr("file",resource )

        try {
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(print());
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    @Test
//    @Rollback(value = true)
    public void loadEmployeeAvatar() throws IOException {
        RequestBuilder request;

        MultiValueMap<String,String> multiParams = new LinkedMultiValueMap<>();
        String id = "24f51bb5-b539-48b4-8c7a-69f6d1742926";
        multiParams.add("width","120");
        multiParams.add("height","120");
        multiParams.add("scaling","0.5");

        request = post(String.format("/api/employee/loadAvatar/%s",id))
//                .accept(MediaType.MULTIPART_FORM_DATA)
                .contentType(MediaType.TEXT_PLAIN);
//                .params(multiParams);
//                .requestAttr("file",resource )

        try {
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(print());
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }
}