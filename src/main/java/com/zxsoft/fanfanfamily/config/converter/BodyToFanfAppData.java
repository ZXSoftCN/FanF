package com.zxsoft.fanfanfamily.config.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.http.ResponseEntity;

public class BodyToFanfAppData {

    public static FanfAppData convert( Object obj) {
        //修改配置返回内容的过滤
        SerializerFeature[] serializerFeatures = {
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.PrettyFormat};

        String strObj = JSON.toJSONStringWithDateFormat(obj,"yyyy-MM-dd HH:mm:ss",serializerFeatures);
        try {
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

                    innerData.setCurrentPage(currentPage);
                    innerData.setPageSize(totalPages);
                    innerData.setTotalCount(totalCount);
                    innerData.setContents(lst);
                    return FanFResponseBodyBuilder.ok("ok", innerData);
                }
            } else if (obj.getClass() == ResponseEntity.class) {
                ResponseEntity innerT = (ResponseEntity) obj;
                Object innerObj = innerT.getBody();
                return FanFResponseBodyBuilder.ok("ok", innerObj);
            } else {
                return FanFResponseBodyBuilder.ok("ok", obj);
            }
        } catch (Exception ex) {
            return FanFResponseBodyBuilder.ok("ok", obj);
        }

        return null;
    }
}
