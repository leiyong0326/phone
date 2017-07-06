package com.ly.base.core.model.sys;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 会员等级表
 * @author LeiYong
 *
 */
public class SysAccount implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1696692168429769128L;

	private Integer pk;

    private String name;

    private String dsc;

    private BigDecimal ratio;

    private Integer score;

    private BigDecimal ratioUp;

    private Integer valid;//'0所有层级,1上级 分成,2上一级分成'

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc == null ? null : dsc.trim();
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public BigDecimal getRatioUp() {
        return ratioUp;
    }

    public void setRatioUp(BigDecimal ratioUp) {
        this.ratioUp = ratioUp;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }
}