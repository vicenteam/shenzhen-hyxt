package com.stylefeng.guns.modular.system.utils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

@ExcelTarget("KucunguanliExcel")
public class KucunguanliExcel {

    @Excel(name = "商品名称",orderNum = "1")
    private String productname;
    @Excel(name = "商品类别",replace = { "礼品类_0", "积分兑换类_1","销售类_3","积分+金额类_4" },orderNum = "2")
    private String  producttype;
    @Excel(name = "记录状态",replace = { "添加库存_0", "减少库存_1", },orderNum = "3")
    private String status;
    @Excel(name = "变化数量",orderNum = "4")
    private String consumptionNum;
    @Excel(name = "记录时间",orderNum = "9")
    private String createtime;
    @Excel(name = "操作人",orderNum = "5")
    private String createuserid;

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConsumptionNum() {
        return consumptionNum;
    }

    public void setConsumptionNum(String consumptionNum) {
        this.consumptionNum = consumptionNum;
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
