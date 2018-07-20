package com.zxsoft.fanfanfamily.mort.domain.custom;

import com.zxsoft.fanfanfamily.base.domain.Region;

import java.util.List;

public interface RegionDaoCustom {
    List<Region> customQuery();

    List<Region> customQueryCriteria();
}
