package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.MemberXiaofeigetjien;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 指定产品为肽、松花粉、钙、羊奶等，这一类的商品会分为一个类目，只有购买了这一类的商品才可以增加签到获取积分的次数。 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-12
 */
public interface MemberXiaofeigetjienMapper extends BaseMapper<MemberXiaofeigetjien> {
    public int sumMoneyByMemberId(@Param("memberId")Integer memberId);
}
