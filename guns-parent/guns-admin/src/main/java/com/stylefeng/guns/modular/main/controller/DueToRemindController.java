package com.stylefeng.guns.modular.main.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.exception.GunsExceptionEnum;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.IIntegralrecordtypeService;
import com.stylefeng.guns.modular.system.model.Integralrecordtype;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.DueToRemind;
import com.stylefeng.guns.modular.main.service.IDueToRemindService;

import java.util.Date;

/**
 * 商品追销管理控制器
 *
 * @author fengshuonan
 * @Date 2018-12-17 11:09:23
 */
@Controller
@RequestMapping("/dueToRemind")
public class DueToRemindController extends BaseController {

    private String PREFIX = "/main/dueToRemind/";

    @Autowired
    private IDueToRemindService dueToRemindService;
    @Autowired
    private IIntegralrecordtypeService iIntegralrecordtypeService;

    /**
     * 跳转到商品追销管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "dueToRemind.html";
    }

    /**
     * 跳转到添加商品追销管理
     */
    @RequestMapping("/dueToRemind_add")
    public String dueToRemindAdd() {
        return PREFIX + "dueToRemind_add.html";
    }

    /**
     * 跳转到修改商品追销管理
     */
    @RequestMapping("/dueToRemind_update/{dueToRemindId}")
    public String dueToRemindUpdate(@PathVariable Integer dueToRemindId, Model model) {
        DueToRemind dueToRemind = dueToRemindService.selectById(dueToRemindId);
        model.addAttribute("item", dueToRemind);
        LogObjectHolder.me().set(dueToRemind);
        return PREFIX + "dueToRemind_edit.html";
    }

    /**
     * 获取商品追销管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<DueToRemind> page = new PageFactory<DueToRemind>().defaultPage();
        BaseEntityWrapper<DueToRemind> baseEntityWrapper = new BaseEntityWrapper<>();
        Page<DueToRemind> result = dueToRemindService.selectPage(page, baseEntityWrapper);
        return super.packForBT(result);
    }

    /**
     * 新增商品追销管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(DueToRemind dueToRemind, Integer productname) throws Exception {
        dueToRemind.setProductId(productname);
        Integralrecordtype integralrecordtype = iIntegralrecordtypeService.selectById(dueToRemind.getProductId());
        if (StringUtils.isEmpty(integralrecordtype.getProducteatingdose())) {
//            throw new GunsException("");
            throw new GunsException(GunsExceptionEnum.REQUEST_NULLS);
        }
        dueToRemind.setProductName(integralrecordtype.getProductname());
        dueToRemind.setCreateTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        dueToRemind.setCreateUserId(ShiroKit.getUser().id);
        dueToRemind.setDeptId(ShiroKit.getUser().getDeptId());
        dueToRemindService.insert(dueToRemind);
        return SUCCESS_TIP;
    }

    /**
     * 删除商品追销管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer dueToRemindId) {
        dueToRemindService.deleteById(dueToRemindId);
        return SUCCESS_TIP;
    }

    /**
     * 修改商品追销管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(DueToRemind dueToRemind) {
//        dueToRemind= dueToRemindService.selectById(dueToRemind.getId());
        dueToRemind.setUpdateTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        dueToRemind.setUpdateUserId(ShiroKit.getUser().id);
        dueToRemindService.updateById(dueToRemind);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/updateStatus")
    @ResponseBody
    public Object updateStatus(DueToRemind dueToRemind) {
        dueToRemind = dueToRemindService.selectById(dueToRemind.getId());
        dueToRemind.setUpdateUserId(ShiroKit.getUser().id);
        dueToRemind.setUpdateTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        if (dueToRemind.getStatus() == 1) {
            dueToRemind.setStatus(0);
        } else {
            dueToRemind.setStatus(1);
        }
        dueToRemindService.updateById(dueToRemind);
        return SUCCESS_TIP;
    }

    /**
     * 商品追销管理详情
     */
    @RequestMapping(value = "/detail/{dueToRemindId}")
    @ResponseBody
    public Object detail(@PathVariable("dueToRemindId") Integer dueToRemindId) {
        return dueToRemindService.selectById(dueToRemindId);
    }

    /**
     * 判断该商品是否需要进行追销
     *
     * @param productId
     * @return
     */
    public boolean judgeDueToRemind(Integer productId) {
        BaseEntityWrapper<DueToRemind> dueToRemindBaseEntityWrapper = new BaseEntityWrapper<>();
        dueToRemindBaseEntityWrapper.eq("status", 0);
        dueToRemindBaseEntityWrapper.eq("productId", productId);
        int i = dueToRemindService.selectCount(dueToRemindBaseEntityWrapper);
        if (i != 0) {
            return true;
        } else {
            return false;
        }
    }
}
