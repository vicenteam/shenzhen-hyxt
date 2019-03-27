package com.stylefeng.guns.modular.main.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.common.annotion.BussinessLog;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.HttpKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.*;
import com.stylefeng.guns.modular.system.controller.DeptController;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import yongyou.util.YongYouAPIUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 新增积分控制器
 *
 * @author fengshuonan
 * @Date 2018-08-14 16:47:26
 */
@Controller
@Scope("prototype")
@RequestMapping("/integralrecord")
public class IntegralrecordController extends BaseController {

    private String PREFIX = "/main/integralrecord/";

    @Autowired
    private IIntegralrecordService integralrecordService;
    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private MembermanagementController membermanagementController;
    @Autowired
    private IIntegralrecordtypeService integralrecordtypeService;
    @Autowired
    private IInventoryManagementService inventoryManagementService;
    @Autowired
    private DueToRemindController dueToRemindController;
    @Autowired
    private DeptController deptController;
    @Autowired
    private IDeptService deptService;
    @Autowired
    private IMainSynchronousService mainSynchronousService;
    @Autowired
    private IVerificationCodeService verificationCodeService;
    @Autowired
    private IMembershipcardtypeService membershipcardtypeService;
    @Autowired
    private IMemberXiaofeigetjienService memberXiaofeigetjienService;


    /**
     * 跳转到新增积分首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "integralrecord.html";
    }

    @RequestMapping("productSalesRankingPage")
    public String productSalesRankingPage() {
        return PREFIX + "productSalesRankingPage.html";
    }

    /**
     * 跳转到添加新增积分
     */
    @RequestMapping("/integralrecord_add")
    public String integralrecordAdd(Model model) {
//        EntityWrapper tWrapper = new EntityWrapper();
//        tWrapper.notIn("names", "积分清零", "积分恢复", "积分兑换");
//        List<Integralrecordtype> types = integralrecordtypeService.selectList(tWrapper);
//        model.addAttribute("type", types);
        return PREFIX + "integralrecord_add.html";
    }

    /**
     * 跳转到修改新增积分
     */
    @RequestMapping("/integralrecord_update/{integralrecordId}")
    public String integralrecordUpdate(@PathVariable Integer integralrecordId, Model model) {
        Integralrecord integralrecord = integralrecordService.selectById(integralrecordId);
        model.addAttribute("item", integralrecord);
        LogObjectHolder.me().set(integralrecord);
        return PREFIX + "integralrecord_edit.html";
    }

    /**
     * 获取新增积分列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<Integralrecord> page = new PageFactory<Integralrecord>().defaultPage();
        BaseEntityWrapper<Integralrecord> baseEntityWrapper = new BaseEntityWrapper<>();
        Page<Integralrecord> result = integralrecordService.selectPage(page, baseEntityWrapper);
        return super.packForBT(result);
    }

    /**
     * 商品销量排名
     *
     * @param deptId
     * @param monthTime1
     * @param monthTime2
     * @param periodTime1
     * @param periodTime2
     * @param orderBy
     * @param desc
     * @return
     */
    @RequestMapping(value = "/productSalesRanking")
    @ResponseBody
    public Object productSalesRanking(Integer offset,
                                      Integer limit,
                                      Integer deptId,
                                      String monthTime1,
                                      String monthTime2,
                                      String periodTime1,
                                      String periodTime2,
                                      String orderBy,
                                      String desc) {
        String format1 = DateUtil.format(new Date(), "yyyy-MM");
        String format2 = DateUtil.format(new Date(), "yyyy-MM-dd");
        monthTime1 = format1 + "-01";
        monthTime2 = format2;

        HttpServletRequest request = HttpKit.getRequest();
        try {
            orderBy = request.getParameter("sort");         //排序字段名称
            desc = request.getParameter("order");       //asc或desc(升序或降序)
        } catch (Exception e) {

        }
        //获取deptids
        List<Map<String, Object>> list = (List<Map<String, Object>>) deptController.findDeptLists(deptId.toString());
        String deptIds = "";
        for (Map<String, Object> map : list) {
            deptIds += map.get("id") + ",";
        }
        deptIds = deptIds.substring(0, deptIds.length() - 1);
        Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
        int i = integralrecordService.productSalesRankingintCount(page.getOffset(), page.getLimit(), deptIds.toString(), monthTime1, monthTime2, periodTime1, periodTime2, orderBy, desc);
        page.setTotal(i);
        List<Map<String, Object>> mapList = integralrecordService.productSalesRanking(page.getOffset(), page.getLimit(), deptIds.toString(), monthTime1, monthTime2, periodTime1, periodTime2, orderBy, desc);
        page.setRecords(mapList);
        return super.packForBT(page);
    }

    /**
     * 新增积分
     */
    @BussinessLog(value = "新增会员积分", key = "xzhyjf")
    @RequestMapping(value = "/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public synchronized Object add(Double integral, Integer productname, Integer memberId, Integer consumptionNum,String productMianFeiNums, String productIds, String productNums, Double play, Integer playType, String verificationcode, String tableNase64Data,String scannerTypeContent) throws Exception {
        Dept dept = deptService.selectById(ShiroKit.getUser().deptId);
        List<MainSynchronous> synchronousList = new ArrayList<>();
        BaseEntityWrapper<Membermanagement> mWrapper = new BaseEntityWrapper<>();
        mWrapper.eq("id", memberId);
        List<Membermanagement> membermanagements = membermanagementService.selectList(mWrapper);
        if (membermanagements.size() == 0) {
            throw new GunsException(BizExceptionEnum.READCARD_ERROR);
        }
        if (playType == 0) {
            //判断验证码是否通过
            BaseEntityWrapper<VerificationCode> verificationCodeBaseEntityWrapper = new BaseEntityWrapper<>();
            verificationCodeBaseEntityWrapper.eq("memberid", memberId);
            VerificationCode verificationCode = verificationCodeService.selectOne(verificationCodeBaseEntityWrapper);
            if (verificationCode == null || !verificationCode.getVerificationcode().equals(verificationcode)) {
                throw new GunsException(BizExceptionEnum.VERIFICATIONCODE_ERROR);
            } else {
                verificationCodeService.deleteById(verificationCode.getId());
            }

            double money = membermanagements.get(0).getMoney();
            if (money < play) {
                throw new GunsException(BizExceptionEnum.MONEY_ERROR);
            } else {//扣除用户余额
                Membermanagement membermanagement = membermanagements.get(0);
                membermanagement.setMoney(membermanagement.getMoney() - play);
                membermanagementService.updateById(membermanagement);
            }
        }
        if (true) {//积分消费
            if (membermanagements.get(0).getIntegral() + integral < 0) {
                throw new GunsException(BizExceptionEnum.JIFEN_ERROR);
            }
        }
        //更新库存
        String[] split = productIds.split(",");
        String[] productNumsSplit = productNums.split(",");
        String[] productMianFeiNum = productMianFeiNums.split(",");
        int index = 0;

        // 2019-02-27 luo
        Membershipcardtype lType = membershipcardtypeService.selectById(membermanagements.get(0).getLevelID());
        Double getIntegral; //

        for (String temp : split) {
            int parseIntTemp = Integer.parseInt(temp);
            int parseIntproductNums = Integer.parseInt(productNumsSplit[index]);
            int MianFeiNum = Integer.parseInt(productMianFeiNum[index]);
            //积分添加操作
            BaseEntityWrapper<Integralrecordtype> typeWrapper = new BaseEntityWrapper<>();
            typeWrapper.eq("id", parseIntTemp);
            Integralrecordtype integralrecordtype = integralrecordtypeService.selectOne(typeWrapper);
//            getIntegral = integralrecordtype.getProductpice() / lType.getShopping();
            getIntegral = Double.parseDouble(integralrecordtype.getProductjifen());
//            List<Integralrecord> integralrecords = insertIntegral(getIntegral, 1, parseIntTemp, membermanagements, integralrecordtype.getProductpice(),parseIntproductNums);
            List<Integralrecord> integralrecords = insertIntegral(integral, 1, parseIntTemp, membermanagements, integralrecordtype.getProductpice(),parseIntproductNums);

            integralrecordtype.setProductnum(integralrecordtype.getProductnum() - parseIntproductNums);//库存减
            integralrecordtype.setUpdatetime(DateUtil.getTime());
            integralrecordtype.setUpdateuserid(ShiroKit.getUser().getId().toString());
            if (integralrecordtype.getProductnum() < 0) {
                throw new GunsException(BizExceptionEnum.PRODUCTNUM_ERROR);
            }
            integralrecordtypeService.updateById(integralrecordtype);

            //插入商品记录
            InventoryManagement inventoryManagement = new InventoryManagement();
            inventoryManagement.setCreatetime(DateUtil.getTime());
            inventoryManagement.setCreateuserid(ShiroKit.getUser().getId().toString());
            inventoryManagement.setDeptid(ShiroKit.getUser().getDeptId().toString());
            inventoryManagement.setIntegralrecordtypeid(integralrecordtype.getId());
            inventoryManagement.setStatus("1");
            inventoryManagement.setMemberPhone(membermanagements.get(0).getPhone());
            inventoryManagement.setMemberid(memberId.toString());
            inventoryManagement.setConsumptionNum(1);
            inventoryManagement.setName(integralrecordtype.getProductname());
            inventoryManagement.setMemberName(membermanagements.get(0).getName());
            inventoryManagement.setIntegralid(integralrecords.get(0).getId());
            inventoryManagement.setConsumptionNum(parseIntproductNums);
            //设置支付金额与付款方式
            DecimalFormat df   = new DecimalFormat("######0.00");
            inventoryManagement.setJine(Double.parseDouble(df.format((integralrecordtype.getProductpice()*parseIntproductNums)*lType.getShoppingnew())));
            inventoryManagement.setPayType(playType==0?"账户余额"
                    :playType==1?"现金支付":
                    playType==2?"支付宝":
                    playType==3?"微信":
                    playType==4?"其他":"积分消费"
            );
            //判断商品是否需要进行追销
            if (dueToRemindController.judgeDueToRemind(parseIntTemp)) {
                inventoryManagement.setIsDueToRemind(1);
            }
            inventoryManagementService.insert(inventoryManagement);
            //判断当前商品类型是否参与购买1000送？积分类型
            if(integralrecordtype.getIsfandian()==1){
                MemberXiaofeigetjien t = new MemberXiaofeigetjien();
                t.setDeptid(ShiroKit.getUser().deptId);
                t.setMemberid(memberId);
                t.setPlayproductnum(parseIntproductNums);
                t.setPlayproductmoney(integralrecordtype.getProductpice()*parseIntproductNums);
                memberXiaofeigetjienService.insert(t);
                int i = memberXiaofeigetjienService.sumMoneyByMemberId(memberId);
                if(i!=0){//更新获取次数
                    Integer addcheckInNum = membermanagements.get(0).getAddcheckInNum();
                    if(addcheckInNum!=null){
                        int floor = (int)Math.floor(i / 1000);
                        int isinsert=floor-addcheckInNum;
                        if(isinsert>0){
                            Membermanagement membermanagement = membermanagementService.selectById(memberId);
                            membermanagement.setAddcheckInNum(membermanagement.getAddcheckInNum()+isinsert);
                            //添加可签到获得积分次数
                            Membershipcardtype membershipcardtype = membershipcardtypeService.selectById(membermanagement.getLevelID());
                            if(membershipcardtype.getKeqiandaonum()!=null){
                                membermanagement.setCheckInNum(membermanagement.getCheckInNum()+membershipcardtype.getKeqiandaonum());
                            }
                            membermanagementService.updateById(membermanagement);
                        }
                    }
                }

            }

            //同步数据写入T+库
            String now = DateUtil.format(new Date(), "yyyy-MM-dd");
            int radomInt = new Random().nextInt(999999);
            double baseQuantity = (double) parseIntproductNums;
            //PartnerDTO对象
//        YongYouAPIUtils.postUrl(YongYouAPIUtils.PARTNER_QUERY,"");
            //获取存货编码信息
            String InventoryCode = integralrecordtype.getInventoryCode();
//        YongYouAPIUtils.postUrl(YongYouAPIUtils.INVENTORY_QUERY,"{\"param\":{\"code\":\""+InventoryCode+"\"}}");
            int i = mainSynchronousService.selectCount(null);
            String tableJson = "";
            boolean busiType = false;
            if (integralrecordtype.getProducttype() == 0) {//赠送出库
                busiType = true;
            }
            tableJson =
                    "{\n" +
                            "    dto:{\n" +
                            "       VoucherDate: \"" + now + "\",\n" +
                            "       ExternalCode:\"" + (i + 1) + "\",\n" +
                            "       Customer: {Code: \""+dept.gettPlusDeptCode()+"\"}, \n" +
                            "       InvoiceType: {Code: \"00\"},\n" +
                            "       Address: \"新协会员管理系统\",\n" +
                            "       LinkMan: \"新协会员管理系统\",\n" +
                            "       ContactPhone: \"13611111111\",\n" +
                            "       Department :{code: \"" + dept.gettPlusDeptCode() + "\"},\n" +
                            "       Memo: \"新协会员管理系统-购买商品-赠品数量："+MianFeiNum+"\",\n" +
                            "       IsAutoGenerateSaleOut:true,\n" +
                            "       dynamicPropertyKeys: [\"isautoaudit\",\"isautoauditsaleout\"],"+
                            "       dynamicPropertyValues: [true,true],"+
                            "       Warehouse:{code:\"" + integralrecordtype.getWarehouseCode() + "\"},\n" +
                            "       IsPresent:" + busiType + ",\n" +
                            "       SaleDeliveryDetails: [{\n" +
                            "           Inventory:{Code: \"" + InventoryCode + "\"},\n" +
                            "           Unit: {Name:\"" + integralrecordtype.getUnitName() + "\"},\n" +
                            "           Quantity: " + baseQuantity + ",\n" +
                            "           OrigPrice: " + integralrecordtype.getProductpice() * baseQuantity + ",\n" +
                            "           OrigTaxAmount: " + integralrecordtype.getProductpice() * baseQuantity + ",\n" +
                            "           DynamicPropertyKeys:[\"priuserdefnvc1\",\"priuserdefdecm1\"],\n" +
                            "           DynamicPropertyValues:[\"sn001\",\"123\"]\n" +
                            "       }]\n" +
                            "    } \n" +
                            "}";
            MainSynchronous mainSynchronous = new MainSynchronous();
            mainSynchronous.setSynchronousJson(tableJson);
            mainSynchronous.setStatus(0);
            mainSynchronous.setMemberid(memberId);
            mainSynchronous.setCreatedt(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            mainSynchronousService.insert(mainSynchronous);
            //
//            synchronousData(mainSynchronous);
            synchronousList.add(mainSynchronous);
            index++;
        }
        for (MainSynchronous a : synchronousList) {
            synchronousData(a);
        }
        //新增购买小票
        ReceiptsInfo receiptsInfo = new ReceiptsInfo();
        receiptsInfo.setCreateTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        receiptsInfo.setDeptId(ShiroKit.getUser().deptId);
        receiptsInfo.setMemberId(memberId);
        receiptsInfo.setMemberName(membermanagements.get(0).getName());
        receiptsInfo.setMemberPhone(membermanagements.get(0).getPhone());
        receiptsInfo.setPlayMoney(play + "");
        receiptsInfo.setReceiptsBase64Img(tableNase64Data);
        receiptsInfo.insert();
        //提交T+收款单
        receiveVoucherCreate(dept.gettPlusDeptCode(),play,playType,"商品购买",true,memberId);
        return SUCCESS_TIP;
    }

    /**
     * 会员积分增加并新增记录
     *
     * @param integral
     * @param type
     * @param typeId
     * @param mList
     * @return
     * @throws Exception
     */
    public synchronized List<Integralrecord> insertIntegral(double integral, Integer type, Integer typeId, List<Membermanagement> mList, double price,Integer parseIntproductNums) throws Exception {
        List<Integralrecord> integralrecords = new ArrayList<>();
        Integralrecord integralrecord = new Integralrecord();
        double nowIntegral = 0;
        double nowCountPrice = 0;
        for (Membermanagement memberId : mList) {  //循环当前门店会员列表为
            nowIntegral = memberId.getIntegral();
            nowCountPrice = memberId.getCountPrice();
            if (type == 1) {
                if (integral < 0) { //扣除类积分
                    if ((nowIntegral + integral) >= 0) {
                        memberId.setIntegral(nowIntegral + integral);
//                        memberId.setCountPrice(nowCountPrice + integral);
                        memberId.setPrice(memberId.getPrice().doubleValue()+(price*parseIntproductNums)); //总消费额
                    } else {
                        throw new Exception("可用积分不足！");
                    }
                } else {
                    memberId.setIntegral(nowIntegral + integral);
                    memberId.setCountPrice(nowCountPrice + integral);
                    memberId.setPrice(memberId.getPrice().doubleValue()+(price*parseIntproductNums)); //总消费额
                }
                // type=1 商品积分
                integralrecord.setIntegralType(type.toString());
                integralrecord.setTypeId(typeId.toString());
            } else if (type == 2) {
                if (typeId == 2) { //扣除积分
                    if ((nowIntegral - integral) >= 0) {
                        memberId.setIntegral(nowIntegral - integral);
//                        memberId.setCountPrice(nowCountPrice - integral);
                    } else {
                        throw new Exception("可用积分不足！");
                    }
                } else {
                    memberId.setIntegral(nowIntegral + integral);
                    memberId.setCountPrice(nowCountPrice + integral);
//                    memberId.setPrice(memberId.getPrice()+(price*parseIntproductNums)); //总消费额
                }
                // type=2 行为积分
                integralrecord.setIntegralType(type.toString());
                integralrecord.setOtherTypeId(typeId.toString());
            }
            //更新会员总积分和实际积分
            membermanagementService.updateById(memberId);
            if(type!=2){
                membermanagementController.updateMemberLeave(memberId.getId() + "");
            }

            //添加积分记录
            integralrecord.setIntegral(integral);
            if (type == 2 && typeId == 2) integralrecord.setIntegral(-integral);
            integralrecord.setCreateTime(DateUtil.getTime());
            integralrecord.setMemberid(memberId.getId());
            integralrecord.setDeptid(ShiroKit.getUser().getDeptId());
            integralrecord.setStaffid(ShiroKit.getUser().getId());
            integralrecordService.insert(integralrecord);
            integralrecords.add(integralrecord);
        }
        return integralrecords;
    }

    /**
     * 删除新增积分
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer integralrecordId) {
        integralrecordService.deleteById(integralrecordId);
        return SUCCESS_TIP;
    }

    /**
     * 修改新增积分
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Integralrecord integralrecord) {
        integralrecordService.updateById(integralrecord);
        return SUCCESS_TIP;
    }

    /**
     * 新增积分详情
     */
    @RequestMapping(value = "/detail/{integralrecordId}")
    @ResponseBody
    public Object detail(@PathVariable("integralrecordId") Integer integralrecordId) {
        return integralrecordService.selectById(integralrecordId);
    }

    /**
     * 提交商品订单到T+
     *
     * @param mainSynchronous
     * @return
     * @throws Exception
     */
    public Object synchronousData(MainSynchronous mainSynchronous) throws Exception {
        String s = YongYouAPIUtils.postUrl(YongYouAPIUtils.SALEDELIVERY_CREATE, mainSynchronous.getSynchronousJson());
        mainSynchronous.setSynchronousurl(YongYouAPIUtils.SALEDELIVERY_CREATE);
        mainSynchronous.setCreatedt(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        System.out.println("---" + s);
        if (!"null".equals(s)) {
            JSONObject jsonObject = JSON.parseObject(s);
            mainSynchronous.setStatus(2);
            mainSynchronous.setErrorMssage(jsonObject.getString("message"));
            throw new GunsException(BizExceptionEnum.NUM_ERROR);
        } else {
            mainSynchronous.setStatus(1);
            mainSynchronous.setErrorMssage(s);
        }
        mainSynchronousService.updateById(mainSynchronous);
        return null;
    }

    /**
     * 创建收款单到T+
     *
     * @param departmentCode
     * @param origAmount
     * @param playType
     */
    public void receiveVoucherCreate(String departmentCode, Double origAmount, int playType,String text,boolean IsReceiveFlag,Integer memberId) throws Exception {
        String SettleStyleCode = "";
        String SettleStyleBankAccountName = "";
        switch (playType) {
            case 0:
                SettleStyleCode="994";
                SettleStyleBankAccountName = "刷卡";
                break;
            case 1:
                SettleStyleCode="1";
                SettleStyleBankAccountName = "现金";
                break;
            case 2:
                SettleStyleCode="7";
                SettleStyleBankAccountName = "支付宝";
                break;
            case 3:
                SettleStyleCode="6";
                SettleStyleBankAccountName = "微信";
                break;
            case 4:
                SettleStyleCode="1";
                SettleStyleBankAccountName = "现金";
                break;
            case 5:
                SettleStyleCode="993";
                SettleStyleBankAccountName = "积分抵现";
                break;
        }

        String json =
                "{\n" +
                        "\tdto: {\n" +
                        "\t\tExternalCode: \"" + new Date().getTime() + "\",\n" +
                        "\t\tVoucherDate: \"" + DateUtil.formatDate(new Date(), "yyyy-MM-dd") + "\",\n" +
                        "\t\tPartner: {\n" +
                        "\t\t\tCode: \""+departmentCode+"\"\n" +
                        "\t\t},\n" +
                        "\t\tDepartment: {\n" +
                        "\t\t\tCode: \"" + departmentCode + "\"\n" +
                        "\t\t},\n" +
                        "\t\tCurrency: {\n" +
                        "\t\t\tCode: \"RMB\"\n" +
                        "\t\t},\n" +
                        "\t\tIsReceiveFlag: "+IsReceiveFlag+",\n" +
                        "\t\tExchangeRate: 1,\n" +
                        "\t\tMemo: \""+text+"\",\n" +
                        "\t\tArapMultiSettleDetails: [{\n" +
                        "\t\t\tSettleStyle: {\n" +
                        "\t\t\t\tCode: \""+SettleStyleCode+"\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\tBankAccount: {\n" +
                        "\t\t\t\tName: \""+SettleStyleBankAccountName+"\"\n" +
                        "\t\t\t},\n" +
                        "\t\t\tOrigAmount: " + origAmount + "\n" +
                        "\t\t}],\n" +
                        "\t\tDetails: [{\n" +
                        "\t\t\tVoucherCode: \"SA-2019-02-27-001\",\n" +
                        "\t\t\tVoucherType: {\n" +
                        "\t\t\t\tCode: \"SA04\"\n" +
                        "\t\t\t}\n" +
                        "\t\t}],\n" +
                        "\t\tBusiType: {\n" +
                        "\t\t\tcode: 45\n" +
                        "\t\t},\n" +
                        "VoucherState: {Code: \"01\"}"+
                        "\t}\n" +
                        "}";
        System.out.println("收款单："+json);
        String s = YongYouAPIUtils.postUrl(YongYouAPIUtils.RECEIVEVOUCHER_CREATE, json);
        MainSynchronous mainSynchronous = new MainSynchronous();
        mainSynchronous.setSynchronousurl(YongYouAPIUtils.RECEIVEVOUCHER_CREATE);
        mainSynchronous.setStatus(1);
        mainSynchronous.setErrorMssage(s);
        mainSynchronous.setSynchronousJson(json);
        mainSynchronous.setCreatedt(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        mainSynchronous.setMemberid(memberId);
        mainSynchronousService.insert(mainSynchronous);
        System.out.println("---" + s);
    }
}
