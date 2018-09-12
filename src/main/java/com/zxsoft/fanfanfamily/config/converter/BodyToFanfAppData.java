package com.zxsoft.fanfanfamily.config.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public class BodyToFanfAppData {

    public static FanfAppData convert( Object obj) {
        JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        //修改配置返回内容的过滤
        SerializerFeature[] serializerFeatures = {
//                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteEnumUsingToString,
//                SerializerFeature.WriteNullListAsEmpty,//list为null时改为[]，而非null。沿用null，方便前端展现。
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteDateUseDateFormat,
                };//SerializerFeature.PrettyFormat

        try {NoLazyPropertyFilter filter = new NoLazyPropertyFilter();
            String strObj = JSON.toJSONString(obj,filter,serializerFeatures);
            String msg = "执行成功！";


            if (List.class.isAssignableFrom(obj.getClass())) {
//                JSONArray jsonArray = JSON.parseArray(strObj);
                return FanFResponseBodyBuilder.ok(msg, obj);
            }
            Object simpleObj = JSON.parse(strObj);
            if (Boolean.class.isAssignableFrom(simpleObj.getClass()) || Number.class.isAssignableFrom(simpleObj.getClass())
                    || String.class.isAssignableFrom(simpleObj.getClass()) || Date.class.isAssignableFrom(simpleObj.getClass())) {
                return FanFResponseBodyBuilder.ok(msg, simpleObj);
            }
            JSONObject jsonObj = JSON.parseObject(strObj);
            if (jsonObj.containsKey("pageable")) {
                JSONObject jsonPage = jsonObj.getJSONObject("pageable");
                if (jsonPage.containsKey("paged") && jsonPage.getBoolean("paged")) {
                    //分页响应转化
//                FanfAppData data = new FanfAppData();
                    PageableInnerData innerData = new PageableInnerData();
                    Long currentPage = jsonObj.getLong("number");
                    Long numberOfCurrentPage = jsonPage.getLong("numberOfElements");
                    Long totalCount = jsonObj.getLong("totalElements");
                    Long totalPages = jsonObj.getLong("totalPages");
                    Integer pageSize = jsonObj.getInteger("size");//一页可包含个数
                    JSONArray lst = jsonObj.getJSONArray("content");
                    Integer status = 1;

                    innerData.setCurrentPage(currentPage+1);
                    innerData.setPageSize(pageSize);
                    innerData.setTotalCount(totalCount);
                    innerData.setTotalPage(totalPages);
                    innerData.setContents(lst);
                    return FanFResponseBodyBuilder.ok(msg, innerData);
                }
            } else if (obj.getClass() == ResponseEntity.class) {
                ResponseEntity innerT = (ResponseEntity) obj;
                Object innerObj = innerT.getBody();
                return FanFResponseBodyBuilder.ok(msg, innerObj);
            } else {
                return FanFResponseBodyBuilder.ok(msg, obj);
            }
        } catch (Exception ex) {
            return FanFResponseBodyBuilder.ok("convert exception", obj);
        }

        return null;
    }
}
