package com.zxsoft.fanfanfamily.base.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sys_tracelog")
public class TraceLog implements Serializable {
	private static final long serialVersionUID = -4589566683846441760L;

	private String id;
	private String username;
	private String operation;
	private Long timeConsume;
	private String method;
	private String params;
	private String ip;
	private Date createTime;
	private String location;

	private String timeField;// 用于搜索条件中的时间字段

	public TraceLog() {
	}

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

	@Column(name = "userName")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name = "operation",length = 100)
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	@Column(name = "timeConsume")
	public Long getTimeConsume() {
		return timeConsume;
	}

	public void setTimeConsume(Long timeConsume) {
		this.timeConsume = timeConsume;
	}
	@Column(name = "method")
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	@Column(name = "params",length = 1000)
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	@Column(name = "ip",length = 50)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "location")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	@Transient
	public String getTimeField() {
		return timeField;
	}

	public void setTimeField(String timeField) {
		this.timeField = timeField;
	}
}