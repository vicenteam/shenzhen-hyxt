package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 指定产品为肽、松花粉、钙、羊奶等，这一类的商品会分为一个类目，只有购买了这一类的商品才可以增加签到获取积分的次数。
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-12
 */
@TableName("main_member_xiaofeigetjien")
public class MemberXiaofeigetjien extends Model<MemberXiaofeigetjien> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer memberid;
    private Integer deptid;
    private Integer productid;
    private Integer playproductnum;
    private Double playproductmoney;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public Integer getPlayproductnum() {
        return playproductnum;
    }

    public void setPlayproductnum(Integer playproductnum) {
        this.playproductnum = playproductnum;
    }

    public Double getPlayproductmoney() {
        return playproductmoney;
    }

    public void setPlayproductmoney(Double playproductmoney) {
        this.playproductmoney = playproductmoney;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MemberXiaofeigetjien{" +
        "id=" + id +
        ", memberid=" + memberid +
        ", deptid=" + deptid +
        ", productid=" + productid +
        ", playproductnum=" + playproductnum +
        ", playproductmoney=" + playproductmoney +
        "}";
    }
}
