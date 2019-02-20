package com.stylefeng.guns.modular.main.service.impl;

import com.stylefeng.guns.modular.system.model.VerificationCode;
import com.stylefeng.guns.modular.system.dao.VerificationCodeMapper;
import com.stylefeng.guns.modular.main.service.IVerificationCodeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 验证码 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-02-20
 */
@Service
public class VerificationCodeServiceImpl extends ServiceImpl<VerificationCodeMapper, VerificationCode> implements IVerificationCodeService {

}
