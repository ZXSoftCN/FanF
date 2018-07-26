package com.zxsoft.fanfanfamily.base.domain;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sys_regionresource")
public class RegionResource implements Serializable {

    private static final long serialVersionUID = -8205974842573415757L;
    private String id; // 主键
    private String originFileName;//原文件名
    private String type;//类型
    private String resUrl;
    private Region region;

    @Id
    @GenericGenerator(name = "generator", strategy = "uuid")
    @GeneratedValue(generator = "generator")
    @Column(name = "id",nullable = false,length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "type",columnDefinition = "varchar(10) DEFAULT ''")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "resurl",columnDefinition = "varchar(200)",nullable = false)
    public String getResUrl() {
        return resUrl;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl;
    }

    @JSONField(serialize = false)
    @ManyToOne(cascade = {CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinColumn(name = "regionId",nullable = false,columnDefinition = "varchar(36)")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Transient
    @JSONField(serialize = true)
    public String getRegionId(){
        return region.getId();
    }

    @Column(name = "originFileName",columnDefinition = "varchar(100)")
    public String getOriginFileName() {
        return originFileName;
    }

    public void setOriginFileName(String originFileName) {
        this.originFileName = originFileName;
    }
}
