package com.zxsoft.fanfanfamily.mort.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.SimpleEntity;
import com.zxsoft.fanfanfamily.common.SpringUtil;
import com.zxsoft.fanfanfamily.mort.repository.RegionDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.core.ReflectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class RegionWithChildDTO extends SimpleEntity {

    private String code;
    //地区logo
    private String logoUrl;
    private String parentRegion;
    private List<RegionWithChildDTO> subRegions = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getParentRegion() {
        return parentRegion;
    }

    public void setParentRegion(String parentRegion) {
        this.parentRegion = parentRegion;
    }

    @JSONField(name = "children")
    public List<RegionWithChildDTO> getSubRegions() {
        if (getId() != null) {
            RegionDao regionDao = (RegionDao) SpringUtil.getBean("regionDao");
            List<Region> lstSub = regionDao.customQueryAllByParentRegionId(getId());
            if (lstSub.size() > 0) {
                List<RegionWithChildDTO> lstRlt = new ArrayList<>();
                lstSub.forEach(item -> lstRlt.add(RegionWithChildDTO.convert(item)));
                return lstRlt;
            }
        }
        return null;
    }

    public void setSubRegions(List<RegionWithChildDTO> subRegions) {
        this.subRegions = subRegions;
    }

    /**
     * 将region转化成包含SubRegions的简易DTO。注意：parentRegion是懒加载无法直接获取到ID，所以做了特别处理。
     * @param region
     * @return
     */
    public static RegionWithChildDTO convert(Region region) {
        RegionWithChildDTO item = new RegionWithChildDTO();
        PropertyDescriptor arrSrcProSet[] = ReflectUtils.getBeanSetters(region.getClass());
        PropertyDescriptor arrDestProSet[] = ReflectUtils.getBeanSetters(item.getClass());

        try {
            for (int i = 0; i < arrSrcProSet.length; i++) {
                for (int j = 0; j < arrDestProSet.length; j++) {
                    if (arrSrcProSet[i].getName().equalsIgnoreCase(arrDestProSet[j].getName())) {
                        if (arrSrcProSet[i].getName().equalsIgnoreCase("parentRegion")) {
                            Region parentRegion = region.getParentRegion();
                            if (parentRegion != null  && !StringUtils.isEmpty(parentRegion.getId())) {
                                arrDestProSet[j].getWriteMethod().invoke(item, parentRegion.toString());
                            }
                        } else {
                            Object srcValue = arrSrcProSet[i].getReadMethod().invoke(region);
                            arrDestProSet[j].getWriteMethod().invoke(item,srcValue);
                        }
                    }
                }
            }
        } catch (InvocationTargetException ex) {
            item = null;
        }catch (IllegalAccessException ex) {
            item = null;
        }

        return item;
    }
}
