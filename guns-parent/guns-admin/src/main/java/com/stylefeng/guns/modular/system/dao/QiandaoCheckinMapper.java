package com.stylefeng.guns.modular.system.dao;

import com.stylefeng.guns.modular.system.model.Membermanagement;
import com.stylefeng.guns.modular.system.model.QiandaoCheckin;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 复签记录表 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-14
 */
public interface QiandaoCheckinMapper extends BaseMapper<QiandaoCheckin> {

    /**
     * 查询最新签到时间
     * @param memberId
     * @return
     */
    String selectNewCreateTime(@Param("memberId")Integer memberId, @Param("beginTime")String beginTime, @Param("endTime")String endTime);

    /**
     * 查询最新复签时间
     * @param memberId
     * @return
     */
    String selectNewUpdateTime(@Param("memberId")Integer memberId, @Param("beginTime")String beginTime, @Param("endTime")String endTime);

    List<Map<String,Object>> list2(@Param("dayse")Integer dayse, @Param("deptId")Integer deptId, @Param("beginTime")String beginTime, @Param("endTime")String endTime, @Param("pageNum")int pageNum, @Param("pageSize")int pageSize);
    int list2Count(@Param("dayse")Integer dayse,@Param("deptId")Integer deptId, @Param("beginTime")String beginTime, @Param("endTime")String endTime);
}
