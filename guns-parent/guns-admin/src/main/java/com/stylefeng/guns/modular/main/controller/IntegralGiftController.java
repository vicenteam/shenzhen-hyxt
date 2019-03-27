package com.stylefeng.guns.modular.main.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.common.annotion.BussinessLog;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.IClearService;
import com.stylefeng.guns.modular.main.service.IIntegralrecordService;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.system.model.Clear;
import com.stylefeng.guns.modular.system.model.Dept;
import com.stylefeng.guns.modular.system.model.Integralrecord;
import com.stylefeng.guns.modular.system.model.Membermanagement;
import com.stylefeng.guns.modular.system.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 积分清零控制器
 *
 * @author fengshuonan
 * @Date 2018-08-14 16:47:26
 */
@Controller
@RequestMapping("/integralgift")
public class IntegralGiftController extends BaseController {

    private String PREFIX = "/main/integralrecord/";

    @Autowired
    private IDeptService deptService;
    @Autowired
    private IIntegralrecordService integralrecordService;
    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private MembermanagementController membermanagementController;
    @Autowired
    private IntegralrecordController integralrecordController;

    @RequestMapping("")
    public String index(Model model){
        Dept dept = deptService.selectById(ShiroKit.getUser().getDeptId());
        model.addAttribute("dept",dept);
        return PREFIX + "integralGift.html";
    }

    /**
     * 添加赠送积分
     */
    @BussinessLog(value = "添加赠送积分", key = "tjjfzs")
    @RequestMapping(value = "/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Object add(double integral) throws Exception {
        BaseEntityWrapper<Membermanagement> wrapper = new BaseEntityWrapper<>();
        List<Membermanagement> ms = membermanagementService.selectList(wrapper);
        //积分添加操作
        //integralrecordController.insertIntegral(integral,2,10,ms,0,1);
        {
            //----
//            double integral=-(integralrecordtype.getProductduihuanjifen() * productNum);
            Integer type=2;
            Integer typeId=10;
            List<Membermanagement> mList=ms;
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
        return SUCCESS_TIP;
    }
}
