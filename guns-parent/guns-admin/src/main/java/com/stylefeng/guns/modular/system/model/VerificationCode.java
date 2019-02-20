package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 验证码
 * </p>
 *
 * @author stylefeng
 * @since 2019-02-20
 */
@TableName("main_verification_code")
public class VerificationCode extends Model<VerificationCode> {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String verificationcode;
    private Long createlongtime;
    private String phone;
    private Integer deptid;
    private String status;
    private Integer memberid;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVerificationcode() {
        return verificationcode;
    }

    public void setVerificationcode(String verificationcode) {
        this.verificationcode = verificationcode;
    }

    public Long getCreatelongtime() {
        return createlongtime;
    }

    public void setCreatelongtime(Long createlongtime) {
        this.createlongtime = createlongtime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "VerificationCode{" +
        "id=" + id +
        ", verificationcode=" + verificationcode +
        ", createlongtime=" + createlongtime +
        ", phone=" + phone +
        ", deptid=" + deptid +
        ", status=" + status +
        ", memberid=" + memberid +
        "}";
    }
}
