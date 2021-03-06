package com.stylefeng.guns.modular.main.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.*;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IDeptService;
import com.stylefeng.guns.modular.system.service.IUserService;
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
import yongyou.util.YongYouAPIUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品退换货控制器
 *
 * @author fengshuonan
 * @Date 2018-12-11 10:46:39
 */
@Controller
@Scope("prototype")
@RequestMapping("/productReturnChange")
public class ProductReturnChangeController extends BaseController {

    private String PREFIX = "/main/productReturnChange/";

    @Autowired
    private IProductReturnChangeService productReturnChangeService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IIntegralrecordtypeService iIntegralrecordtypeService;
    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private InventoryManagementController inventoryManagementController;
    @Autowired
    private IInventoryManagementService inventoryManagementService;
    @Autowired
    private IIntegralrecordService iIntegralrecordService;
    @Autowired
    private MembermanagementController membermanagementController;
    @Autowired
    private IMainSynchronousService mainSynchronousService;
    @Autowired
    private IDeptService deptService;
    @Autowired
    private IntegralrecordController integralrecordController;

    /**
     * 跳转到商品退换货首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "productReturnChange.html";
    }

    /**
     * 跳转到添加商品退换货
     */
    @RequestMapping("/productReturnChange_add")
    public String productReturnChangeAdd() {
        return PREFIX + "productReturnChange_add.html";
    }

    /**
     * 跳转到修改商品退换货
     */
    @RequestMapping("/productReturnChange_update/{productReturnChangeId}")
    public String productReturnChangeUpdate(@PathVariable Integer productReturnChangeId, Model model) {
        ProductReturnChange productReturnChange = productReturnChangeService.selectById(productReturnChangeId);
        productReturnChange.setCreateuserid(userService.selectById(productReturnChange.getCreateuserid()).getName());
        model.addAttribute("item", productReturnChange);
        LogObjectHolder.me().set(productReturnChange);
        return PREFIX + "productReturnChange_edit.html";
    }

    @RequestMapping("/productReturnChange_update2/{productReturnChangeId}")
    public String productReturnChangeUpdate2(@PathVariable Integer productReturnChangeId, Model model) {
        EntityWrapper<ProductReturnChange> productReturnChangeEntityWrapper = new EntityWrapper<>();
        productReturnChangeEntityWrapper.eq("id", productReturnChangeId);
        Map<String, Object> stringObjectMap = productReturnChangeService.selectMap(productReturnChangeEntityWrapper);
        stringObjectMap.put("createuserid", userService.selectById(stringObjectMap.get("createuserid") + "").getName());
        if ("0".equals(stringObjectMap.get("isInsert") + "")) {
            stringObjectMap.put("isInsert", "不入库");
        } else {
            stringObjectMap.put("isInsert", "入库");
        }
        model.addAttribute("item", stringObjectMap);
        return PREFIX + "productReturnChange_edit1.html";
    }

    /**
     * 获取商品退换货列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition, String productName, Integer returnchangeType, Integer status) {
        Page<ProductReturnChange> page = new PageFactory<ProductReturnChange>().defaultPage();
        EntityWrapper<ProductReturnChange> baseEntityWrapper = new EntityWrapper<>();
        if (!StringUtils.isEmpty(condition))
            baseEntityWrapper.like("memberName", condition).or().like("memberPhone", condition);
        if (!StringUtils.isEmpty(productName)) baseEntityWrapper.like("productName", productName);
        if (returnchangeType != null) baseEntityWrapper.eq("returnchangeType", returnchangeType);
        if (status != null) baseEntityWrapper.eq("status", status);
        baseEntityWrapper.orderBy("createtime", false);
        Page<ProductReturnChange> result = productReturnChangeService.selectPage(page, baseEntityWrapper);
        List<ProductReturnChange> records = result.getRecords();
        records.forEach(a -> {
            User user = userService.selectById(a.getCreateuserid());
            a.setCreateuserid(user.getName());
            a.setMemberPhone(deptService.selectById(a.getDeptId()).getFullname());
        });
        return super.packForBT(result);
    }

    /**
     * 新增商品退换货
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ProductReturnChange productReturnChange) {
        productReturnChangeService.insert(productReturnChange);
        return SUCCESS_TIP;
    }

    /**
     * 删除商品退换货
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer productReturnChangeId) {
        productReturnChangeService.deleteById(productReturnChangeId);
        return SUCCESS_TIP;
    }

    /**
     * 修改商品退换货
     *
     * @param id              操作id
     * @param productname     退换货商品id
     * @param isInsert        是否入库0 是 1 否
     * @param returnchangeNum 退还数量
     * @return
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Object update(@RequestParam Integer id, @RequestParam Integer productname, @RequestParam Integer isInsert, @RequestParam Integer returnchangeNum) throws Exception {
        ProductReturnChange productReturnChange = productReturnChangeService.selectById(id);
        Integralrecordtype integralrecordtype = iIntegralrecordtypeService.selectById(productname);
        productReturnChange.setReturnchangeproductId(integralrecordtype.getId());
        productReturnChange.setReturnchangeproductName(integralrecordtype.getProductname());
        productReturnChange.setStatus(1);
        //更改数量信息
        productReturnChange.setReturnchangeNum(returnchangeNum);
        productReturnChange.setIsInsert(isInsert);

        //判断是否入库
        if (isInsert != null && isInsert == 1) {
            InventoryManagement inventoryManagement = new InventoryManagement();
            inventoryManagement.setConsumptionNum(returnchangeNum);
            controlleradd(inventoryManagement, productReturnChange.getProductId());
        }
        //判断是退货还是换货 如果是换货即选择商品库存减少
        if (productReturnChange != null && productReturnChange.getReturnchangeType() == 1) {
            integralrecordtype.setProductnum((integralrecordtype.getProductnum() - returnchangeNum));
            iIntegralrecordtypeService.updateById(integralrecordtype);
        } else {
            //更改用户积分 (积分回滚)
            String productjifen = integralrecordtype.getProductjifen();
            Integer memberId = productReturnChange.getMemberId();
            Membermanagement membermanagement = membermanagementService.selectById(memberId);
            Double countPrice = membermanagement.getCountPrice();
            Double integral = membermanagement.getIntegral();
            double v = (Double.parseDouble(productjifen) * returnchangeNum);
            membermanagement.setCountPrice((countPrice - v));
            membermanagement.setIntegral((integral - v));
            membermanagementService.updateById(membermanagement);
            //删除积分记录
            iIntegralrecordService.deleteById(productReturnChange.getIntegralrecodeId());
            //删除商品库存管理订单
            inventoryManagementService.deleteById(productReturnChange.getInventoryManagementId());
            //更新会员会员等级
            membermanagementController.updateMemberLeave(memberId.toString());
//            productReceiveT(integralrecordtype, "04", (double) returnchangeNum,memberId);
        }
        productReturnChange.updateById();

        //删除商品订单
        return SUCCESS_TIP;
    }

    /**
     * 商品退换货详情
     */
    @RequestMapping(value = "/detail/{productReturnChangeId}")
    @ResponseBody
    public Object detail(@PathVariable("productReturnChangeId") Integer productReturnChangeId) {
        return productReturnChangeService.selectById(productReturnChangeId);
    }

    public Object controlleradd(InventoryManagement inventoryManagement, Integer productname) {
        //获取商品,名称
        Integralrecordtype integralrecordtype = iIntegralrecordtypeService.selectById(productname);
        integralrecordtype.setProductnum(integralrecordtype.getProductnum() + inventoryManagement.getConsumptionNum());
        iIntegralrecordtypeService.updateById(integralrecordtype);
        //新增库存记录表
        inventoryManagement.setCreatetime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        inventoryManagement.setCreateuserid(ShiroKit.getUser().getId() + "");
        inventoryManagement.setStatus("0");
        inventoryManagement.setDeptid(ShiroKit.getUser().getDeptId() + "");
        inventoryManagement.setIntegralrecordtypeid(productname);
        inventoryManagement.setName(integralrecordtype.getProductname());
        inventoryManagementService.insert(inventoryManagement);
        return SUCCESS_TIP;
    }

    /**
     * 退换货商品对接T+平台
     * @param integralrecordtype
     * @param busiType
     * @param baseQuantity
     * @throws Exception
     */
    public void productReceiveT(Integralrecordtype integralrecordtype, String busiType, Double baseQuantity,Integer memberId) throws Exception {
        String now = DateUtil.format(new Date(), "yyyy-MM-dd");
        String InventoryCode = integralrecordtype.getInventoryCode();
        String tableJson = "{\n" +
                "\tdto:{\n" +
                "\t\tExternalCode: \"" + (new Date().getTime()) + "\",\n" +
                "\t\tVoucherType: {Code: \"ST1004\"},\n" +
                "\t\tVoucherDate: \"" + now + "\",\n" +
                "\t\tBusiType: {Code: \"" + busiType + "\"},\n" +
                "\t\tWarehouse: {Code: \"" + integralrecordtype.getWarehouseCode() + "\"},\n" +
                "\t\tMemo: \"退换货\",\n" +
                "\t\tRDRecordDetails: [{\n" +
                "\t\t\tInvBarCode: \"\",\n" +
                "\t\t\tInventory: {Code: \"" + InventoryCode + "\"},\n" +
                "\t\t\tBaseQuantity: " + baseQuantity + "\n" +
                "\t\t}]\n" +
                "\t}\n" +
                "}";
        Dept dept = deptService.selectById(ShiroKit.getUser().deptId);
        tableJson="{\n" +
                "    dto:{\n" +
                "       VoucherDate: \""+now+"\",\n" +
                "       ExternalCode:\""+(new Date().getTime())+"\",\n" +
                "       Customer: {Code: \""+dept.gettPlusDeptCode()+"\"}, \n" +
                "       InvoiceType: {Code: \"00\"},\n" +
                "       Address: \"新协会员管理系统\",\n" +
                "       LinkMan: \"新协会员管理系统\",\n" +
                "       ContactPhone: \"13611111111\",\n" +
                "       Department :{code: \""+dept.gettPlusDeptCode()+"\"},\n" +
                "       Memo: \"新协会员管理系统\",\n" +
                "       IsAutoGenerateSaleOut:true,\n" +
                "       dynamicPropertyKeys: [\"isautoaudit\",\"isautoauditsaleout\"],"+
                "       dynamicPropertyValues: [true,true],"+
                "       Warehouse:{code:\""+integralrecordtype.getWarehouseCode()+"\"},\n" +
                "       IsPresent:false,\n" +
                "       SaleDeliveryDetails: [{\n" +
                "           Inventory:{Code: \""+InventoryCode+"\"},\n" +
                "           Unit: {Name:\""+integralrecordtype.getUnitName()+"\"},\n" +
                "           Quantity: -"+baseQuantity+",\n" +
                "           OrigPrice: \"0\",\n" +
                "           OrigTaxAmount: -"+baseQuantity*integralrecordtype.getProductpice()+",\n" +
                "           DynamicPropertyKeys:[\"priuserdefnvc1\",\"priuserdefdecm1\"],\n" +
                "           DynamicPropertyValues:[\"sn001\",\"123\"]\n" +
                "       }]\n" +
                "    } \n" +
                "}";;
        MainSynchronous mainSynchronous = new MainSynchronous();
        mainSynchronous.setSynchronousJson(tableJson);
        mainSynchronous.setStatus(0);
        mainSynchronous.setMemberid(memberId);
        mainSynchronous.setSynchronousurl(YongYouAPIUtils.SALEDELIVERY_CREATE);
        mainSynchronousService.insert(mainSynchronous);
        //
        String s = YongYouAPIUtils.postUrl(YongYouAPIUtils.SALEDELIVERY_CREATE, tableJson);
        System.out.println("---"+s);
        if(!"null".equals(s)){
            JSONObject jsonObject = JSON.parseObject(s);
            mainSynchronous.setStatus(2);
            mainSynchronous.setErrorMssage(jsonObject.getString("message"));
        }else {
            mainSynchronous.setStatus(1);
        }
        mainSynchronousService.updateById(mainSynchronous);

        //
//        integralrecordController.receiveVoucherCreate(dept.gettPlusDeptCode(),-(baseQuantity*integralrecordtype.getProductpice()),0,"商品退款",true,memberId);

    }
}
