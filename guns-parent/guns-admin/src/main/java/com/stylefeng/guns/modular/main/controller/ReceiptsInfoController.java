package com.stylefeng.guns.modular.main.controller;

import com.alibaba.druid.util.StringUtils;
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
import com.stylefeng.guns.modular.system.model.ReceiptsInfo;
import com.stylefeng.guns.modular.main.service.IReceiptsInfoService;

/**
 * 小票信息控制器
 *
 * @author fengshuonan
 * @Date 2019-02-21 14:33:26
 */
@Controller
@RequestMapping("/receiptsInfo")
public class ReceiptsInfoController extends BaseController {

    private String PREFIX = "/main/receiptsInfo/";

    @Autowired
    private IReceiptsInfoService receiptsInfoService;

    /**
     * 跳转到小票信息首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "receiptsInfo.html";
    }

    /**
     * 跳转到添加小票信息
     */
    @RequestMapping("/receiptsInfo_add")
    public String receiptsInfoAdd() {
        return PREFIX + "receiptsInfo_add.html";
    }

    /**
     * 跳转到修改小票信息
     */
    @RequestMapping("/receiptsInfo_update/{receiptsInfoId}")
    public String receiptsInfoUpdate(@PathVariable Integer receiptsInfoId, Model model) {
        ReceiptsInfo receiptsInfo = receiptsInfoService.selectById(receiptsInfoId);
        model.addAttribute("item",receiptsInfo);
        LogObjectHolder.me().set(receiptsInfo);
        return PREFIX + "receiptsInfo_edit.html";
    }

    /**
     * 获取小票信息列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<ReceiptsInfo> page = new PageFactory<ReceiptsInfo>().defaultPage();
        BaseEntityWrapper<ReceiptsInfo> baseEntityWrapper = new BaseEntityWrapper<>();

       if(!StringUtils.isEmpty(condition)){
           baseEntityWrapper.like("memberName",condition).or().like("memberPhone",condition);
       } baseEntityWrapper.orderBy("createTime",false);

        Page<ReceiptsInfo> result = receiptsInfoService.selectPage(page, baseEntityWrapper);
        return super.packForBT(result);
    }

    /**
     * 新增小票信息
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ReceiptsInfo receiptsInfo) {
        receiptsInfoService.insert(receiptsInfo);
        return SUCCESS_TIP;
    }

    /**
     * 删除小票信息
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer receiptsInfoId) {
        receiptsInfoService.deleteById(receiptsInfoId);
        return SUCCESS_TIP;
    }

    /**
     * 修改小票信息
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ReceiptsInfo receiptsInfo) {
        receiptsInfoService.updateById(receiptsInfo);
        return SUCCESS_TIP;
    }

    /**
     * 小票信息详情
     */
    @RequestMapping(value = "/detail/{receiptsInfoId}")
    @ResponseBody
    public Object detail(@PathVariable("receiptsInfoId") Integer receiptsInfoId) {
        return receiptsInfoService.selectById(receiptsInfoId);
    }
}
