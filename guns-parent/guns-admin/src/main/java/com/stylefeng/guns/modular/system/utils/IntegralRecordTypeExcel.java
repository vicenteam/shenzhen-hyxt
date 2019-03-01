package com.stylefeng.guns.modular.system.utils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

@ExcelTarget("integralRecordTypeExcel")
public class IntegralRecordTypeExcel {


//    @Excel(name = "门店名称",orderNum = "1")
//    private String WarehouseName;
//    @Excel(name = "门店编码",orderNum = "2")
//    private String WarehouseCode;
    @Excel(name = "商品编码",orderNum = "1")
    private String InventoryCode;
    @Excel(name = "亲民价",orderNum = "2")
    private Double productpice;
    @Excel(name = "零售价",orderNum = "3")
    private Double retailPrice;
//    @Excel(name = "商品名称",orderNum = "4")
//    private String InventoryName;
//    @Excel(name = "规格",orderNum = "5")
//    private String Specification;
//    @Excel(name = "可用数量",orderNum = "6")
//    private Double AvailableQuantity;
//    @Excel(name = "计量单位",orderNum = "7")
//    private String UnitName;
//    @Excel(name = "商品类型",orderNum = "8")
//    private Integer producttype;

    public String getInventoryCode() {
        return InventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        InventoryCode = inventoryCode;
    }

    public Double getProductpice() {
        return productpice;
    }

    public void setProductpice(Double productpice) {
        this.productpice = productpice;
    }

    public Double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Double retailPrice) {
        this.retailPrice = retailPrice;
    }
}
