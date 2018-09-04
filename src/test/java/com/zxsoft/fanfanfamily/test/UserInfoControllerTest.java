package com.zxsoft.fanfanfamily.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zxsoft.fanfanfamily.base.domain.Role;
import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import com.zxsoft.fanfanfamily.base.domain.vo.UserPermissionDTO;
import com.zxsoft.fanfanfamily.base.domain.vo.UserPermissionInner;
import com.zxsoft.fanfanfamily.base.repository.UserInfoDao;
import com.zxsoft.fanfanfamily.base.service.RoleService;
import com.zxsoft.fanfanfamily.base.service.UserInfoService;
import com.zxsoft.fanfanfamily.config.AppCrossOriginProperties;
import com.zxsoft.fanfanfamily.config.AppPropertiesConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserInfoService userInfoService;


    @Test
    @Rollback(value = true)
    public void addUserInfo() {
        //测试属性组
        String requestBody = "{\"name\":\"007\",\"password\":\"123456\"}";
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
    public void addRolesToUser() {
        Role item1 = roleService.getByKey("超级管理员").get();
        Role item2 = roleService.getByKey("用户").get();

        UserInfo userInfo = userInfoService.getByKey("006").get();
        userInfo.getRoleList().add(item1);
        userInfo.getRoleList().add(item2);

        userInfoService.save(userInfo);
    }

    @Test
    public void queryAllUserInfo() {
        RequestBuilder request;

        request = get(String.format("/api/user/queryAll"))
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
    public void queryUserInfoDao(){
        Sort sort = Sort.by("userName");
        Pageable page = PageRequest.of(0,10,sort);
        Page<UserInfo> item = userInfoDao.findAllByIdIsNotNull(page);
        if (item.getSize() > 0) {
            SerializerFeature[] serializerFeatures = {
//                SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteNullStringAsEmpty,
                    SerializerFeature.WriteNullNumberAsZero,
                    SerializerFeature.WriteNullBooleanAsFalse,
                    SerializerFeature.WriteEnumUsingToString,
//                SerializerFeature.WriteNullListAsEmpty,//list为null时改为[]，而非null。沿用null，方便前端展现。
                    SerializerFeature.DisableCircularReferenceDetect,
                    SerializerFeature.PrettyFormat
            };
            String strObj = JSON.toJSONStringWithDateFormat(item,"yyyy-MM-dd HH:mm:ss",serializerFeatures);
            System.out.println(strObj);
        }
    }
    
    @Test
    public void queryUserInfo() {
        RequestBuilder request;

        String requestBody = "{\"userName\":\"admin\"}";
        MultiValueMap<String,String> multiParams = new LinkedMultiValueMap<>();
        multiParams.add("userName","admin");

        String userName = "admin";// "402881e564c015100164c0154cbe0000";
        request = post(String.format("/api/user/userinfo"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .params(multiParams);

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
        mapValue.add("size","20");
        mapValue.add("sort","userName");
        request = get("/api/user/query")
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
    public void findUserPermission() {
        String userName = "464f7c57-d481-4417-8bc0-d8cf0a1a8e25";
        Optional<UserPermissionDTO> dto = userInfoService.findUserInfoPermission(userName);
        if (dto.isPresent()) {
            System.out.println(String.format("userInfo:%s,permission",dto.get().getInner()));
        }
    }

    @Test
    public void findUserPermissionController() {
        RequestBuilder request;
        MultiValueMap<String,String> mapValue = new LinkedMultiValueMap<>();
        mapValue.add("userName","admin");
        request = get("/api/user/userPermission")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
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