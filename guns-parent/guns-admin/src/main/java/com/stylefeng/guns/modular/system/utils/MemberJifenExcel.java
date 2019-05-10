package com.stylefeng.guns.modular.system.utils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

@ExcelTarget("memberJifenExcel")
public class MemberJifenExcel {

    @Excel(name = "会员名称",orderNum = "1")
    private String memberName;
    @Excel(name = "身份证号",orderNum = "2")
    private String  membercadid;
    @Excel(name = "联系电话",orderNum = "3")
    private String memberPhone;
    @Excel(name = "花费金额",orderNum = "4")
    private String payMoney;
    @Excel(name = "积分值",orderNum = "9")
    private String integral;
    @Excel(name = "积分明细",orderNum = "5")
    private String typeId;
    @Excel(name = "操作人",orderNum = "10")
    private String staffName;
    @Excel(name = "创建时间",orderNum = "8")
    private String createTime;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMembercadid() {
        return membercadid;
    }

    public void setMembercadid(String membercadid) {
        this.membercadid = membercadid;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
