package com.stylefeng.guns.modular.main.service;

import com.stylefeng.guns.modular.system.model.Integralrecord;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分记录 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-14
 */
public interface IIntegralrecordService extends IService<Integralrecord> {

    List<Map<String, Object>> productSalesRanking(Integer pagetNum,
                                                  Integer pageSize,
                                                  String deptId,
                                                  String monthTime1,
                                                  String monthTime2,
                                                  String periodTime1,
                                                  String periodTime2,
                                                  String orderBy,
                                                  String desc);

    List<Map<String, Object>>  productSalesRankingintCount(Integer pagetNum,
                                    Integer pageSize,
                                    String deptId,
                                    String monthTime1,
                                    String monthTime2,
                                    String periodTime1,
                                    String periodTime2,
                                    String orderBy,
                                    String desc);
    int duihuanTableDataCount(Integer pagetNum,
                              Integer pageSize,
                              String deptId,
                              String periodTime1,
                              String periodTime2,
                              String orderBy,
                              String status,
                              String desc);
    List<Map<String,Object>> duihuanTableData(Integer pagetNum,
                                              Integer pageSize,
                                              String deptId,
                                              String periodTime1,
                                              String periodTime2,
                                              String orderBy,
                                              String status,
                                              String desc);

}
