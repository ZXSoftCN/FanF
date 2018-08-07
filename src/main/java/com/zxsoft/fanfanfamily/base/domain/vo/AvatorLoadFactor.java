package com.zxsoft.fanfanfamily.base.domain.vo;

/*
    加载头像、图片等(如png类)的属性项
 */
public class AvatorLoadFactor {
    private Integer width;//宽度
    private Integer height;//高度
    private Double scaling;//像素倍数

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Double getScaling() {
        return scaling;
    }

    public void setScaling(Double scaling) {
        this.scaling = scaling;
    }

    public AvatorLoadFactor() {
        scaling = 1D;
    }
}
