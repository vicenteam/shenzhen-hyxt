package com.stylefeng.guns.modular.system.utils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

@ExcelTarget("memberExcel")
public class MemberExcel {

    @Excel(name = "客户名称",orderNum = "1")
    private String mName;
    @Excel(name = "性别",replace = { "男_1", "女_2" },orderNum = "2")
    private Integer mSex;
    @Excel(name = "卡号",orderNum = "3")
    private String mCadID;
    @Excel(name = "联系电话",orderNum = "4")
    private String mPhone;
    @Excel(name = "联系地址",orderNum = "9")
    private String mAddress;
    @Excel(name = "可用积分",orderNum = "5")
    private double mIntegral;
    @Excel(name = "总积分",orderNum = "10")
    private double mCountPrice;
    @Excel(name = "是否老年协会会员",orderNum = "8")
    private Integer mIsoldsociety;
    @Excel(name = "卡片等级",orderNum = "6")
    private String mLevel;
    @Excel(name = "开卡时间",orderNum = "7")
    private String mCreateDt;
    @Excel(name = "门店名称",orderNum = "8")
    private String mDeptName;
    @Excel(name = "病史",orderNum = "9")
    private String bs;
    @Excel(name = "备注",orderNum = "10")
    private String remak;

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public String getRemak() {
        return remak;
    }

    public void setRemak(String remak) {
        this.remak = remak;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public Integer getmSex() {
        return mSex;
    }

    public void setmSex(Integer mSex) {
        this.mSex = mSex;
    }

    public String getmCadID() {
        return mCadID;
    }

    public void setmCadID(String mCadID) {
        this.mCadID = mCadID;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public double getmIntegral() {
        return mIntegral;
    }

    public void setmIntegral(double mIntegral) {
        this.mIntegral = mIntegral;
    }

    public double getmCountPrice() {
        return mCountPrice;
    }

    public void setmCountPrice(double mCountPrice) {
        this.mCountPrice = mCountPrice;
    }

    public Integer getmIsoldsociety() {
        return mIsoldsociety;
    }

    public void setmIsoldsociety(Integer mIsoldsociety) {
        this.mIsoldsociety = mIsoldsociety;
    }

    public String getmLevel() {
        return mLevel;
    }

    public void setmLevel(String mLevel) {
        this.mLevel = mLevel;
    }

    public String getmCreateDt() {
        return mCreateDt;
    }

    public void setmCreateDt(String mCreateDt) {
        this.mCreateDt = mCreateDt;
    }

    public String getmDeptName() {
        return mDeptName;
    }

    public void setmDeptName(String mDeptName) {
        this.mDeptName = mDeptName;
    }
}
