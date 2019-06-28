package com.stylefeng.guns.modular.main.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.*;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IDeptService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 复签记录控制器
 *
 * @author fengshuonan
 * @Date 2018-08-14 14:52:03
 */
@Controller
@Scope("prototype")
@RequestMapping("/qiandaoCheckin")
public class QiandaoCheckinController extends BaseController {

    private String PREFIX = "/main/qiandaoCheckin/";

    @Autowired
    private IQiandaoCheckinService qiandaoCheckinService;
    @Autowired
    private ICheckinService checkinService;
    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private IMembershipcardtypeService membershipcardtypeService;
    @Autowired
    private IntegralrecordController integralrecordController;
    @Autowired
    private MembermanagementController membermanagementController;
    @Autowired
    private IIntegralrecordService integralrecordService;
    @Autowired
    private IDeptService deptService;

    /**
     * 跳转到复签记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "qiandaoCheckin.html";
    }

    /**
     * 跳转到添加复签记录
     */
    @RequestMapping("/qiandaoCheckin_add")
    public String qiandaoCheckinAdd() {
        return PREFIX + "qiandaoCheckin_add.html";
    }

    /**
     * 跳转到修改复签记录
     */
    @RequestMapping("/qiandaoCheckin_update/{qiandaoCheckinId}")
    public String qiandaoCheckinUpdate(@PathVariable Integer qiandaoCheckinId, Model model) {
        QiandaoCheckin qiandaoCheckin = qiandaoCheckinService.selectById(qiandaoCheckinId);
        model.addAttribute("item", qiandaoCheckin);
        LogObjectHolder.me().set(qiandaoCheckin);
        return PREFIX + "qiandaoCheckin_edit.html";
    }

    /**
     * 获取复签记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<QiandaoCheckin> page = new PageFactory<QiandaoCheckin>().defaultPage();
        BaseEntityWrapper<QiandaoCheckin> baseEntityWrapper = new BaseEntityWrapper<>();
        Page<QiandaoCheckin> result = qiandaoCheckinService.selectPage(page, baseEntityWrapper);
        return super.packForBT(result);
    }

    /**
     * 新增复签记录
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public synchronized Object add(String memberId, String chechId) throws Exception {
        //判断签到场次是否被结束
        if (!StringUtils.isEmpty(chechId)) {
            Checkin checkin1 = checkinService.selectById(chechId);
            if (checkin1 != null) {
                if (checkin1.getStatus() == 2) {
                    throw new Exception("该场次已经结束无法进行该操作!");
                }
            }
        }
        //查询当天是否签到
        BaseEntityWrapper<QiandaoCheckin> qiandaoCheckinBaseEntityWrapper = new BaseEntityWrapper<>();
        qiandaoCheckinBaseEntityWrapper.eq("memberid", memberId);
        qiandaoCheckinBaseEntityWrapper.eq("deptid", ShiroKit.getUser().getDeptId());
        qiandaoCheckinBaseEntityWrapper.like("createtime", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
        int i = qiandaoCheckinService.selectCount(qiandaoCheckinBaseEntityWrapper);
        if (i != 0) {
            throw new Exception("该用户当天已首签无法进行该操作!");
        }
        QiandaoCheckin qiandaoCheckin = new QiandaoCheckin();
        qiandaoCheckin.setCheckinid(Integer.parseInt(chechId));
        qiandaoCheckin.setCreatetime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        qiandaoCheckin.setDeptid(ShiroKit.getUser().getDeptId());
        qiandaoCheckin.setMemberid(Integer.parseInt(memberId));
        qiandaoCheckinService.insert(qiandaoCheckin);

        //修改签到记录
        Membermanagement membermanagement = membermanagementService.selectById(memberId);
        if(membermanagement!=null){
            List<Membermanagement> membermanagements = new ArrayList<>(); //会员打卡获得积分
            Membershipcardtype membershipcardtype1 = membershipcardtypeService.selectById(membermanagement.getLevelID());
            membermanagements.add(membermanagement);
            Double integral  = membershipcardtype1.getSignin(); //签到积分
            Dept dept = deptService.selectById(53);
            Dept dept1 = deptService.selectById(ShiroKit.getUser().deptId);
            if(dept!=null&&dept.getCheckJifenSwitch()==1||dept1.getCheckJifenSwitch()==1){
                integral=0.0;
            }
//            if(! StringUtils.isEmpty(membermanagement.getBirthday())){
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                Date date = sdf.parse(membermanagement.getBirthday());
//                Date nowDate = new Date();
//                if(date.getMonth() == nowDate.getMonth() && date.getDay() == nowDate.getDay()){ //判断是否为生日  双倍签到积分
//                    //integralrecordController.insertIntegral(integral ,2,3,membermanagements,0,1); //
//                    {
//                        //----
////                double integral=integral;
//                        Integer type=2;
//                        Integer typeId=3;
//                        List<Membermanagement> mList=membermanagements;
//                        int price=0;
//                        int parseIntproductNums=1;
//                        List<Integralrecord> integralrecords = new ArrayList<>();
//                        Integralrecord integralrecord = new Integralrecord();
//                        double nowIntegral = 0;
//                        double nowCountPrice = 0;
//                        for (Membermanagement memberIdo : mList) {  //循环当前门店会员列表为
//                            nowIntegral = memberIdo.getIntegral();
//                            nowCountPrice = memberIdo.getCountPrice();
//                            if (type == 1) {
//                                if (integral < 0) { //扣除类积分
//                                    if ((nowIntegral + integral) >= 0) {
//                                        memberIdo.setIntegral(nowIntegral + integral);
////                        memberId.setCountPrice(nowCountPrice + integral);
//                                        memberIdo.setPrice(memberIdo.getPrice().doubleValue()+(price*parseIntproductNums)); //总消费额
//                                    } else {
//                                        throw new Exception("可用积分不足！");
//                                    }
//                                } else {
//                                    memberIdo.setIntegral(nowIntegral + integral);
//                                    memberIdo.setCountPrice(nowCountPrice + integral);
//                                    memberIdo.setPrice(memberIdo.getPrice().doubleValue()+(price*parseIntproductNums)); //总消费额
//                                }
//                                // type=1 商品积分
//                                integralrecord.setIntegralType(type.toString());
//                                integralrecord.setTypeId(typeId.toString());
//                            } else if (type == 2) {
//                                if (typeId == 2) { //扣除积分
//                                    if ((nowIntegral - integral) >= 0) {
//                                        memberIdo.setIntegral(nowIntegral - integral);
////                        memberId.setCountPrice(nowCountPrice - integral);
//                                    } else {
//                                        throw new Exception("可用积分不足！");
//                                    }
//                                } else {
//                                    memberIdo.setIntegral(nowIntegral + integral);
//                                    memberIdo.setCountPrice(nowCountPrice + integral);
////                    memberId.setPrice(memberId.getPrice()+(price*parseIntproductNums)); //总消费额
//                                }
//                                // type=2 行为积分
//                                integralrecord.setIntegralType(type.toString());
//                                integralrecord.setOtherTypeId(typeId.toString());
//                            }
//                            //更新会员总积分和实际积分
//                            membermanagementService.updateById(memberIdo);
//                            if(type!=2){
//                                membermanagementController.updateMemberLeave(memberIdo.getId() + "");
//                            }
//
//                            //添加积分记录
//                            integralrecord.setIntegral(integral);
//                            if (type == 2 && typeId == 2) integralrecord.setIntegral(-integral);
//                            integralrecord.setCreateTime(DateUtil.getTime());
//                            integralrecord.setMemberid(memberIdo.getId());
//                            integralrecord.setDeptid(ShiroKit.getUser().getDeptId());
//                            integralrecord.setStaffid(ShiroKit.getUser().getId());
//                            integralrecordService.insert(integralrecord);
//                            integralrecords.add(integralrecord);
//                        }
//                        //----
//                    }
//                    membermanagement = membermanagementService.selectById(memberId);
//                }
//            }
            System.out.println(integralrecordController==null);
            //判断可签到获得积分次数 >0执行积分操作
            Integer checkInNum = membermanagement.getCheckInNum();
            if(checkInNum!=null&&checkInNum>0){
                //integralrecordController.insertIntegral(integral,2,0,membermanagements,0,1);
                {
                    //----
//                double integral=integral;
                    Integer type=2;
                    Integer typeId=0;
                    List<Membermanagement> mList=membermanagements;
                    int price=0;
                    int parseIntproductNums=1;
                    List<Integralrecord> integralrecords = new ArrayList<>();
                    Integralrecord integralrecord = new Integralrecord();
                    double nowIntegral = 0;
                    double nowCountPrice = 0;
                    for (Membermanagement memberIdo : mList) {  //循环当前门店会员列表为
                        nowIntegral = memberIdo.getIntegral();
                        nowCountPrice = memberIdo.getCountPrice();
                        if (type == 1) {
                            if (integral < 0) { //扣除类积分
                                if ((nowIntegral + integral) >= 0) {
                                    memberIdo.setIntegral(nowIntegral + integral);
//                        memberId.setCountPrice(nowCountPrice + integral);
                                    memberIdo.setPrice(memberIdo.getPrice().doubleValue()+(price*parseIntproductNums)); //总消费额
                                } else {
                                    throw new Exception("可用积分不足！");
                                }
                            } else {
                                memberIdo.setIntegral(nowIntegral + integral);
                                memberIdo.setCountPrice(nowCountPrice + integral);
                                memberIdo.setPrice(memberIdo.getPrice().doubleValue()+(price*parseIntproductNums)); //总消费额
                            }
                            // type=1 商品积分
                            integralrecord.setIntegralType(type.toString());
                            integralrecord.setTypeId(typeId.toString());
                        } else if (type == 2) {
                            if (typeId == 2) { //扣除积分
                                if ((nowIntegral - integral) >= 0) {
                                    memberIdo.setIntegral(nowIntegral - integral);
//                        memberId.setCountPrice(nowCountPrice - integral);
                                } else {
                                    throw new Exception("可用积分不足！");
                                }
                            } else {
                                memberIdo.setIntegral(nowIntegral + integral);
                                memberIdo.setCountPrice(nowCountPrice + integral);
//                    memberId.setPrice(memberId.getPrice()+(price*parseIntproductNums)); //总消费额
                            }
                            // type=2 行为积分
                            integralrecord.setIntegralType(type.toString());
                            integralrecord.setOtherTypeId(typeId.toString());
                        }
                        //更新会员总积分和实际积分
                        membermanagementService.updateById(memberIdo);
                        if(type!=2){
                            membermanagementController.updateMemberLeave(memberIdo.getId() + "");
                        }

                        //添加积分记录
                        integralrecord.setIntegral(integral);
                        if (type == 2 && typeId == 2) integralrecord.setIntegral(-integral);
                        integralrecord.setCreateTime(DateUtil.getTime());
                        integralrecord.setMemberid(memberIdo.getId());
                        integralrecord.setDeptid(ShiroKit.getUser().getDeptId());
                        integralrecord.setStaffid(ShiroKit.getUser().getId());
                        integralrecordService.insert(integralrecord);
                        integralrecords.add(integralrecord);
                    }
                    //----
                }
                membermanagement = membermanagementService.selectById(memberId);
                membermanagement.setCheckInNum(membermanagement.getCheckInNum()-1);
            }else {
//                throw new Exception("签到次数不足!");
            }
//            if(! StringUtils.isEmpty(membermanagement.getIntroducerId())){ //会员打卡推荐人获得积分
//                List<Membermanagement> introducers = new ArrayList<>();
//
//                Membermanagement introducer = membermanagementService.selectById(membermanagement.getIntroducerId());
//                Membershipcardtype membershipcardtype2 = membershipcardtypeService.selectById(introducer.getLevelID());
//                introducers.add(introducer);
//                integralrecordController.insertIntegral(membershipcardtype2.getNewpoints(),2,3,introducers);
//            }
            membermanagement.setCheckINTime1(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            membermanagement.setIsvisit(0);
            membermanagementService.updateById(membermanagement);
            membermanagementController.updateMemberInfo(membermanagement);
            //进行复签
            update(memberId,chechId);
        }
        return SUCCESS_TIP;
    }

    /**
     * 删除复签记录
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer qiandaoCheckinId) {
        qiandaoCheckinService.deleteById(qiandaoCheckinId);
        return SUCCESS_TIP;
    }

    /**
     * 修改复签记录
     */
    @RequestMapping(value = "/update")
    @ResponseBody
//    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public synchronized Object update(String memberId, String chechId) throws Exception {
        //判断签到场次是否被结束
        if (!StringUtils.isEmpty(chechId)) {
            Checkin checkin1 = checkinService.selectById(chechId);
            if (checkin1 != null) {
                if (checkin1.getStatus() == 2) {
//                    throw new Exception("该场次已经结束无法进行该操作!");
                }
            }
        }
        EntityWrapper<QiandaoCheckin> qiandaoCheckinBaseEntityWrapper = new EntityWrapper<>();
        qiandaoCheckinBaseEntityWrapper.eq("memberid", memberId);
        qiandaoCheckinBaseEntityWrapper.eq("checkinid", chechId);
        QiandaoCheckin qiandaoCheckin = qiandaoCheckinService.selectOne(qiandaoCheckinBaseEntityWrapper);
        if (qiandaoCheckin != null && StringUtils.isEmpty(qiandaoCheckin.getUpdatetime())) {
            qiandaoCheckin.setUpdatetime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            qiandaoCheckinService.updateById(qiandaoCheckin);
            //如果当前用户复签次数累计>=10次更新为普通会员卡
//            EntityWrapper<QiandaoCheckin> qiandaoc = new EntityWrapper<>();
//            qiandaoc.eq("memberid", memberId);
//            qiandaoc.isNotNull("updatetime");
//            int count = qiandaoCheckinService.selectCount(qiandaoc);
//            Membermanagement membermanagement1 = membermanagementService.selectById(memberId);
////            if (membermanagement1 != null && membermanagement1.getLevelID().equals("1")) {
//            if (membermanagement1 != null) {
//                String levelID = membermanagement1.getLevelID();
//                Membershipcardtype membershipcardtype = membershipcardtypeService.selectById(levelID);
//                if (membershipcardtype != null && membershipcardtype.getLeaves() == 0) {
//                    if (membershipcardtype != null && count >= membershipcardtype.getCheckleavenum()) {
//                        Membermanagement membermanagement = membermanagementService.selectById(memberId);
////                        if (membermanagement.getLevelID().equals("1")) {//零时卡更新普通会员卡
//                        EntityWrapper<Membershipcardtype> membershipcardtypeEntityWrapper = new EntityWrapper<>();
//                        membershipcardtypeEntityWrapper.eq("deptid", qiandaoCheckin.getDeptid());
//                        List<Membershipcardtype> list = membershipcardtypeService.selectList(membershipcardtypeEntityWrapper);
//                        if (list.size() >= 2) {
//                            membermanagement.setLevelID(list.get(1).getId() + "");
//                        }
////                        }
//                        membermanagementService.updateById(membermanagement);
//                        membermanagementController.updateMemberInfo(membermanagement);
//                    }
//                }
//
//            }

            //复签成功后统计当前场次完整签到人数
            Checkin checkin = checkinService.selectById(chechId);
            if (checkin != null) {
                if (checkin.getMemberCount() == null) {
                    checkin.setMemberCount(1);
                } else {
                    checkin.setMemberCount((checkin.getMemberCount() + 1));
                }
            }
            checkinService.updateById(checkin);
        }
        return SUCCESS_TIP;
    }

    /**
     * 复签记录详情
     */
    @RequestMapping(value = "/detail/{qiandaoCheckinId}")
    @ResponseBody
    public Object detail(@PathVariable("qiandaoCheckinId") Integer qiandaoCheckinId) {
        return qiandaoCheckinService.selectById(qiandaoCheckinId);
    }
}
