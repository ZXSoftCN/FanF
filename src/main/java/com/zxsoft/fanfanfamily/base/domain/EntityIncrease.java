package com.zxsoft.fanfanfamily.base.domain;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sys_EntityIncrease")
public class EntityIncrease implements Serializable {

    private static final long serialVersionUID = 2131270446310574318L;

    private String id; // 主键
    private String name;//名称
    private String entityName;//实体key名称
    private String prefix;//编码前缀
    private String dateFormat;//日期获取样式
    private int codeNumLength;//编码流水号长度
    private int codeNumMax;//当前编码流水号最大值
    private int sortNoMax;//实体排序号最大值
    private String separate;//分隔符

    @Id
    @GenericGenerator(name = "generator", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "generator")
    @Column(name = "id",nullable = false,length = 36)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(unique = true,nullable = false)
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @Column
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Column(columnDefinition = "varchar(20) DEFAULT 'yyMMdd'")
    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Column(columnDefinition = "int unsigned DEFAULT 7")
    public int getCodeNumLength() {
        return codeNumLength;
    }

    public void setCodeNumLength(int codeNumLength) {
        this.codeNumLength = codeNumLength;
    }

    @Column(columnDefinition = "int unsigned DEFAULT 0")
    public int getCodeNumMax() {
        return codeNumMax;
    }

    public void setCodeNumMax(int codeNumMax) {
        this.codeNumMax = codeNumMax;
    }

    @Column(columnDefinition = "int unsigned DEFAULT 0")
    public int getSortNoMax() {
        return sortNoMax;
    }

    public void setSortNoMax(int sortNoMax) {
        this.sortNoMax = sortNoMax;
    }

    @Column
    public String getSeparate() {
        return separate;
    }

    public void setSeparate(String separate) {
        this.separate = separate;
    }

    /*
        初始化时为“-”，后续可修改去除。
     */
    @PrePersist
    public void onSetDefault() {
        if (StringUtils.isBlank(this.dateFormat)) {
            this.setDateFormat("yyMMdd");
        }
        if (codeNumLength == 0) {
            this.setCodeNumLength(7);
        }
        if (StringUtils.isBlank(separate)) {
            separate = "-";
        }
    }


}
