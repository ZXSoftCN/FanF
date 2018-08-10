package com.zxsoft.fanfanfamily.test;

import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import com.zxsoft.fanfanfamily.base.repository.UserInfoDao;
import com.zxsoft.fanfanfamily.config.AppCrossOriginProperties;
import com.zxsoft.fanfanfamily.config.AppPropertiesConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CrossOrigin(origins = "http://localhost:8080")
public class UserInfoControllerTest extends BaseTest {


    @Autowired
    private AppCrossOriginProperties crossOriginProperties;
    @Autowired
    private AppPropertiesConfig appPropertiesConfig;
    @Autowired
    private UserInfoDao userInfoDao;


    @Test
    @Rollback(value = true)
    public void addUserInfo() {
        //测试属性组
        String requestBody = "{\"name\":\"006\",\"password\":\"123456\"}";
        RequestBuilder request;
        request = post("/api/user/add")
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
    public void uploadUserInfoAvatar() throws IOException {
        //测试属性组
        String fileLocal="E:\\MyProject\\DGYH\\FanF-Family\\resource\\icon\\VIP.png";
        Path uploadFile = Paths.get(fileLocal);
        RequestBuilder request;

        MultiValueMap<String,String> multiParams = new LinkedMultiValueMap<>();
        multiParams.add("id","4ec5f548-a55c-4a92-b4cb-550ba07802b9");
        multiParams.add("postfix","png");//附件后缀

        request = post("/api/user/updateAvatar")
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
    public void loadUserInfoAvatar() throws IOException {
        RequestBuilder request;

        MultiValueMap<String,String> multiParams = new LinkedMultiValueMap<>();
        multiParams.add("id","4ec5f548-a55c-4a92-b4cb-550ba07802b9");
        multiParams.add("width","120");
        multiParams.add("height","120");
        multiParams.add("scaling","0.5");

        request = post("/api/user/loadAvatar")
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
    public void queryUserInfoDao(){
        Pageable page = PageRequest.of(0,2);
        Optional<UserInfo> item = userInfoDao.findById("67117d84-4e15-4ac7-b704-caeed79652a9");
        if (item.isPresent()) {
            System.out.println(item.get().getUserName());
        }
    }
    
    @Test
    public void queryUserInfo() {
        RequestBuilder request;

        String userInfoId = "67117d84-4e15-4ac7-b704-caeed79652a9";// "402881e564c015100164c0154cbe0000";
        request = get(String.format("/api/user/get/%s",userInfoId))
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
    public void queryUserInfoByPage() {
        RequestBuilder request;
        String userInfoId = "402881e564c015100164c0154cbe0000";
        MultiValueMap<String,String> mapValue = new LinkedMultiValueMap<>();
        mapValue.add("page","0");
        mapValue.add("size","3");
        mapValue.add("sort","code");
        request = get("/api/userInfo/query")
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
}