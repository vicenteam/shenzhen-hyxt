package com.stylefeng.guns.modular.main.service.impl;

import com.stylefeng.guns.modular.system.model.MemberXiaofeigetjien;
import com.stylefeng.guns.modular.system.dao.MemberXiaofeigetjienMapper;
import com.stylefeng.guns.modular.main.service.IMemberXiaofeigetjienService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 指定产品为肽、松花粉、钙、羊奶等，这一类的商品会分为一个类目，只有购买了这一类的商品才可以增加签到获取积分的次数。 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-03-12
 */
@Service
public class MemberXiaofeigetjienServiceImpl extends ServiceImpl<MemberXiaofeigetjienMapper, MemberXiaofeigetjien> implements IMemberXiaofeigetjienService {
    @Override
    public int sumMoneyByMemberId(Integer memberId) {
        return this.baseMapper.sumMoneyByMemberId(memberId);
    }
}
