package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商品追销管理
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-17
 */
@TableName("main_due_to_remind")
public class DueToRemind extends Model<DueToRemind> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 到期提醒天数
     */
    private Integer dueToRemindDays;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
    private Integer createUserId;
    private Integer updateUserId;
    /**
     * 提示信息
     */
    private String dueToRemindContext;
    /**
     * 状态(0可用 1停用 )
     */
    private Integer status;
    private Integer deptId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getDueToRemindDays() {
        return dueToRemindDays;
    }

    public void setDueToRemindDays(Integer dueToRemindDays) {
        this.dueToRemindDays = dueToRemindDays;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getDueToRemindContext() {
        return dueToRemindContext;
    }

    public void setDueToRemindContext(String dueToRemindContext) {
        this.dueToRemindContext = dueToRemindContext;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DueToRemind{" +
        "id=" + id +
        ", productId=" + productId +
        ", productName=" + productName +
        ", dueToRemindDays=" + dueToRemindDays +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", createUserId=" + createUserId +
        ", updateUserId=" + updateUserId +
        ", dueToRemindContext=" + dueToRemindContext +
        ", status=" + status +
        ", deptId=" + deptId +
        "}";
    }
}
