package com.stylefeng.guns.modular.system.utils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

@ExcelTarget("JifenduihuanExcel")
public class JifenduihuanExcel {

    @Excel(name = "门店", orderNum = "1")
    private String fullname;
    @Excel(name = "商品名称", orderNum = "2")
    private String productname;
    @Excel(name = "商品库存", orderNum = "3")
    private String productnum;
    @Excel(name = "商品兑换售量", orderNum = "4")
    private String num;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductnum() {
        return productnum;
    }

    public void setProductnum(String productnum) {
        this.productnum = productnum;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
