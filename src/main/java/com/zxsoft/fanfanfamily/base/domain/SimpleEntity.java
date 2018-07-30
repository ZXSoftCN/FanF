package com.zxsoft.fanfanfamily.base.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class SimpleEntity extends BaseEntity {
private static final long serialVersionUID = -3595068446762873289L;

    private String name;
    private String description;

    @Column(name = "name", unique = true, nullable = false, columnDefinition = "varchar(100)")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", columnDefinition = "varchar(1000)")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
