package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 消费小票信息
 * </p>
 *
 * @author stylefeng
 * @since 2019-02-21
 */
@TableName("main_receipts_info")
public class ReceiptsInfo extends Model<ReceiptsInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 创建时间
     */
    private String createTime;
    private Integer deptId;
    /**
     * 图片
     */
    private String receiptsBase64Img;
    private Integer memberId;
    /**
     * 购买人电话
     */
    private String memberPhone;
    /**
     * 购买人姓名
     */
    private String memberName;
    /**
     * 支付金额
     */
    private String playMoney;
    private Integer status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getReceiptsBase64Img() {
        return receiptsBase64Img;
    }

    public void setReceiptsBase64Img(String receiptsBase64Img) {
        this.receiptsBase64Img = receiptsBase64Img;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPlayMoney() {
        return playMoney;
    }

    public void setPlayMoney(String playMoney) {
        this.playMoney = playMoney;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ReceiptsInfo{" +
        "id=" + id +
        ", createTime=" + createTime +
        ", deptId=" + deptId +
        ", memberId=" + memberId +
        ", memberPhone=" + memberPhone +
        ", memberName=" + memberName +
        ", playMoney=" + playMoney +
        ", status=" + status +
        "}";
    }
}
