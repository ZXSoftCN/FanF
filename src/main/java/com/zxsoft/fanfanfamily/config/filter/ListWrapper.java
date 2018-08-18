package com.zxsoft.fanfanfamily.config.filter;


import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ResponseBody:响应时对List对象包装一层节点属性（list），如下：
 * {
 *     data : {
 *         list : [
 *              {...},
 *              {...}
 *         ]
 *     },
 *     msg: "0K",
 *     status: 1
 * }
 * @param <T>
 */
public class ListWrapper<T> implements Serializable {

    private List<T> nodeWrpper = new ArrayList<>();

    @JSONField(name = "list")
    public List<T> getNodeWrpper() {
        return nodeWrpper;
    }

    public void setNodeWrpper(List<T> nodeWrpper) {
        this.nodeWrpper = nodeWrpper;
    }

    public ListWrapper(List<T> nodeWrpper) {
        this.nodeWrpper = nodeWrpper;
    }
}
