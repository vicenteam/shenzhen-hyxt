package com.stylefeng.guns.modular.main.service;

import com.stylefeng.guns.modular.system.model.MemberXiaofeigetjien;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 指定产品为肽、松花粉、钙、羊奶等，这一类的商品会分为一个类目，只有购买了这一类的商品才可以增加签到获取积分的次数。 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-12
 */
public interface IMemberXiaofeigetjienService extends IService<MemberXiaofeigetjien> {
    int sumMoneyByMemberId(Integer memberId);
}
