package com.ly.base.core.model.log;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志
 * @author LeiYong
 *
 */
public class LogOperater implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5390995626433234239L;

	private Integer pk;

    private String createBy;

    private Date createTime;

    private String type;

    private String model;

    private String func;

    private String content;

    private String method;

    private String title;

    private String from;

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func == null ? null : func.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from == null ? null : from.trim();
    }
}