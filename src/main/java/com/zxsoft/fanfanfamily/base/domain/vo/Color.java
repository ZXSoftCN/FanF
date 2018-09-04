package com.zxsoft.fanfanfamily.base.domain.vo;

public enum Color {
    green("#64ea91"),
    blue("#8fc9fb"),
    purple("#d897eb"),
    red("#f69899"),
    yellow("#f8c82e"),
    peach("#f797d6"),
    borderBase("#e5e5e5"),
    borderSplit("#f4f4f4"),
    grass("#d6fbb5"),
    sky("#c1e0fc");

    private  String color;
    private Color(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return this.color.toString();
    }
}
