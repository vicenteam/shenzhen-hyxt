package com.stylefeng.guns.modular.main.service.impl;

import com.stylefeng.guns.modular.system.model.Membermanagement;
import com.stylefeng.guns.modular.system.model.QiandaoCheckin;
import com.stylefeng.guns.modular.system.dao.QiandaoCheckinMapper;
import com.stylefeng.guns.modular.main.service.IQiandaoCheckinService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 复签记录表 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-14
 */
@Service
public class QiandaoCheckinServiceImpl extends ServiceImpl<QiandaoCheckinMapper, QiandaoCheckin> implements IQiandaoCheckinService {

    @Override
    public String selectNewCreateTime(Integer memberId, String beginTime, String endTime) {
        return this.baseMapper.selectNewCreateTime(memberId,beginTime,endTime);
    }

    @Override
    public String selectNewUpdateTime(Integer memberId, String beginTime, String endTime) {
        return this.baseMapper.selectNewUpdateTime(memberId,beginTime,endTime);
    }

    @Override
    public int list2Count(Integer dayse, Integer deptId, String beginTime, String endTime) {
        return this.baseMapper.list2Count(dayse,deptId,beginTime,endTime);
    }

    @Override
    public List<Map<String,Object>> list2(Integer dayse, Integer deptId, String beginTime, String endTime, int pageNum, int pageSize) {
        return this.baseMapper.list2(dayse,deptId,beginTime,endTime,pageNum,pageSize);
    }
}
