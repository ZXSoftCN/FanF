package com.zxsoft.fanfanfamily.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


public class JPAUtil {

    public static Pageable getPageAble(java.util.Map<String,Integer> pageParams){

        if (!pageParams.isEmpty()) {
            int offset = pageParams.containsKey("offset")?pageParams.get("offset"):0;
            int limit = pageParams.containsKey("limit")?pageParams.get("limit"):10;

            Pageable pageItem = PageRequest.of(offset, limit);
            return pageItem;
        }
        return null;
    }
}
