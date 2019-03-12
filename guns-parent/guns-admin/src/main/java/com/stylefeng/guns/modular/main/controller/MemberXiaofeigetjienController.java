package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
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
import com.stylefeng.guns.modular.system.model.MemberXiaofeigetjien;
import com.stylefeng.guns.modular.main.service.IMemberXiaofeigetjienService;

/**
 * 购买部分商品添加积分控制器
 *
 * @author fengshuonan
 * @Date 2019-03-12 15:30:28
 */
@Controller
@RequestMapping("/memberXiaofeigetjien")
public class MemberXiaofeigetjienController extends BaseController {

    private String PREFIX = "/main/memberXiaofeigetjien/";

    @Autowired
    private IMemberXiaofeigetjienService memberXiaofeigetjienService;

    /**
     * 跳转到购买部分商品添加积分首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "memberXiaofeigetjien.html";
    }

    /**
     * 跳转到添加购买部分商品添加积分
     */
    @RequestMapping("/memberXiaofeigetjien_add")
    public String memberXiaofeigetjienAdd() {
        return PREFIX + "memberXiaofeigetjien_add.html";
    }

    /**
     * 跳转到修改购买部分商品添加积分
     */
    @RequestMapping("/memberXiaofeigetjien_update/{memberXiaofeigetjienId}")
    public String memberXiaofeigetjienUpdate(@PathVariable Integer memberXiaofeigetjienId, Model model) {
        MemberXiaofeigetjien memberXiaofeigetjien = memberXiaofeigetjienService.selectById(memberXiaofeigetjienId);
        model.addAttribute("item",memberXiaofeigetjien);
        LogObjectHolder.me().set(memberXiaofeigetjien);
        return PREFIX + "memberXiaofeigetjien_edit.html";
    }

    /**
     * 获取购买部分商品添加积分列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<MemberXiaofeigetjien> page = new PageFactory<MemberXiaofeigetjien>().defaultPage();
        BaseEntityWrapper<MemberXiaofeigetjien> baseEntityWrapper = new BaseEntityWrapper<>();
        Page<MemberXiaofeigetjien> result = memberXiaofeigetjienService.selectPage(page, baseEntityWrapper);
        return super.packForBT(result);
    }

    /**
     * 新增购买部分商品添加积分
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(MemberXiaofeigetjien memberXiaofeigetjien) {
        memberXiaofeigetjienService.insert(memberXiaofeigetjien);
        return SUCCESS_TIP;
    }

    /**
     * 删除购买部分商品添加积分
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer memberXiaofeigetjienId) {
        memberXiaofeigetjienService.deleteById(memberXiaofeigetjienId);
        return SUCCESS_TIP;
    }

    /**
     * 修改购买部分商品添加积分
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(MemberXiaofeigetjien memberXiaofeigetjien) {
        memberXiaofeigetjienService.updateById(memberXiaofeigetjien);
        return SUCCESS_TIP;
    }

    /**
     * 购买部分商品添加积分详情
     */
    @RequestMapping(value = "/detail/{memberXiaofeigetjienId}")
    @ResponseBody
    public Object detail(@PathVariable("memberXiaofeigetjienId") Integer memberXiaofeigetjienId) {
        return memberXiaofeigetjienService.selectById(memberXiaofeigetjienId);
    }
}
