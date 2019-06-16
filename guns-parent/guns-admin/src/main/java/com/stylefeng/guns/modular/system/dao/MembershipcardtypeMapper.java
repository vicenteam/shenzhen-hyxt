package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.Membershipcardtype;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 会员等级配置表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-13
 */
public interface MembershipcardtypeMapper extends BaseMapper<Membershipcardtype> {
    public int updateInfoByLeaves(@Param("moRenkeqiandaonum")int moRenkeqiandaonum,@Param("leaves")int leave) ;
}
