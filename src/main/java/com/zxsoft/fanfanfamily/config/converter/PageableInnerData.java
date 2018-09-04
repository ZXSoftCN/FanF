package com.zxsoft.fanfanfamily.config.converter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName(value = "data")
public class PageableInnerData {
    private Long totalCount;//总个数
    private Long currentPage;//当前页号
    private Integer pageSize;//每页个数
    private Long totalPage;//当前总共有多少页
    private JSONArray contents = new JSONArray();

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    @JSONField(name = "list")
    public JSONArray getContents() {
        return contents;
    }

    public void setContents(JSONArray contents) {
        this.contents = contents;
    }
}
