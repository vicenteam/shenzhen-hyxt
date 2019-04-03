package com.stylefeng.guns.modular.main.service;

import com.stylefeng.guns.modular.system.model.MemberCard;
import com.baomidou.mybatisplus.service.IService;
import org.springframework.context.annotation.Scope;

/**
 * <p>
 * 会员卡关联关系表 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-08-10
 */
@Scope("prototype")
public interface IMemberCardService extends IService<MemberCard> {

}
