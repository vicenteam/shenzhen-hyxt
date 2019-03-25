package com.stylefeng.guns.modular.main.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.IIntegralrecordtypeService;
import com.stylefeng.guns.modular.main.service.IMainSynchronousService;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.main.service.IVerificationCodeService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 验证码控制器
 *
 * @author fengshuonan
 * @Date 2019-02-20 16:56:21
 */
@Controller
@RequestMapping("/jifenduihuan")
public class JifenduihuanController extends BaseController {

    private String PREFIX = "/main/jifenduihuan/";

    @Autowired
    private IVerificationCodeService verificationCodeService;
    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private IntegralrecordController integralrecordController;
    @Autowired
    private IIntegralrecordtypeService integralrecordtypeServicel;
    @Autowired
    private IDeptService deptService;
    @Autowired
    private IMainSynchronousService mainSynchronousService;

    /**
     * 跳转到验证码首页
     */
    @RequestMapping("")
    public String index(Model model) {
        System.out.println("----");
        return PREFIX + "jifenduihuan.html";
    }


    /**
     * 进行积分兑换
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Object list(String memberId, String productId, Integer productNum) throws Exception {
        Dept dept = deptService.selectById(ShiroKit.getUser().deptId);
        Integralrecordtype integralrecordtype = integralrecordtypeServicel.selectById(productId);
        if (integralrecordtype.getProductnum() - productNum < 0) {
            throw new Exception("兑换数量超出库存总量");
        }
        Membermanagement membermanagement = membermanagementService.selectById(memberId);
        List<Membermanagement> membermanagementList = new ArrayList<>();
        membermanagementList.add(membermanagement);
        //扣除积分
        integralrecordController.insertIntegral(-(integralrecordtype.getProductduihuanjifen() * productNum), 1, Integer.parseInt(productId), membermanagementList, 0, 1);
        //更新本地商品库存
        integralrecordtype.setProductnum(integralrecordtype.getProductnum() - productNum);
        integralrecordtypeServicel.updateById(integralrecordtype);
        //提交t+数据
        String tableJson = "";
        boolean busiType = false;
        if (integralrecordtype.getProducttype() == 0) {//赠送出库
            busiType = true;
        }
        List<MainSynchronous> synchronousList = new ArrayList<>();
        tableJson =
                "{\n" +
                        "    dto:{\n" +
                        "       VoucherDate: \"" + DateUtil.format(new Date(), "yyyy-MM-dd") + "\",\n" +
                        "       ExternalCode:\"" + new Date().getTime() + "\",\n" +
                        "       Customer: {Code: \"" + dept.gettPlusDeptCode() + "\"}, \n" +
                        "       InvoiceType: {Code: \"00\"},\n" +
                        "       Address: \"新协会员管理系统\",\n" +
                        "       LinkMan: \"新协会员管理系统\",\n" +
                        "       ContactPhone: \"13611111111\",\n" +
                        "       Department :{code: \"" + dept.gettPlusDeptCode() + "\"},\n" +
                        "       Memo: \"新协会员管理系统-兑换商品-兑换数量：" + productNum + "\",\n" +
                        "       IsAutoGenerateSaleOut:true,\n" +
                        "       dynamicPropertyKeys: [\"isautoaudit\",\"isautoauditsaleout\"]," +
                        "       dynamicPropertyValues: [true,true]," +
                        "       Warehouse:{code:\"" + integralrecordtype.getWarehouseCode() + "\"},\n" +
                        "       IsPresent:" + true + ",\n" +
                        "       SaleDeliveryDetails: [{\n" +
                        "           Inventory:{Code: \"" + integralrecordtype.getInventoryCode() + "\"},\n" +
                        "           Unit: {Name:\"" + integralrecordtype.getUnitName() + "\"},\n" +
                        "           Quantity: " + (double) productNum + ",\n" +
                        "           OrigPrice: " + integralrecordtype.getProductpice() * (double) productNum + ",\n" +
                        "           OrigTaxAmount: " + integralrecordtype.getProductpice() * (double) productNum + ",\n" +
                        "           DynamicPropertyKeys:[\"priuserdefnvc1\",\"priuserdefdecm1\"],\n" +
                        "           DynamicPropertyValues:[\"sn001\",\"123\"]\n" +
                        "       }]\n" +
                        "    } \n" +
                        "}";
        MainSynchronous mainSynchronous = new MainSynchronous();
        mainSynchronous.setSynchronousJson(tableJson);
        mainSynchronous.setStatus(0);
        mainSynchronous.setMemberid(Integer.parseInt(memberId));
        mainSynchronous.setCreatedt(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        mainSynchronousService.insert(mainSynchronous);
        integralrecordController.synchronousData(mainSynchronous);
        return SUCCESS_TIP;
    }


}
