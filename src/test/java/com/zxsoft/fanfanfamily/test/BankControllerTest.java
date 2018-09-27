package com.zxsoft.fanfanfamily.test;

import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import com.zxsoft.fanfanfamily.config.AppCrossOriginProperties;
import com.zxsoft.fanfanfamily.config.AppPropertiesConfig;
import com.zxsoft.fanfanfamily.mort.repository.BankDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CrossOrigin(origins = "http://localhost:8080")
public class BankControllerTest extends BaseTest {


    @Autowired
    private AppCrossOriginProperties crossOriginProperties;
    @Autowired
    private AppPropertiesConfig appPropertiesConfig;
    @Autowired
    private BankDao bankDao;


    @Test
    @Rollback(value = true)
    public void addBank() {
        //测试属性组
        List<String> lstCrossOrigin = crossOriginProperties.getLocations();
        List<String> lstOrgCC = appPropertiesConfig.getCrossOriginLocations();

        String requestBody = "{\"code\":\"006\",\"name\":\"郑州\"}";
        RequestBuilder request;
        request = post("/api/bank/add")
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
//    @Rollback(value = true)
    public void uploadBankAvatar() throws IOException {
        //测试属性组
        String fileLocal="E:\\MyProject\\DGYH\\FanF-Family\\resource\\icon\\VIP.png";
        Path uploadFile = Paths.get(fileLocal);
        RequestBuilder request;

        MultiValueMap<String,String> multiParams = new LinkedMultiValueMap<>();
        multiParams.add("bankId","4ec5f548-a55c-4a92-b4cb-550ba07802b9");
        multiParams.add("postfix","png");//附件后缀

        request = post("/api/bank/upload")
//                .accept(MediaType.MULTIPART_FORM_DATA)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(Files.readAllBytes(uploadFile))
                .params(multiParams);
//                .requestAttr("file",resource )

        try {
            mockMvc.perform(request)
                    .andExpect(status().isOk());
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    @Test
//    @Rollback(value = true)
    public void loadBankAvatar() throws IOException {
        RequestBuilder request;

        MultiValueMap<String,String> multiParams = new LinkedMultiValueMap<>();
        multiParams.add("bankId","4ec5f548-a55c-4a92-b4cb-550ba07802b9");
        multiParams.add("width","120");
        multiParams.add("height","120");
        multiParams.add("scaling","0.5");

        request = post("/api/bank/loadAvatar")
//                .accept(MediaType.MULTIPART_FORM_DATA)
                .contentType(MediaType.TEXT_PLAIN)
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
    public void queryBankDao(){
        Pageable page = PageRequest.of(0,2);
        Optional<Bank> item = bankDao.findById("67117d84-4e15-4ac7-b704-caeed79652a9");
        if (item.isPresent()) {
            System.out.println(item.get().getCode());
        }
    }
    
    @Test
    public void queryBank() {
        RequestBuilder request;

        String bankId = "67117d84-4e15-4ac7-b704-caeed79652a9";// "402881e564c015100164c0154cbe0000";
        request = get(String.format("/api/bank/get/%s",bankId))
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
    public void queryBankByPage() {
        RequestBuilder request;
        String bankId = "402881e564c015100164c0154cbe0000";
        MultiValueMap<String,String> mapValue = new LinkedMultiValueMap<>();
        mapValue.add("page","0");
        mapValue.add("size","3");
        mapValue.add("sort","code");
        request = get("/api/bank/query")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.TEXT_PLAIN)
                .params(mapValue);
        try {
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(print());
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    @Test
    public void queryTree() {
        RequestBuilder request;
        request = get("/api/bank/queryTree")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        try {
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(print());
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }

    @Test
    public void querySubs() {
        RequestBuilder request;

        String regionId = "67117d84-4e15-4ac7-b704-caeed79652a9";// "402881e564c015100164c0154cbe0000";
        MultiValueMap<String,String> multiParams = new LinkedMultiValueMap<>();
        multiParams.add("id",regionId);
        request = get("/api/bank/querySubs")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.TEXT_PLAIN)
                .params(multiParams);

        try {
            mockMvc.perform(request)
                    .andExpect(status().isOk())
                    .andDo(print());
        }catch (Exception ex){
            System.out.print(ex.getMessage());
        }
    }
}