package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.system.model.Membermanagement;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.PrepaidRecord;
import com.stylefeng.guns.modular.main.service.IPrepaidRecordService;

import java.util.Date;

/**
 * 充值记录控制器
 *
 * @author fengshuonan
 * @Date 2019-01-28 15:54:38
 */
@Controller
@RequestMapping("/prepaidRecord")
public class PrepaidRecordController extends BaseController {

    private String PREFIX = "/main/prepaidRecord/";

    @Autowired
    private IPrepaidRecordService prepaidRecordService;
    @Autowired
    private IMembermanagementService membermanagementService;

    /**
     * 跳转到充值记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "prepaidRecord.html";
    }

    /**
     * 跳转到添加充值记录
     */
    @RequestMapping("/prepaidRecord_add")
    public String prepaidRecordAdd() {
        return PREFIX + "prepaidRecord_add.html";
    }

    /**
     * 跳转到修改充值记录
     */
    @RequestMapping("/prepaidRecord_update/{prepaidRecordId}")
    public String prepaidRecordUpdate(@PathVariable Integer prepaidRecordId, Model model) {
        PrepaidRecord prepaidRecord = prepaidRecordService.selectById(prepaidRecordId);
        model.addAttribute("item",prepaidRecord);
        LogObjectHolder.me().set(prepaidRecord);
        return PREFIX + "prepaidRecord_edit.html";
    }

    /**
     * 获取充值记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<PrepaidRecord> page = new PageFactory<PrepaidRecord>().defaultPage();
        BaseEntityWrapper<PrepaidRecord> baseEntityWrapper = new BaseEntityWrapper<>();
        baseEntityWrapper.like("prepaidRecordMemberName",condition);
        baseEntityWrapper.or().like("prepaidRecordMemberPhone",condition);
        baseEntityWrapper.orderBy("prepaidRecordTime",false);
        Page<PrepaidRecord> result = prepaidRecordService.selectPage(page, baseEntityWrapper);
        return super.packForBT(result);
    }

    /**
     * 新增充值记录
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(PrepaidRecord prepaidRecord) {
        prepaidRecord.setPrepaidRecordTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        prepaidRecord.setDeptid(ShiroKit.getUser().deptId);
        prepaidRecordService.insert(prepaidRecord);
        //更新会员剩余可用金额
        Membermanagement membermanagement = membermanagementService.selectById(prepaidRecord.getPrepaidRecordMemberId());
        membermanagement.setMoney((membermanagement.getMoney()+prepaidRecord.getPrepaidRecordMoney()));
        membermanagementService.updateById(membermanagement);
        return SUCCESS_TIP;
    }

    /**
     * 删除充值记录
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer prepaidRecordId) {
        prepaidRecordService.deleteById(prepaidRecordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改充值记录
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(PrepaidRecord prepaidRecord) {
        prepaidRecordService.updateById(prepaidRecord);
        return SUCCESS_TIP;
    }

    /**
     * 充值记录详情
     */
    @RequestMapping(value = "/detail/{prepaidRecordId}")
    @ResponseBody
    public Object detail(@PathVariable("prepaidRecordId") Integer prepaidRecordId) {
        return prepaidRecordService.selectById(prepaidRecordId);
    }
}
