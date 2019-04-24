package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.Integralrecordtype;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 积分类型 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-12-03
 */
public interface IntegralrecordtypeMapper extends BaseMapper<Integralrecordtype> {
    void updateAllIntegralrecordtype(@Param("id") String id);
}
