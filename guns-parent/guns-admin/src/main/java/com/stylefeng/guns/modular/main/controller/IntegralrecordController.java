package com.stylefeng.guns.modular.main.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.SuccessTip;
import com.stylefeng.guns.core.common.annotion.BussinessLog;
import com.stylefeng.guns.core.common.exception.BizExceptionEnum;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.support.HttpKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.*;
import com.stylefeng.guns.modular.system.controller.DeptController;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IDeptService;
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

import javax.servlet.http.HttpServletRequest;
import java.sql.Wrapper;
import java.util.*;

/**
 * 新增积分控制器
 *
 * @author fengshuonan
 * @Date 2018-08-14 16:47:26
 */
@Controller
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
        EntityWrapper tWrapper = new EntityWrapper();
        tWrapper.notIn("names","积分清零","积分恢复","积分兑换");
        List<Integralrecordtype> types = integralrecordtypeService.selectList(tWrapper);
        model.addAttribute("type",types);
        return PREFIX + "integralrecord_add.html";
    }

    /**
     * 跳转到修改新增积分
     */
    @RequestMapping("/integralrecord_update/{integralrecordId}")
    public String integralrecordUpdate(@PathVariable Integer integralrecordId, Model model) {
        Integralrecord integralrecord = integralrecordService.selectById(integralrecordId);
        model.addAttribute("item",integralrecord);
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
        monthTime1=format1+"-01";
        monthTime2=format2;

        HttpServletRequest request = HttpKit.getRequest();
       try {
           orderBy = request.getParameter("sort");         //排序字段名称
           desc = request.getParameter("order");       //asc或desc(升序或降序)
       }catch (Exception e){

       }
        //获取deptids
        List<Map<String, Object>> list=( List<Map<String, Object>>) deptController.findDeptLists(deptId.toString());
        String deptIds="";
        for(Map<String, Object> map:list){
            deptIds+=map.get("id")+",";
        }
        deptIds=deptIds.substring(0,deptIds.length()-1);
        Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
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
    public Object add(Double integral, Integer productname, Integer memberId,Integer consumptionNum,String productIds,String productNums,Double play,Integer playType,String verificationcode,String tableNase64Data) throws Exception {
       List<MainSynchronous> synchronousList=new ArrayList<>();
        BaseEntityWrapper<Membermanagement> mWrapper = new BaseEntityWrapper<>();
        mWrapper.eq("id",memberId);
        List<Membermanagement> membermanagements = membermanagementService.selectList(mWrapper);

        if(playType==0){
            //判断验证码是否通过
            BaseEntityWrapper<VerificationCode> verificationCodeBaseEntityWrapper = new BaseEntityWrapper<>();
            verificationCodeBaseEntityWrapper.eq("memberid",memberId);
            VerificationCode verificationCode = verificationCodeService.selectOne(verificationCodeBaseEntityWrapper);
            if(verificationCode==null||!verificationCode.getVerificationcode().equals(verificationcode)){
                throw new GunsException(BizExceptionEnum.VERIFICATIONCODE_ERROR);
            }else {
                verificationCodeService.deleteById(verificationCode.getId());
            }

            Double money = membermanagements.get(0).getMoney();
            if(money<play){
                throw new GunsException(BizExceptionEnum.MONEY_ERROR);
            }
        }
        //更新库存
        String[] split = productIds.split(",");
        String[] productNumsSplit = productNums.split(",");
        int index=0;
        for(String temp:split){
            int parseIntTemp = Integer.parseInt(temp);
            int parseIntproductNums = Integer.parseInt(productNumsSplit[index]);
            //积分添加操作
            List<Integralrecord> integralrecords = insertIntegral(integral,1,parseIntTemp,membermanagements);
            BaseEntityWrapper<Integralrecordtype> typeWrapper = new BaseEntityWrapper<>();
            typeWrapper.eq("id",parseIntTemp);
            Integralrecordtype integralrecordtype = integralrecordtypeService.selectOne(typeWrapper);
            integralrecordtype.setProductnum(integralrecordtype.getProductnum()-parseIntproductNums);//库存减
            integralrecordtype.setUpdatetime(DateUtil.getTime());
            integralrecordtype.setUpdateuserid(ShiroKit.getUser().getId().toString());
            if(integralrecordtype.getProductnum()<0){
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
            //判断商品是否需要进行追销
            if(dueToRemindController.judgeDueToRemind(parseIntTemp)){
                inventoryManagement.setIsDueToRemind(1);
            }
            inventoryManagementService.insert(inventoryManagement);

            //同步数据写入T+库
            String now = DateUtil.format(new Date(), "yyyy-MM-dd");
            int radomInt = new Random().nextInt(999999);
            double baseQuantity =(double)parseIntproductNums;
            //PartnerDTO对象
//        YongYouAPIUtils.postUrl(YongYouAPIUtils.PARTNER_QUERY,"");
            //获取存货编码信息
            String InventoryCode=integralrecordtype.getInventoryCode();
//        YongYouAPIUtils.postUrl(YongYouAPIUtils.INVENTORY_QUERY,"{\"param\":{\"code\":\""+InventoryCode+"\"}}");
            int i = mainSynchronousService.selectCount(null);
            String tableJson="{\n" +
                    "        \"dto\": {\n" +
                    "            \"BusiType\": {\"Code\": \"15\"},\n" +       //15 销售出库，16 销售退库
                    "            \"Warehouse\": {\"Code\": \""+integralrecordtype.getWarehouseCode()+"\"},\n" + //仓库信息。传入的仓库编码信息与T+系统编码一致
                    "            \"VoucherDate\": \""+now+"\",\n" +      //单据日期
                    "            \"Customer\": {\"Code\": \"0010001\"},\n" + //客户，PartnerDTO对象，客户信息
                    "            \"RDRecordDetails\": [{\"BaseQuantity\": "+baseQuantity+", \"Code\": \""+(i+1)+"\", \"Inventory\": {\"Code\": \""+InventoryCode+"\"}}],\n" + //单据明细信
                    "            \"Code\": \""+radomInt +"\",\n" + //单据编码
//        "            \"Partner\": {\"Code\": \"001\"},\n" +
                    "            \"Memo\": \"销售\",\n" + //备注
                    "            \"ExternalCode\": \""+radomInt+"\",\n" + //外部系统数据编号；OpenAPI调用者填写,后台做唯一性检查。用于防止重复提交，和外系统数据对应。
                    "            \"VoucherType\": {\"Code\": \"ST1021\"}\n" + //单据类型。固定值:{Code: "ST1021"},
                    "        }\n" +
                    "    }";
            boolean busiType=false;
            if(integralrecordtype.getProducttype()==0){//赠送出库
                busiType=true;
            }
//            tableJson="{\n" +
//                    "\tdto:{\n" +
//                    "\t\tExternalCode: \""+(i+1)+"\",\n" +
//                    "\t\tVoucherType: {Code: \"ST1021\"},\n" +
//                    "\t\tPartner:{Code:\"LS\"},\n"+
//                    "\t\tVoucherDate: \""+now+"\",\n" +
//                    "\t\tBusiType: {Code: \""+busiType+"\"},\n" +
//                    "\t\tWarehouse: {Code: \""+integralrecordtype.getWarehouseCode()+"\"},\n" +
//                    "\t\tMemo: \"销售\",\n" +
//                    "\t\tRDRecordDetails: [{\n" +
//                    "\t\t\tInvBarCode: \"\",\n" +
//                    "\t\t\tInventory: {Code: \""+InventoryCode+"\"},\n" +
//                    "\t\t\tBaseQuantity: "+baseQuantity+"\n" +
//                    "\t\t}]\n" +
//                    "\t}\n" +
//                    "}";
            Dept dept = deptService.selectById(ShiroKit.getUser().deptId);
            tableJson=
                    "{\n" +
                            "    dto:{\n" +
                            "       VoucherDate: \""+now+"\",\n" +
                            "       ExternalCode:\"\"+(i+1)+\"\",\n" +
                            "       Customer: {Code: \"LS\"}, \n" +
                            "       InvoiceType: {Code: \"00\"},\n" +
                            "       Address: \"新协会员管理系统\",\n" +
                            "       LinkMan: \"新协会员管理系统\",\n" +
                            "       ContactPhone: \"13611111111\",\n" +
                            "       Department :{code: \""+dept.gettPlusDeptCode()+"\"},\n" +
                            "       Memo: \"新协会员管理系统\",\n" +
                            "       IsAutoGenerateSaleOut:true,\n" +
                            "       Warehouse:{code:\""+integralrecordtype.getWarehouseCode()+"\"},\n" +
                            "       IsPresent:"+busiType+"\n" +
                            "       SaleDeliveryDetails: [{\n" +
                            "           Inventory:{Code: \""+InventoryCode+"\"},\n" +
                            "           Unit: {Name:\""+integralrecordtype.getUnitName()+"\"},\n" +
                            "           Quantity: "+baseQuantity+",\n" +
                            "           OrigPrice: "+integralrecordtype.getProductpice()*baseQuantity+",\n" +
                            "           OrigTaxAmount: "+integralrecordtype.getProductpice()*baseQuantity+",\n" +
                            "           DynamicPropertyKeys:[\"priuserdefnvc1\",\"priuserdefdecm1\"],\n" +
                            "           DynamicPropertyValues:[\"sn001\",\"123\"]\n" +
                            "       }]\n" +
                            "    } \n" +
                            "}";
            MainSynchronous mainSynchronous = new MainSynchronous();
            mainSynchronous.setSynchronousJson(tableJson);
            mainSynchronous.setStatus(0);
            mainSynchronousService.insert(mainSynchronous);
            //
//            synchronousData(mainSynchronous);
            synchronousList.add(mainSynchronous);
            index++;
        }
        for(MainSynchronous a:synchronousList){
            synchronousData(a);
        }
        //新增购买小票
        ReceiptsInfo receiptsInfo = new ReceiptsInfo();
        receiptsInfo.setCreateTime(DateUtil.formatDate(new Date(),"yyyy-HH-dd HH:mm:ss"));
        receiptsInfo.setDeptId(ShiroKit.getUser().deptId);
        receiptsInfo.setMemberId(memberId);
        receiptsInfo.setMemberName(membermanagements.get(0).getName());
        receiptsInfo.setMemberPhone(membermanagements.get(0).getPhone());
        receiptsInfo.setPlayMoney(play+"");
        receiptsInfo.setReceiptsBase64Img(tableNase64Data);
        receiptsInfo.insert();
        return SUCCESS_TIP;
    }

    /**
     * 会员积分增加并新增记录
     * @param integral
     * @param type
     * @param typeId
     * @param mList
     * @return
     * @throws Exception
     */
    public List<Integralrecord> insertIntegral(double integral, Integer type, Integer typeId, List<Membermanagement> mList) throws Exception {
        List<Integralrecord> integralrecords = new ArrayList<>();
        Integralrecord integralrecord = new Integralrecord();
        double nowIntegral = 0;
        double nowCountPrice = 0;
        for(Membermanagement memberId : mList){  //循环当前门店会员列表为
            nowIntegral = memberId.getIntegral();
            nowCountPrice = memberId.getCountPrice();
            if(type == 1){
                if(integral < 0){ //扣除类积分
                    if((nowIntegral + integral) >= 0){
                        memberId.setIntegral(nowIntegral + integral);
                    }else {
                        throw new Exception("可用积分不足！");
                    }
                }else {
                    memberId.setIntegral(nowIntegral + integral);
                    memberId.setCountPrice(nowCountPrice + integral);
                }
            }else if(type == 2){
                if(typeId == 2) { //扣除积分
                    if((nowIntegral - integral) >= 0){
                        memberId.setIntegral(nowIntegral - integral);
                    }else {
                        throw new Exception("可用积分不足！");
                    }
                }else {
                    memberId.setIntegral(nowIntegral + integral);
                    memberId.setCountPrice(nowCountPrice + integral);
                }
            }
            //更新会员总积分和实际积分
            membermanagementService.updateById(memberId);
            membermanagementController.updateMemberLeave(memberId.getId()+"");

            if(type == 1){ // type=1 商品积分
                integralrecord.setIntegralType(type.toString());
                integralrecord.setTypeId(typeId.toString());
            }else if(type == 2){ // type=2 行为积分
                integralrecord.setIntegralType(type.toString());
                integralrecord.setOtherTypeId(typeId.toString());
            }
            //添加积分记录
            integralrecord.setIntegral(integral);
            if(type==2&&typeId==2)integralrecord.setIntegral(-integral);
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
     * @param mainSynchronous
     * @return
     * @throws Exception
     */
    public Object  synchronousData(MainSynchronous mainSynchronous) throws Exception {
        String s = YongYouAPIUtils.postUrl(YongYouAPIUtils.SALEDISPATCH_CREATE, mainSynchronous.getSynchronousJson());
        System.out.println("---"+s);
        if(!"null".equals(s)){
            JSONObject jsonObject = JSON.parseObject(s);
            mainSynchronous.setStatus(2);
            mainSynchronous.setErrorMssage(jsonObject.getString("message"));
        }else {
            mainSynchronous.setStatus(1);
        }
        mainSynchronousService.updateById(mainSynchronous);
        return null;
    }
}
