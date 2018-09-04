package com.zxsoft.fanfanfamily.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zxsoft.fanfanfamily.base.domain.Menu;
import com.zxsoft.fanfanfamily.base.repository.MenuDao;
import com.zxsoft.fanfanfamily.base.service.MenuService;
import com.zxsoft.fanfanfamily.base.service.impl.MenuServiceImpl;
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
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CrossOrigin(origins = "http://localhost:8080")
public class MenuControllerTest extends BaseTest {


    @Autowired
    private AppCrossOriginProperties crossOriginProperties;
    @Autowired
    private AppPropertiesConfig appPropertiesConfig;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private MenuService menuService;

    @Test
    public void testMenuQuery() {
        List<Menu> lst = menuService.findAll();
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
        String strObj = JSON.toJSONStringWithDateFormat(lst,"yyyy-MM-dd HH:mm:ss",serializerFeatures);
        System.out.println(strObj);
    }

    @Test
    @Rollback(value = true)
    public void addMenu() {
        //测试属性组
        List<String> lstCrossOrigin = crossOriginProperties.getLocations();
        List<String> lstOrgCC = appPropertiesConfig.getCrossOriginLocations();

        //language=JSON
        String requestBody = "{\"name\":\"工作台1\",\"pathKey\":\"desk1$\",\"iconName\":\"home\",\"status\":false}";
        RequestBuilder request;
        request = post("/api/menu/add")
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
    @Rollback(value = true)
    public void addBatchMenu() {
        //language=JSON
        String requestBody = "[{\"name\":\"设置中心\",\"pathKey\":\"set$\",\"iconName\":\"set\"}]";
        RequestBuilder request;
        request = post("/api/menu/addbatch")
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
    @Rollback(value = true)
    public void addBaseMenu() {
        //language=JSON
        String requestBody = "[{\"name\":\"基础数据\",\"pathKey\":\"set$\",\"iconName\":\"set\"},{\"name\":\"银行\",\"pathKey\":\"set$\",\"iconName\":\"set\"}]";
        RequestBuilder request;
        request = post("/api/menu/addbatch")
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
    @Rollback(value = true)
    public void addBaseSubMenu() {
        //language=JSON
        String requestBody = "[{\"name\":\"银行\",\"pathKey\":\"set$\",\"iconName\":\"set\"},{\"name\":\"政策类型\",\"pathKey\":\"set$\",\"iconName\":\"set\"}]";
        RequestBuilder request;
        request = post("/api/menu/addbatchwithparent/543d9393-b89f-494d-8ce8-16015b37aebc")
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
    @Rollback(value = true)
    public void addBaseSubMenuTest1() {
        //language=JSON
        String requestBody = "[{\"name\":\"测试子节点\",\"pathKey\":\"set$\",\"iconName\":\"set\"}]";
        RequestBuilder request;
        request = post("/api/menu/addbatchwithparent/420b1102-9776-4472-b8d2-0e6b5781c37b")
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
    @Rollback(value = true)
    public void addSubMenu() {
        //language=JSON
        String requestBody = "{\"name\":\"概览\",\"pathKey\":\"desk$/index\",\"iconName\":\"\"}";
        RequestBuilder request;
        request = post("/api/menu/addwithparent/20dc3150-a4fb-489d-8817-61ac143ec436")
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
    @Rollback(value = true)
    public void addBatchSubMenu1() {
        //language=JSON
        String requestBody = "[{\"name\":\"图表\",\"pathKey\":\"echarts\",\"iconName\":\"\"},\n" +
                "  {\"name\":\"编辑器\",\"pathKey\":\"editor\",\"iconName\":\"\"},\n" +
                "  {\"name\":\"聊天室\",\"pathKey\":\"chat\",\"iconName\":\"\"}]";
        RequestBuilder request;
        request = post("/api/menu/addbatchwithparent/20dc3150-a4fb-489d-8817-61ac143ec436")
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
    @Rollback(value = true)
    public void addBatchSubMenu2() {
        //language=JSON
        String requestBody = "[{\"name\":\"用户管理\",\"pathKey\":\"set$/userManage\",\"iconName\":\"userManage\"},\n" +
                "  {\"name\":\"角色管理\",\"pathKey\":\"set$/roleManage\",\"iconName\":\"roleManage\"},\n" +
                "  {\"name\":\"权限管理\",\"pathKey\":\"set$/moduleManage\",\"iconName\":\"moduleManage\"}]";
        RequestBuilder request;
        request = post("/api/menu/addbatchwithparent/e3feb337-83c7-422f-acba-e775a57c623a")
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
    public void queryAllMenu() {
        RequestBuilder request;
        request = get("/api/menu/queryAll")
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
    public void queryTopMenuAllTree() {
        RequestBuilder request;
        request = get("/api/menu/queryTopMenuAllTree")
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
    public void queryMenuDao(){
        Pageable page = PageRequest.of(0,2);
        Optional<Menu> item = menuDao.findById("67117d84-4e15-4ac7-b704-caeed79652a9");
        if (item.isPresent()) {
            System.out.println(item.get().getName());
        }
    }
    
    @Test
    public void queryMenuEntity() {
        String requestBody = "{\"id\":\"cf295a3e-44d8-4485-8361-112cadd57ac9\"}";
        RequestBuilder request;

        request = post(String.format("/api/menu/getEntity"))
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
    public void queryMenu() {
        RequestBuilder request;

        String menuId = "67117d84-4e15-4ac7-b704-caeed79652a9";// "402881e564c015100164c0154cbe0000";
        request = get(String.format("/api/menu/get/%s",menuId))
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
    public void queryMenuByPage() {
        RequestBuilder request;
        String menuId = "402881e564c015100164c0154cbe0000";
        MultiValueMap<String,String> mapValue = new LinkedMultiValueMap<>();
        mapValue.add("page","0");
        mapValue.add("size","3");
        mapValue.add("sort","code");
        request = get("/api/menu/query")
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