package com.stylefeng.guns.modular.system.utils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

@ExcelTarget("DingdanExcel")
public class DingdanExcel {

    @Excel(name = "购买者",orderNum = "1")
    private String memberName;
    @Excel(name = "购买商品名称",orderNum = "2")
    private String  productname;
    @Excel(name = "购买数量",orderNum = "3")
    private String consumptionNum;
    @Excel(name = "金额",orderNum = "4")
    private String jine;
    @Excel(name = "付款方式",orderNum = "9")
    private String payType;
    @Excel(name = "记录时间",orderNum = "5")
    private String createtime;
    @Excel(name = "操作人",orderNum = "10")
    private String createuserid;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getConsumptionNum() {
        return consumptionNum;
    }

    public void setConsumptionNum(String consumptionNum) {
        this.consumptionNum = consumptionNum;
    }

    public String getJine() {
        return jine;
    }

    public void setJine(String jine) {
        this.jine = jine;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
    }
}
