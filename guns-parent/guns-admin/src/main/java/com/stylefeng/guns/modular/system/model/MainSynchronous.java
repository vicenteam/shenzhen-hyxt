package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 同步T+json数据
 * </p>
 *
 * @author stylefeng
 * @since 2019-02-13
 */
@TableName("main_synchronous")
public class MainSynchronous extends Model<MainSynchronous> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String synchronousJson;
    /**
     * 0未同步 1成功 2失败
     */
    private Integer status;
    private String errorMssage;
    private String synchronousurl;
    private String createdt;
    private Integer memberid;

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public String getSynchronousurl() {
        return synchronousurl;
    }

    public void setSynchronousurl(String synchronousurl) {
        this.synchronousurl = synchronousurl;
    }

    public String getCreatedt() {
        return createdt;
    }

    public void setCreatedt(String createdt) {
        this.createdt = createdt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSynchronousJson() {
        return synchronousJson;
    }

    public void setSynchronousJson(String synchronousJson) {
        this.synchronousJson = synchronousJson;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMssage() {
        return errorMssage;
    }

    public void setErrorMssage(String errorMssage) {
        this.errorMssage = errorMssage;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MainSynchronous{" +
        "id=" + id +
        ", synchronousJson=" + synchronousJson +
        ", status=" + status +
        ", errorMssage=" + errorMssage +
        "}";
    }
}
