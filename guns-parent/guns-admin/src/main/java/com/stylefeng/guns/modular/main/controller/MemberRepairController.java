package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.*;
import com.stylefeng.guns.modular.system.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@Scope("prototype")
@RequestMapping("/memberRepair")
public class MemberRepairController extends BaseController {
    private String PREFIX = "/main/membermanagement/";

    @Autowired
    private ICheckinService checkinService;
    @Autowired
    private IQiandaoCheckinService qiandaoCheckinService;
    @Autowired
    private IMembershipcardtypeService membershipcardtypeService;
    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private IIntegralrecordService integralrecordService;

    @RequestMapping("")
    public String index(){
        return PREFIX + "memberRepair.html";
    }

    /**
     *  会员补签
     * @param memberId
     * @param time
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/repair")
    @ResponseBody
    public Object repair(String memberId,String time) throws Exception {
        String[] val1 = time.split(" ");
        String[] val2 = val1[0].split("-");
        BaseEntityWrapper<QiandaoCheckin> qWrapper = new BaseEntityWrapper<>();
        qWrapper.eq("memberid",memberId);
        qWrapper.like("createtime",val1[0]);
        QiandaoCheckin qian = qiandaoCheckinService.selectOne(qWrapper);
        if(qian != null){
            if(StringUtils.isEmpty(qian.getCreatetime())){ //如果未进行首签
                qian.setCreatetime(time);
                qian.setUpdatetime(time);
            }else if(StringUtils.isEmpty(qian.getUpdatetime())){ //已首签未复签
                qian.setUpdatetime(qian.getCreatetime()); //如果已经首签 补签复签的时间就为首签时间
            }
            qiandaoCheckinService.updateById(qian); //进行补签
        }else {
            StringBuffer defaultVal = new StringBuffer();
            for (int i=0; i<val2.length; i++){
                defaultVal.append(val2[i]);
                if(i == val2.length-1){
                    defaultVal.append("01"); //拼接日期字符串 格式为yyyyMMdd01 默认某天第一场次
                }
            }
            BaseEntityWrapper<Checkin> cWrapper = new BaseEntityWrapper<>();
            cWrapper.eq("screenings",Integer.parseInt(defaultVal.toString()));
            Checkin checkin = checkinService.selectOne(cWrapper); //
            if(checkin != null){ //如果有当前场次的记录
                QiandaoCheckin qiandaoCheckin = new QiandaoCheckin();
                qiandaoCheckin.setCreatetime(time);
                qiandaoCheckin.setUpdatetime(time);
                qiandaoCheckin.setStatus(0);
                qiandaoCheckin.setCheckinid(checkin.getId());
                qiandaoCheckin.setDeptid(ShiroKit.getUser().getDeptId());
                qiandaoCheckin.setMemberid(Integer.parseInt(memberId));
                qiandaoCheckinService.insert(qiandaoCheckin);
            }else {
                throw new GunsException(BizExceptionEnum.SERVER_ERROR1);
            }
        }

        Membermanagement membermanagement = membermanagementService.selectById(memberId);
        if(membermanagement != null){
            Integer checkInNum = membermanagement.getCheckInNum();
            if(checkInNum!=null&&checkInNum>0){
                Membershipcardtype membershipcardtype = membershipcardtypeService.selectById(membermanagement.getLevelID());
                Integralrecord integralrecord = new Integralrecord();
                integralrecord.setIntegral(membershipcardtype.getSignin());
                integralrecord.setOtherTypeId("0");
                integralrecord.setIntegralType("2");
                integralrecord.setMemberid(membermanagement.getId());
                integralrecord.setCreateTime(DateUtil.getTime());
                integralrecord.setDeptid(ShiroKit.getUser().getDeptId());
                integralrecord.setStaffid(ShiroKit.getUser().getId());
                integralrecordService.insert(integralrecord);

                membermanagement.setIntegral(membermanagement.getIntegral() + membershipcardtype.getSignin());
                membermanagement.setCountPrice(membermanagement.getCountPrice() + membershipcardtype.getSignin());
                membermanagementService.updateById(membermanagement);

                membermanagement = membermanagementService.selectById(memberId);
                membermanagement.setCheckInNum(membermanagement.getCheckInNum()-1);
            }
            membermanagement.setCheckINTime1(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            membermanagement.setIsvisit(0);
            membermanagementService.updateById(membermanagement);
        }

        return SUCCESS_TIP;
    }
}
