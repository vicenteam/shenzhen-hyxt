package com.stylefeng.guns.modular.system.utils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

@ExcelTarget("XiaoliangpaimingExcel")
public class XiaoliangpaimingExcel {

    @Excel(name = "商品名称",orderNum = "1")
    private String productname;
    @Excel(name = "商品库存",orderNum = "2")
    private String  productnum;
    @Excel(name = "总销售量",orderNum = "3")
    private String count;
    @Excel(name = "月销售量",orderNum = "4")
    private String month;
    @Excel(name = "时间段销售量",orderNum = "5")
    private String time_to_end;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTime_to_end() {
        return time_to_end;
    }

    public void setTime_to_end(String time_to_end) {
        this.time_to_end = time_to_end;
    }
}
