package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 充值记录表
 * </p>
 *
 * @author stylefeng
 * @since 2019-01-28
 */
@TableName("main_prepaid_record")
public class PrepaidRecord extends Model<PrepaidRecord> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 预充值时间
     */
    private String prepaidRecordTime;
    /**
     * 预充值金额
     */
    private Double prepaidRecordMoney;
    /**
     * memberId
     */
    private Integer prepaidRecordMemberId;
    private Integer deptid;
    /**
     * member姓名
     */
    private String prepaidRecordMemberName;
    /**
     * 预充值者电话号码
     */
    private String prepaidRecordMemberPhone;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrepaidRecordTime() {
        return prepaidRecordTime;
    }

    public void setPrepaidRecordTime(String prepaidRecordTime) {
        this.prepaidRecordTime = prepaidRecordTime;
    }

    public Double getPrepaidRecordMoney() {
        return prepaidRecordMoney;
    }

    public void setPrepaidRecordMoney(Double prepaidRecordMoney) {
        this.prepaidRecordMoney = prepaidRecordMoney;
    }

    public Integer getPrepaidRecordMemberId() {
        return prepaidRecordMemberId;
    }

    public void setPrepaidRecordMemberId(Integer prepaidRecordMemberId) {
        this.prepaidRecordMemberId = prepaidRecordMemberId;
    }

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public String getPrepaidRecordMemberName() {
        return prepaidRecordMemberName;
    }

    public void setPrepaidRecordMemberName(String prepaidRecordMemberName) {
        this.prepaidRecordMemberName = prepaidRecordMemberName;
    }

    public String getPrepaidRecordMemberPhone() {
        return prepaidRecordMemberPhone;
    }

    public void setPrepaidRecordMemberPhone(String prepaidRecordMemberPhone) {
        this.prepaidRecordMemberPhone = prepaidRecordMemberPhone;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PrepaidRecord{" +
        "id=" + id +
        ", prepaidRecordTime=" + prepaidRecordTime +
        ", prepaidRecordMoney=" + prepaidRecordMoney +
        ", prepaidRecordMemberId=" + prepaidRecordMemberId +
        ", deptid=" + deptid +
        ", prepaidRecordMemberName=" + prepaidRecordMemberName +
        ", prepaidRecordMemberPhone=" + prepaidRecordMemberPhone +
        "}";
    }
}
