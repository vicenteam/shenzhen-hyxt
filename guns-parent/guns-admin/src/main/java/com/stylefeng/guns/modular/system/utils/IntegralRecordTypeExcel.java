package com.stylefeng.guns.modular.system.utils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

@ExcelTarget("integralRecordTypeExcel")
public class IntegralRecordTypeExcel {

    @Excel(name = "商品编码",orderNum = "1")
    private String inventoryCode;
    @Excel(name = "商品名称",orderNum = "2")
    private String inventoryName;
    @Excel(name = "亲民价",orderNum = "3")
    private String productpice;
    @Excel(name = "零售价",orderNum = "4")
    private String retailPrice;
    @Excel(name = "规格",orderNum = "5")
    private String specification;
    @Excel(name = "可用数量",orderNum = "6")
    private String availableQuantity;
    @Excel(name = "计量单位",orderNum = "7")
    private String unitName;
    @Excel(name = "商品类型",orderNum = "8")
    private String producttype;
    @Excel(name = "仓库名称",orderNum = "9")
    private String warehouseName;
    @Excel(name = "仓库编码",orderNum = "10")
    private String warehouseCode;

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getProductpice() {
        return productpice;
    }

    public void setProductpice(String productpice) {
        this.productpice = productpice;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }
}
