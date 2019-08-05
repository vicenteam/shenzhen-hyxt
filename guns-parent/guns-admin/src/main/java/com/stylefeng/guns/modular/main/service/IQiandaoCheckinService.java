package com.stylefeng.guns.modular.main.service;

import com.stylefeng.guns.modular.system.model.QiandaoCheckin;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 复签记录表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-14
 */
public interface IQiandaoCheckinService extends IService<QiandaoCheckin> {

    /**
     * 查询最新签到时间
     * @param memberId
     * @return
     */
    String selectNewCreateTime(Integer memberId, String beginTime, String endTime);

    /**
     * 查询最新复签时间
     * @param memberId
     * @return
     */
    String selectNewUpdateTime(Integer memberId, String beginTime, String endTime);

    int list2Count(@Param("dayse")Integer dayse,@Param("deptId")Integer deptId, @Param("beginTime")String beginTime, @Param("endTime")String endTime);
    List<Map<String,Object>> list2(Integer dayse, Integer deptId, String beginTime, String endTime, int pageNum, int pageSize);
}
