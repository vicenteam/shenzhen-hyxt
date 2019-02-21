package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.shiro.ShiroKit;
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
import com.stylefeng.guns.modular.system.model.VerificationCode;
import com.stylefeng.guns.modular.main.service.IVerificationCodeService;

import java.util.Date;
import java.util.Random;

/**
 * 验证码控制器
 *
 * @author fengshuonan
 * @Date 2019-02-20 16:56:21
 */
@Controller
@RequestMapping("/verificationCode")
public class VerificationCodeController extends BaseController {

    private String PREFIX = "/main/verificationCode/";

    @Autowired
    private IVerificationCodeService verificationCodeService;
    @Autowired
    private IMembermanagementService membermanagementService;

    /**
     * 跳转到验证码首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "verificationCode.html";
    }

    /**
     * 跳转到添加验证码
     */
    @RequestMapping("/verificationCode_add")
    public String verificationCodeAdd() {
        return PREFIX + "verificationCode_add.html";
    }

    /**
     * 跳转到修改验证码
     */
    @RequestMapping("/verificationCode_update/{verificationCodeId}")
    public String verificationCodeUpdate(@PathVariable Integer verificationCodeId, Model model) {
        VerificationCode verificationCode = verificationCodeService.selectById(verificationCodeId);
        model.addAttribute("item",verificationCode);
        LogObjectHolder.me().set(verificationCode);
        return PREFIX + "verificationCode_edit.html";
    }

    /**
     * 获取验证码列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<VerificationCode> page = new PageFactory<VerificationCode>().defaultPage();
        BaseEntityWrapper<VerificationCode> baseEntityWrapper = new BaseEntityWrapper<>();
        Page<VerificationCode> result = verificationCodeService.selectPage(page, baseEntityWrapper);
        return super.packForBT(result);
    }

    /**
     * 新增验证码
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(VerificationCode verificationCode,Integer memberId) {
        Membermanagement membermanagement = membermanagementService.selectById(memberId);
        if(membermanagement!=null&&membermanagement.getPhone().length()>0){
            BaseEntityWrapper<VerificationCode> verificationCodeBaseEntityWrapper = new BaseEntityWrapper<>();
            verificationCodeBaseEntityWrapper.eq("memberid",memberId);
            VerificationCode verificationCode1 = verificationCodeService.selectOne(verificationCodeBaseEntityWrapper);
            if(verificationCode1!=null){
                verificationCodeService.deleteById(verificationCode1.getId());
            }
            Random random = new Random();
            int x = random.nextInt(9999);
            verificationCode.setDeptid(ShiroKit.getUser().deptId);
            verificationCode.setMemberid(memberId);
            verificationCode.setPhone(membermanagement.getPhone());
            verificationCode.setCreatelongtime(new Date().getTime());
            verificationCode.setStatus("0");
            verificationCode.setVerificationcode(x+"");
            verificationCodeService.insert(verificationCode);
        }else {
            throw new GunsException(BizExceptionEnum.PHONE_ERROR);
        }

        return SUCCESS_TIP;
    }

    /**
     * 删除验证码
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer verificationCodeId) {
        verificationCodeService.deleteById(verificationCodeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改验证码
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(VerificationCode verificationCode) {
        verificationCodeService.updateById(verificationCode);
        return SUCCESS_TIP;
    }

    /**
     * 验证码详情
     */
    @RequestMapping(value = "/detail/{verificationCodeId}")
    @ResponseBody
    public Object detail(@PathVariable("verificationCodeId") Integer verificationCodeId) {
        return verificationCodeService.selectById(verificationCodeId);
    }
}
