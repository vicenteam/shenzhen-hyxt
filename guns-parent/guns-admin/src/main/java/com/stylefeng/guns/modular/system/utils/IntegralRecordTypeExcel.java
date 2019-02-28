package com.stylefeng.guns.modular.system.utils;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

@ExcelTarget("integralRecordTypeExcel")
public class IntegralRecordTypeExcel {


    @Excel(name = "门店名称",orderNum = "1")
    private String WarehouseName;
    @Excel(name = "门店编码",orderNum = "2")
    private String WarehouseCode;
    @Excel(name = "商品编码",orderNum = "3")
    private String InventoryCode;
    @Excel(name = "商品名称",orderNum = "4")
    private String InventoryName;
    @Excel(name = "规格",orderNum = "5")
    private String Specification;
    @Excel(name = "可用数量",orderNum = "6")
    private Double AvailableQuantity;
    @Excel(name = "计量单位",orderNum = "7")
    private String UnitName;
    @Excel(name = "商品类型",orderNum = "8")
    private Integer producttype;

    public String getWarehouseName() {
        return WarehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        WarehouseName = warehouseName;
    }

    public String getWarehouseCode() {
        return WarehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        WarehouseCode = warehouseCode;
    }

    public String getInventoryCode() {
        return InventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        InventoryCode = inventoryCode;
    }

    public String getInventoryName() {
        return InventoryName;
    }

    public void setInventoryName(String inventoryName) {
        InventoryName = inventoryName;
    }

    public String getSpecification() {
        return Specification;
    }

    public void setSpecification(String specification) {
        Specification = specification;
    }

    public Double getAvailableQuantity() {
        return AvailableQuantity;
    }

    public void setAvailableQuantity(Double availableQuantity) {
        AvailableQuantity = availableQuantity;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public Integer getProducttype() {
        return producttype;
    }

    public void setProducttype(Integer producttype) {
        this.producttype = producttype;
    }
}
