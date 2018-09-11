package com.zxsoft.fanfanfamily.base.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxsoft.fanfanfamily.base.controller.BaseRestControllerImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.Session;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity implements Serializable,EntityPersistExpand {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    //编译时记录版本ID，用于分辨修改前后对象序列化是否相同。
    //若有修改VersionUID，则提示：序列化版本不一致异常(InvalidCastException)
    private static final long serialVersionUID = -4960932774196310290L;

    private String id; // 主键
    private Date createTime;
    private Date lastUpdateTime;
    private String creator;
    private String lastUpdater;

    //region 实体持久化扩展处理，如设置默认值等。
    @Transient
    @Override
    public String getEntityClassName() {
        return this.getClass().getSimpleName();
//        return "BaseEntity";
    }

    @Override
    public void onSetDefault() {
        createTime = lastUpdateTime = DateTime.now().toDate();
        try {
            Subject currSubject = SecurityUtils.getSubject();
            if (currSubject != null) {
                org.apache.shiro.session.Session session = currSubject.getSession();
                if (session != null && session.getAttribute("userName") != null) {
                    setCreator(session.getAttribute("userName").toString());
                    setLastUpdater(session.getAttribute("userName").toString());
                }
            }
        } catch (Exception ex) {
            logger.error(String.format("创建实体%s【ID:%s】时未能获取当前用户，错误：%s",getEntityClassName(),getId(),ex.getMessage()));
        }
    }
    @Override
    public void onPostPersist(){}

    @Override
    public void onPreUpdate() {
        setLastUpdateTime(DateTime.now().toDate());
        try {
            Subject currSubject = SecurityUtils.getSubject();
            if (currSubject != null) {
                org.apache.shiro.session.Session session = currSubject.getSession();
                if (session != null && session.getAttribute("userName") != null) {
                    setLastUpdater(session.getAttribute("userName").toString());
                }
            }
        } catch (Exception ex) {
            logger.error(String.format("更新实体%s【ID:%s】时未能获取当前用户，错误：%s",getEntityClassName(),getId(),ex.getMessage()));
        }
    }

    @Override
    public void onPostUpdate() {

    }

    @PrePersist
    public void prePersist() {
        onSetDefault();
    }

    @PreUpdate
    public void preUpdate() {
        onPreUpdate();
    }

    @PostPersist
    public void postEntityValue() {
        onPostPersist();
    }
    //endregion

    @PostUpdate
    public void postUpdate() {
        onPostUpdate();
    }

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


//    @JSONField(serialize = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time",updatable = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

//    @JSONField(serialize = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastUpdate_Time")
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

//    @JSONField(serialize = false)
    @Column(name="creator",updatable = false)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

//    @JSONField(serialize = false)
    @Column(name="lastUpdater")
    public String getLastUpdater() {
        return lastUpdater;
    }

    public void setLastUpdater(String lastUpdater) {
        this.lastUpdater = lastUpdater;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
