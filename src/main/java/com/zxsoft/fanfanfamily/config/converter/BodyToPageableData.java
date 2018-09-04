package com.zxsoft.fanfanfamily.config.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

public class BodyToPageableData {

    public static FanFResponseEntity convert(Object obj) {
        //修改配置返回内容的过滤
        SerializerFeature[] serializerFeatures = {
//                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
//                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.DisableCircularReferenceDetect
                };//SerializerFeature.PrettyFormat

        String strObj = JSON.toJSONStringWithDateFormat(obj,"yyyy-MM-dd HH:mm:ss",serializerFeatures);
        JSONObject jsonObj = JSON.parseObject(strObj);
        if (jsonObj.containsKey("pageable")) {
            JSONObject jsonPage = jsonObj.getJSONObject("pageable");
            if (jsonPage.containsKey("paged") && jsonPage.getBoolean("paged")) {
                //分页响应转化
                FanFResponseEntity data = new FanFResponseEntity();
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
                data.setStatus(status);
                data.setT(innerData);

                return data;
            }
        }

        return null;
    }
}
