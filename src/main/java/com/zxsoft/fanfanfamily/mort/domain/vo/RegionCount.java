package com.zxsoft.fanfanfamily.mort.domain.vo;

import com.zxsoft.fanfanfamily.base.domain.Region;

public interface RegionCount {
    Region getRegion();
    Integer getBankCount();

    //处理结果为null的方式
    default int getBankCountNotNull() {
        return getBankCount() == null ? 0 : getBankCount();
    }
}
