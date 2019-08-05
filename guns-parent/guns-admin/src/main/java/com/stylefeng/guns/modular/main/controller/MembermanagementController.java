package com.stylefeng.guns.modular.main.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.GunsApplication;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.base.tips.SuccessTip;
import com.stylefeng.guns.core.common.annotion.BussinessLog;
import com.stylefeng.guns.core.common.constant.dictmap.DeptDict;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.main.service.*;
import com.stylefeng.guns.modular.main.service.IBaMedicalService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IDeptService;
import com.stylefeng.guns.modular.system.service.IUserService;
import com.stylefeng.guns.modular.system.utils.IntegralRecordTypeExcel;
import com.stylefeng.guns.modular.system.utils.MemberExcel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 会员管理控制器
 *
 * @author fengshuonan
 * @Date 2018-08-10 16:00:02
 */
@Controller
@Scope("prototype")
@RequestMapping("/membermanagement")
public class MembermanagementController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(MembermanagementController.class);
    private String PREFIX = "/main/membermanagement/";


    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private IMemberCardService memberCardService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDeptService deptService;
    @Autowired
    private IMembershipcardtypeService membershipcardtypeService;
    @Autowired
    private IQiandaoCheckinService qiandaoCheckinService;
    @Autowired
    private IMemberBamedicalService memberBamedicalService;
    @Autowired
    private IBaMedicalService baMedicalService;
    @Autowired
    private IActivityService activityService;
    @Autowired
    private IMemberInactivityService memberInactivityService;
    @Autowired
    private IIntegralrecordService integralrecordService;

    /**
     * 跳转到会员管理首页
     */
    @RequestMapping("")
    public String index(Model model) {
        BaseEntityWrapper<User> deptBaseEntityWrapper = new BaseEntityWrapper<>();
        List list = userService.selectList(deptBaseEntityWrapper);
        model.addAttribute("staffs", list);
        EntityWrapper<Dept> deptBaseEntityWrapper1 = new EntityWrapper<>();
        if (ShiroKit.getUser().getAccount().equals("admin")) {
        } else {
            deptBaseEntityWrapper1.eq("id", ShiroKit.getUser().getDeptId());
        }
        List depts = deptService.selectList(deptBaseEntityWrapper1);
        model.addAttribute("depts", depts);
        return PREFIX + "membermanagement.html";
    }

    /**
     * 跳转到添加会员管理
     */
    @RequestMapping("/membermanagement_add")
    public String membermanagementAdd(Model model) {
        BaseEntityWrapper<Dept> deptBaseEntityWrapper = new BaseEntityWrapper<>();
        List list = userService.selectList(deptBaseEntityWrapper);
        EntityWrapper<BaMedical> memberBamedicalEntityWrapper = new EntityWrapper<>();
        //获取会员等级
        List<Membershipcardtype> list1 = membershipcardtypeService.selectList(new BaseEntityWrapper<Membershipcardtype>());
        List<BaMedical> baMedicals = baMedicalService.selectList(memberBamedicalEntityWrapper);
        model.addAttribute("membershipcardtype", list1);
        model.addAttribute("staffs", list);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < baMedicals.size(); i++) {
            sb.append("<tr>");
            sb.append(" <td style='width: 20px'><input name='baMedicals' type='checkbox'  value='" + baMedicals.get(i - 1).getId() + "'> </td><td style='width: 170px'>" + baMedicals.get(i - 1).getName() + "</td>");
            sb.append(" <td style='width: 20px'><input name='baMedicals' type='checkbox'  value='" + baMedicals.get(i).getId() + "'> </td><td style='width: 170px'>" + baMedicals.get(i).getName() + "</td>");
            sb.append("</tr>");
            i++;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("val", sb.toString());
        model.addAttribute("baMedicals", map);
        return PREFIX + "membermanagement_add.html";
    }

    @RequestMapping("/membermanagement_userPhotoPage")
    public String membermanagement_userPhotoPage(Model model) {

        return PREFIX + "membermanagement_userPhotoPage1.html";
    }

    /**
     * 跳转到修改会员管理
     */
    @RequestMapping("/membermanagement_update/{membermanagementId}")
    public String membermanagementUpdate(@PathVariable Integer membermanagementId, Model model) {
        Membermanagement membermanagement = membermanagementService.selectById(membermanagementId);
        model.addAttribute("item", membermanagement);
        BaseEntityWrapper<Dept> deptBaseEntityWrapper = new BaseEntityWrapper<>();
        List list = userService.selectList(deptBaseEntityWrapper);
        model.addAttribute("staffs", list);
        EntityWrapper<BaMedical> memberBamedicalEntityWrapper = new EntityWrapper<>();
        List<BaMedical> baMedicals = baMedicalService.selectList(memberBamedicalEntityWrapper);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < baMedicals.size(); i++) {
            sb.append("<tr>");
            sb.append(" <td style='width: 20px'><input name='baMedicals' type='checkbox'  value='" + baMedicals.get(i - 1).getId() + "'> </td><td style='width: 170px'>" + baMedicals.get(i - 1).getName() + "</td>");
            sb.append(" <td style='width: 20px'><input name='baMedicals' type='checkbox'  value='" + baMedicals.get(i).getId() + "'> </td><td style='width: 170px'>" + baMedicals.get(i).getName() + "</td>");
            sb.append("</tr>");
            i++;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("val", sb.toString());
        model.addAttribute("baMedicals", map);
        EntityWrapper<MemberBamedical> memberBamedicalEntityWrapper1 = new EntityWrapper<>();
        memberBamedicalEntityWrapper1.eq("memberid", membermanagementId);
        List<MemberBamedical> memberBamedicals = memberBamedicalService.selectList(memberBamedicalEntityWrapper1);
        String userbaMedicals = "";
        for (MemberBamedical ba : memberBamedicals) {
            userbaMedicals += ba.getBamedicalid() + ",";
        }
        LogObjectHolder.me().set(membermanagement);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("val", userbaMedicals);
        //获取会员等级
        List<Membershipcardtype> list1 = membershipcardtypeService.selectList(new BaseEntityWrapper<Membershipcardtype>());
        model.addAttribute("userbaMedicals", map1);
        model.addAttribute("membershipcardtype", list1);
        return PREFIX + "membermanagement_edit.html";
    }

    /**
     * 挂失补卡页面跳转
     *
     * @param membermanagementId
     * @param model
     * @return
     */
    @RequestMapping("/guashiDataBuKa/{membermanagementId}")
    public String guashiDataBuKa(@PathVariable Integer membermanagementId, Model model) {
        Membermanagement membermanagement = membermanagementService.selectById(membermanagementId);
        model.addAttribute("item", membermanagement);
        BaseEntityWrapper<Dept> deptBaseEntityWrapper = new BaseEntityWrapper<>();
        List list = userService.selectList(deptBaseEntityWrapper);
        model.addAttribute("staffs", list);
        return PREFIX + "guashiDataBuKa.html";
    }

    /**
     * 签到记录页面
     *
     * @param membermanagementId
     * @param model
     * @return
     */
    @RequestMapping("/membermanagementcheckHistory/{membermanagementId}")
    public String membermanagementcheckHistory(@PathVariable Integer membermanagementId, Model model) {
        Membermanagement membermanagement = membermanagementService.selectById(membermanagementId);
        model.addAttribute("memberId", membermanagementId);
        return PREFIX + "membermanagementcheckHistory.html";
    }

    /**
     * 获取会员管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String name, String address, String fstatus, String sex, String idcard, String phone, String stafff, String deptid, String province, String city, String district, String memberid, String townshipid) {
        Page<Membermanagement> page = new PageFactory<Membermanagement>().defaultPage();
        EntityWrapper<Membermanagement> baseEntityWrapper = new EntityWrapper<>();
        if (!StringUtils.isEmpty(name)) baseEntityWrapper.eq("name", name);
        if (!StringUtils.isEmpty(address)) baseEntityWrapper.like("address", address);
        if (!StringUtils.isEmpty(fstatus)) baseEntityWrapper.eq("familyStatusID", fstatus);
        if (!StringUtils.isEmpty(sex)) baseEntityWrapper.eq("sex", sex);
        if (!StringUtils.isEmpty(idcard)) baseEntityWrapper.eq("idcard", idcard);
        if (!StringUtils.isEmpty(phone)) baseEntityWrapper.like("phone", phone);
        if (!StringUtils.isEmpty(stafff)) baseEntityWrapper.eq("staffid", stafff);
        if (!StringUtils.isEmpty(deptid)) {
            baseEntityWrapper.eq("deptid", deptid);
        } else {
            if (ShiroKit.getUser().getAccount().equals("admin")) {
//                baseEntityWrapper.eq("deptid", ShiroKit.getUser().getDeptId());
            } else {
                baseEntityWrapper.eq("deptid", ShiroKit.getUser().getDeptId());
            }

        }
        if (!StringUtils.isEmpty(province)) baseEntityWrapper.eq("province", province);
        if (!StringUtils.isEmpty(city)) baseEntityWrapper.eq("city", city);
        if (!StringUtils.isEmpty(district)) baseEntityWrapper.eq("district", district);
        if (!StringUtils.isEmpty(memberid)) baseEntityWrapper.eq("id", memberid);
        if (!StringUtils.isEmpty(townshipid)) baseEntityWrapper.eq("townshipid", townshipid);
        baseEntityWrapper.eq("state", 0);
        Page<Map<String, Object>> mapPage = membermanagementService.selectMapsPage(page, baseEntityWrapper);
        List<Map<String, Object>> records = mapPage.getRecords();
        for (Map<String, Object> map : records) {
            String s = (String) map.get("levelID");
            Integer id = (int) map.get("id");
            Membershipcardtype membershipcardtype = membershipcardtypeService.selectById(s);
            if (membershipcardtype != null) {
                map.put("levelID", membershipcardtype.getCardname());
            }
            BaseEntityWrapper<QiandaoCheckin> qiandaoCheckinBaseEntityWrapper = new BaseEntityWrapper<>();
            qiandaoCheckinBaseEntityWrapper.eq("memberid", id);
            String format = DateUtil.format(new Date(), "yyyy-MM-dd");
            qiandaoCheckinBaseEntityWrapper.like("createtime", format, SqlLike.RIGHT);
            QiandaoCheckin qiandaoCheckin = qiandaoCheckinService.selectOne(qiandaoCheckinBaseEntityWrapper);
            if (qiandaoCheckin != null) {
                if (qiandaoCheckin.getUpdatetime() != null) {
                    map.put("today", qiandaoCheckin.getUpdatetime());
                } else {
                    map.put("today", qiandaoCheckin.getCreatetime());
                }
            }

        }
        return super.packForBT(mapPage);
    }

    /**
     * 新增会员管理
     *
     * @param membermanagement
     * @param cardCode
     * @param baMedicals
     * @param code
     * @param otherMemberId    //关联卡memberId
     * @return
     * @throws Exception
     */
    @BussinessLog(value = "新增会员管理", key = "addMember")
    @RequestMapping(value = "/add")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Object add(Membermanagement membermanagement, String cardCode, String baMedicals, String code
            , String otherMemberId, String levelID) throws Exception {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");//生成关联字符串
        if (StringUtils.isEmpty(code)) throw new Exception("请进行读卡操作！");
        EntityWrapper<Membermanagement> membermanagementEntityWrapper = new EntityWrapper<>();
        membermanagementEntityWrapper.eq("cadID", membermanagement.getCadID());
        if (membermanagementService.selectOne(membermanagementEntityWrapper) != null) {
            BaseEntityWrapper<MemberCard> memberCardBaseEntityWrapper = new BaseEntityWrapper<>();
            memberCardBaseEntityWrapper.eq("code", code);
            MemberCard memberCard = memberCardService.selectOne(memberCardBaseEntityWrapper);
            memberCardService.deleteById(memberCard.getId());
//            throw new Exception("注册用户已存在开卡门店！");
            ErrorTip errorTip = new ErrorTip(202, "该身份证已存在开卡门店！");
            return errorTip;
        }
        membermanagement.setCreateTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        membermanagement.setDeptId("" + ShiroKit.getUser().getDeptId());
        membermanagement.setTownshipid("0");
        //设置会员等级
        Membershipcardtype membershipcardtype = membershipcardtypeService.selectById(levelID);
        if (membershipcardtype != null) {
            membermanagement.setLevelID(membershipcardtype.getId() + "");
//            membermanagement.setCountPrice(membershipcardtype.getShopping());
            membermanagement.setPrice(membershipcardtype.getUpamount());
            membermanagement.setCheckInNum(membershipcardtype.getMoRenkeqiandaonum());
        }

        if (membermanagement != null && StringUtils.isEmpty(membermanagement.getAvatar()))
            membermanagement.setAvatar("-");
        if (!StringUtils.isEmpty(otherMemberId)) {
            Membermanagement otherMember = membermanagementService.selectById(otherMemberId);
            if (StringUtils.isEmpty(otherMember.getRelation())) {
                otherMember.setRelation(uuid); //存入关联字符串
                membermanagementService.updateById(otherMember);
                membermanagement.setRelation(uuid); //存入关联字符串
            } else {
                throw new Exception("关联卡片已存在关联会员！");
            }
        }

        membermanagementService.insert(membermanagement);

        BaseEntityWrapper<MemberCard> memberCardBaseEntityWrapper = new BaseEntityWrapper<>();
//        memberCardBaseEntityWrapper.eq("code", cardCode);
        memberCardBaseEntityWrapper.eq("code", code);
        MemberCard memberCard = memberCardService.selectOne(memberCardBaseEntityWrapper);
        memberCard.setName(membermanagement.getName());
        memberCard.setMemberid(membermanagement.getId());
        memberCardService.updateById(memberCard);

        //保存用户健康信息
        if (!StringUtils.isEmpty(baMedicals)) {
            String[] split = baMedicals.split(",");
            for (String s : split) {
                MemberBamedical memberBamedical = new MemberBamedical();
                memberBamedical.setBamedicalid(Integer.parseInt(s));
                memberBamedical.setMemberid(membermanagement.getId());
                memberBamedicalService.insert(memberBamedical);
            }
        }
        //判断推荐人]
        String introducerId = membermanagement.getIntroducerId();
        if (!StringUtils.isEmpty(introducerId)) {
            //老会员带人积分
            BaseEntityWrapper<Membermanagement> mWrapper = new BaseEntityWrapper<>();
            mWrapper.eq("id", introducerId);
            List<Membermanagement> membermanagementList = membermanagementService.selectList(mWrapper);
            Membershipcardtype membershipcardtype2 = membershipcardtypeService.selectById(membermanagementList.get(0).getLevelID());
            if (null != membershipcardtype2.getNewpoints() && 0 != membershipcardtype2.getNewpoints()) {
                //integralrecordController.insertIntegral(membershipcardtype2.getNewpoints(),2,1, membermanagementList,0,1);
//
                {
                    //----
                    double integral = membershipcardtype2.getNewpoints();
                    Integer type = 2;
                    Integer typeId = 1;
                    List<Membermanagement> mList = membermanagementList;
                    int price = 0;
                    int parseIntproductNums = 1;
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
                                    memberIdo.setPrice(memberIdo.getPrice().doubleValue() + (price * parseIntproductNums)); //总消费额
                                } else {
                                    throw new Exception("可用积分不足！");
                                }
                            } else {
                                memberIdo.setIntegral(nowIntegral + integral);
                                memberIdo.setCountPrice(nowCountPrice + integral);
                                memberIdo.setPrice(memberIdo.getPrice().doubleValue() + (price * parseIntproductNums)); //总消费额
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
                        if (type != 2) {
                            updateMemberLeave(memberIdo.getId() + "");
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
            }

            //获取推荐活动 被推荐活动
            BaseEntityWrapper<Activity> baseEntityWrapper = new BaseEntityWrapper<Activity>();
            baseEntityWrapper.eq("ruleexpression", 3);
            List<Activity> list1 = activityService.selectList(baseEntityWrapper);//推荐人活动
            baseEntityWrapper = new BaseEntityWrapper<Activity>();
            baseEntityWrapper.eq("ruleexpression", 4);
            List<Activity> list2 = activityService.selectList(baseEntityWrapper);//被推荐人活动
            //分组推荐人与被推荐人

            //推荐人写入推荐人活动领取次数
            list1.forEach(a -> {
                MemberInactivity memberInactivity = new MemberInactivity();
                memberInactivity.setActivityId(a.getId());
                memberInactivity.setMemberId(membermanagementList.get(0).getId());
                memberInactivity.setDeptId(ShiroKit.getUser().getDeptId());
                memberInactivity.setCreateDt(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                memberInactivityService.insert(memberInactivity);
            });
            //被推荐人写入被推荐人活动领取次数
            list2.forEach(a -> {
                MemberInactivity memberInactivity = new MemberInactivity();
                memberInactivity.setActivityId(a.getId());
                memberInactivity.setMemberId(membermanagement.getId());
                memberInactivity.setCreateDt(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
                memberInactivity.setDeptId(ShiroKit.getUser().getDeptId());
                memberInactivityService.insert(memberInactivity);
            });
        }
        return SUCCESS_TIP;
    }

    /**
     * 删除会员管理
     */
    @BussinessLog(value = "删除会员管理", key = "deleteMember")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer membermanagementId, @RequestParam int type) {
        if (type == 0) {
            Membermanagement membermanagement = membermanagementService.selectById(membermanagementId);
            membermanagement.setState(1);
            membermanagementService.updateById(membermanagement);
        } else if (type == 1) {
            EntityWrapper<MemberCard> memberCardEntityWrapper = new EntityWrapper<>();
            memberCardEntityWrapper.eq("memberId", membermanagementId);
            memberCardService.delete(memberCardEntityWrapper);
            membermanagementService.deleteById(membermanagementId);
        }
        return SUCCESS_TIP;
    }

    /**
     * 补卡操作
     *
     * @param memberId
     * @param cardID
     * @return
     */
    @BussinessLog(value = "补卡操作", key = "bukaMember")
    @RequestMapping(value = "/buka")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Object buka(String memberId, String cardID, String cadID) {
        BaseEntityWrapper<MemberCard> memberCardBaseEntityWrapper = new BaseEntityWrapper<>();
        memberCardBaseEntityWrapper.eq("memberid", memberId);
        MemberCard memberCard = memberCardService.selectOne(memberCardBaseEntityWrapper);
        if (memberCard != null && !StringUtils.isEmpty(cardID)) {
            memberCard.setCode(cardID);
            memberCardService.updateById(memberCard);
//            Membermanagement membermanagement = new Membermanagement();
            Membermanagement membermanagement = membermanagementService.selectById(memberId);
//            membermanagement.setId(Integer.parseInt(memberId));
            membermanagement.setCadID(cadID); //会员身份证id
            membermanagementService.updateById(membermanagement);
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改会员管理
     */
    @BussinessLog(value = "修改会员信息", key = "updateMember")
    @RequestMapping(value = "/update")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Object update(Membermanagement membermanagement, String baMedicals) {
        Membermanagement membermanagement1 = membermanagementService.selectById(membermanagement.getId());
        Membershipcardtype membershipcardtype = membershipcardtypeService.selectById(membermanagement.getLevelID());
        logger.info("修改会员等级+Leave" + membermanagement1.getName() + " 原等级id：" + membermanagement1.getLevelID() + " 修改后等级Id:" + membershipcardtype.getId());
        if (membershipcardtype != null) {
//            membermanagement.setCountPrice(membershipcardtype.getShopping());
            membermanagement.setPrice(membershipcardtype.getUpamount());
        }
        membermanagement.setIntegral(membermanagement1.getIntegral());
        membermanagementService.updateById(membermanagement);
        //删除历史健康记录
        EntityWrapper<MemberBamedical> memberBamedicalEntityWrapper = new EntityWrapper<>();
        memberBamedicalEntityWrapper.eq("memberid", membermanagement.getId());
        memberBamedicalService.delete(memberBamedicalEntityWrapper);
        //保存用户健康信息
        if (!StringUtils.isEmpty(baMedicals)) {
            String[] split = baMedicals.split(",");
            for (String s : split) {
                MemberBamedical memberBamedical = new MemberBamedical();
                memberBamedical.setMemberid(membermanagement.getId());
                memberBamedical.setBamedicalid(Integer.parseInt(s));
                memberBamedicalService.insert(memberBamedical);
            }
        }

        updateMemberInfo(membermanagement);
        return SUCCESS_TIP;
    }

    /**
     * 会员管理详情
     */
    @RequestMapping(value = "/detail/{membermanagementId}")
    @ResponseBody
    public Object detail(@PathVariable("membermanagementId") Integer membermanagementId) {
        return membermanagementService.selectById(membermanagementId);
    }

    @RequestMapping(value = "/getXieKaVal")
    @ResponseBody
    public Object getXieKaVal(String code) throws Exception {
        return getXieKaValInfo(code);
    }

    @RequestMapping(value = "/getUserInfo")
    @ResponseBody
    public Object getUserInfo(String value) {
        BaseEntityWrapper<MemberCard> memberCardBaseEntityWrapper = new BaseEntityWrapper<>();
        memberCardBaseEntityWrapper.eq("code", value);
        MemberCard memberCard = memberCardService.selectOne(memberCardBaseEntityWrapper);
        //卡片是否锁定
        if (memberCard != null && memberCard.getMemberid() != null) {
            Integer memberid = memberCard.getMemberid();
            Membermanagement membermanagement = membermanagementService.selectById(memberid);
            if (membermanagement != null) {
                String townshipid = membermanagement.getTownshipid();
                if (townshipid.equals("1")) {
                    return "202";
                }
            }
        } else if (memberCard != null && memberCard.getMemberid() == null) {
            memberCard = null;
        }
        return memberCard;
    }

    public String getXieKaValInfo(String code) throws Exception {
        String chars = "ABCDEF1234567890";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append(chars.charAt((int) (Math.random() * 16)));
        }
        BaseEntityWrapper<MemberCard> memberCardBaseEntityWrapper = new BaseEntityWrapper<>();
//        memberCardBaseEntityWrapper.eq("code", sb.toString());
        memberCardBaseEntityWrapper.eq("code", code);
        MemberCard memberCard1 = memberCardService.selectOne(memberCardBaseEntityWrapper);
        if (memberCard1 != null && memberCard1.getMemberid() != null) {
            throw new Exception("失败");
//            getXieKaValInfo(code);
        } else if (memberCard1 == null) {
            MemberCard memberCard = new MemberCard();
//        memberCard.setCode(sb.toString());
            memberCard.setCode(code);
            memberCard.setCreatetime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            memberCard.setDeptid(ShiroKit.getUser().getDeptId());
            memberCardService.insert(memberCard);
        }

        return sb.toString();
    }


    /**
     * 通过id获取用户信息
     */
    @RequestMapping("getMemberInfo")
    @ResponseBody
    public Object selectMemberInfo(Integer memberId) {
        Membermanagement m = membermanagementService.selectById(memberId);
        Membershipcardtype ms = membershipcardtypeService.selectById(m.getLevelID());
        Map<String, Object> memberinfo = new HashMap<>();
        memberinfo.put("cadID", m.getCadID());
        memberinfo.put("name", m.getName());
        memberinfo.put("id", m.getId());
        memberinfo.put("phone", m.getPhone());
        memberinfo.put("address", m.getAddress());
        memberinfo.put("integral", m.getIntegral());
        memberinfo.put("countPrice", m.getCountPrice());
        memberinfo.put("levelID", ms.getCardname());
        memberinfo.put("offNum", ms.getShoppingnew());
        return memberinfo;
    }

    @RequestMapping("/openintroducer/{membermanagementId}")
    public String openintroducerdata(@PathVariable Integer membermanagementId, Model model) {
        model.addAttribute("memberId", membermanagementId);
        Membermanagement membermanagement = membermanagementService.selectById(membermanagementId);
        model.addAttribute("memberName", membermanagement.getName());
        return PREFIX + "openintroducer.html";
    }

    @RequestMapping(value = "/openintroducerdata/{id}")
    @ResponseBody
    public Object openintroducerdata(String name, @PathVariable Integer id) {
        Page<Membermanagement> page = new PageFactory<Membermanagement>().defaultPage();
        EntityWrapper<Membermanagement> baseEntityWrapper = new EntityWrapper<>();
        baseEntityWrapper.eq("introducerId", id);
        Page<Map<String, Object>> mapPage = membermanagementService.selectMapsPage(page, baseEntityWrapper);
        List<Map<String, Object>> records = mapPage.getRecords();
        for (Map<String, Object> map : records) {
            String s = (String) map.get("levelID");
            Membershipcardtype membershipcardtype = membershipcardtypeService.selectById(s);
            if (membershipcardtype != null && membershipcardtype.getUpamount() == 0) {
                //获取总签到场次次数
                BaseEntityWrapper<QiandaoCheckin> qiandaoCheckinBaseEntityWrapper = new BaseEntityWrapper<>();
                qiandaoCheckinBaseEntityWrapper.eq("memberid", map.get("id"));
                qiandaoCheckinBaseEntityWrapper.isNotNull("updatetime");
                int i = qiandaoCheckinService.selectCount(qiandaoCheckinBaseEntityWrapper);
                if ((membershipcardtype.getCheckleavenum() - i) == 0) {
                    map.put("levelID", membershipcardtype.getCardname());
                } else {
                    map.put("levelID", "<span style='color:red;'>还差" + (membershipcardtype.getCheckleavenum() - i) + "次签到成为普通会员</span>");
                }
                map.put("count", i);
            } else if (membershipcardtype != null && membershipcardtype.getUpamount() != 0) {
                map.put("levelID", membershipcardtype.getCardname());
                //获取总签到场次次数
                BaseEntityWrapper<QiandaoCheckin> qiandaoCheckinBaseEntityWrapper = new BaseEntityWrapper<>();
                qiandaoCheckinBaseEntityWrapper.eq("memberid", map.get("id"));
                int i = qiandaoCheckinService.selectCount(qiandaoCheckinBaseEntityWrapper);
                map.put("count", i);

            }

        }
        //设置推荐的人
        Membermanagement membermanagement = membermanagementService.selectById(id);
        //获取当前用户介绍人
        if (membermanagement != null && membermanagement.getIntroducerId() != null && page.getOffset() == 0) {
            //查询相关信息
            Membermanagement top = membermanagementService.selectById(membermanagement.getIntroducerId());
            //添加到page结果集中
            if (top != null) {
                List<Map<String, Object>> mapList = mapPage.getRecords();
                Map<String, Object> map = new HashMap<>();
                map.put("id", top.getId());
                map.put("name", "<span style='color:red;'>推荐我的人-" + top.getName() + "</span>");
                //获取会员等级
                String s = (String) top.getLevelID();
                Membershipcardtype membershipcardtype = membershipcardtypeService.selectById(s);
                if (membershipcardtype != null) {
                    map.put("levelID", membershipcardtype.getCardname());

                }
                map.put("integral", top.getIntegral());
                //获取总签到场次次数
                BaseEntityWrapper<QiandaoCheckin> qiandaoCheckinBaseEntityWrapper = new BaseEntityWrapper<>();
                qiandaoCheckinBaseEntityWrapper.eq("memberid", top.getId());
                qiandaoCheckinBaseEntityWrapper.isNotNull("updatetime");
                int i = qiandaoCheckinService.selectCount(qiandaoCheckinBaseEntityWrapper);
                map.put("count", i);
                mapList.add(0, map);
            }
        }


        return super.packForBT(mapPage);
    }

    /**
     * 进行挂失
     *
     * @param model
     * @return
     */

    @RequestMapping("/guashi")
    public String guashi(Model model) {
        BaseEntityWrapper<User> deptBaseEntityWrapper = new BaseEntityWrapper<>();
        List list = userService.selectList(deptBaseEntityWrapper);
        model.addAttribute("staffs", list);
        EntityWrapper<Dept> deptBaseEntityWrapper1 = new EntityWrapper<>();
        if (ShiroKit.getUser().getAccount().equals("admin")) {
        } else {
            deptBaseEntityWrapper1.eq("id", ShiroKit.getUser().getDeptId());
        }
        List depts = deptService.selectList(deptBaseEntityWrapper1);
        model.addAttribute("depts", depts);
        return PREFIX + "guashi.html";
    }

    @BussinessLog(value = "进行挂失", key = "guashiData")
    @RequestMapping("/guashiData")
    @ResponseBody
    public Object guashiData(String memberId) {
        Membermanagement m = membermanagementService.selectById(memberId);
        m.setTownshipid("1");
        membermanagementService.updateById(m);
        return SUCCESS_TIP;
    }

    @BussinessLog(value = "解除挂失", key = "guashiData1")
    @RequestMapping("/guashiData1")
    @ResponseBody
    public Object guashiData1(String memberId) {
        Membermanagement m = membermanagementService.selectById(memberId);
        m.setTownshipid("0");
        membermanagementService.updateById(m);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户等级
     *
     * @param memberId
     */
    public synchronized void updateMemberLeave(String memberId) {
        Membermanagement membermanagement = membermanagementService.selectById(memberId);
        Double price = membermanagement.getPrice();
        BaseEntityWrapper<Membershipcardtype> membershipcardtypeBaseEntityWrapper = new BaseEntityWrapper<>();
        membershipcardtypeBaseEntityWrapper.orderBy("upamount", false);
        List<Membershipcardtype> list = membershipcardtypeService.selectList(membershipcardtypeBaseEntityWrapper);
        for (Membershipcardtype membershipcardtype : list) {
            System.out.println(price + "-----");
            System.out.println(membershipcardtype.getUpamount());
            if (price.doubleValue() >= membershipcardtype.getUpamount().doubleValue()) {
                logger.info("修改会员等级+Leave" + membermanagement.getName() + " 原等级id：" + membermanagement.getLevelID() + " 总消费金额：" + membermanagement.getPrice() + "  修改后等级Id:" + membershipcardtype.getId());
                membermanagement.setLevelID(membershipcardtype.getId() + "");
//                membermanagement.setPrice(membershipcardtype.getUpamount());
                System.out.println(membershipcardtype.getId());
                membermanagementService.updateById(membermanagement);
                break;
            }
        }
        updateMemberInfo(membermanagement);
    }

    @BussinessLog(value = "会员资料导出", key = "export_excel")
    @RequestMapping("export_excel")
    public void export(HttpServletResponse response, String name, String address, String fstatus, String sex, String idcard, String phone, String stafff
            , String deptid, String province, String city, String district, String memberid, String townshipid) throws Exception {
        List<MemberExcel> memberExcels = new ArrayList<>();
        EntityWrapper<Membermanagement> baseEntityWrapper = new EntityWrapper<>();
        if (!StringUtils.isEmpty(name)) baseEntityWrapper.eq("name", name);
        if (!StringUtils.isEmpty(address)) baseEntityWrapper.like("address", address);
        if (!StringUtils.isEmpty(fstatus)) baseEntityWrapper.eq("familyStatusID", fstatus);
        if (!StringUtils.isEmpty(sex)) baseEntityWrapper.eq("sex", sex);
        if (!StringUtils.isEmpty(idcard)) baseEntityWrapper.eq("idcard", idcard);
        if (!StringUtils.isEmpty(phone)) baseEntityWrapper.like("phone", phone);
        if (!StringUtils.isEmpty(stafff)) baseEntityWrapper.eq("staffid", stafff);
        if (!StringUtils.isEmpty(deptid)) {
            baseEntityWrapper.eq("deptid", deptid);
        } else {
            if (ShiroKit.getUser().getAccount().equals("admin")) {

            } else {
                baseEntityWrapper.eq("deptid", ShiroKit.getUser().getDeptId());
            }
        }
        if (!StringUtils.isEmpty(province)) baseEntityWrapper.eq("province", province);
        if (!StringUtils.isEmpty(city)) baseEntityWrapper.eq("city", city);
        if (!StringUtils.isEmpty(district)) baseEntityWrapper.eq("district", district);
        if (!StringUtils.isEmpty(memberid)) baseEntityWrapper.eq("id", memberid);
        if (!StringUtils.isEmpty(townshipid)) baseEntityWrapper.eq("townshipid", townshipid);
//        baseEntityWrapper.eq("state", 0);
        List<Membermanagement> membermanagements = membermanagementService.selectList(baseEntityWrapper);
        for (Membermanagement m : membermanagements) {
            Map<String, Object> mMap = new LinkedHashMap<>();
            mMap.put("mName", m.getName());
            mMap.put("mSex", m.getSex());
            mMap.put("mCadID", m.getCadID());
            mMap.put("mPhone", m.getPhone());
            mMap.put("mAddress", m.getAddress());
            mMap.put("mIntegral", m.getIntegral());
            mMap.put("mCountPrice", m.getCountPrice());
            mMap.put("mIsoldsociety", m.getIsoldsociety());
            mMap.put("mLevel", membershipcardtypeService.selectById(m.getLevelID()).getCardname());
            mMap.put("mCreateDt", m.getCreateTime());
            mMap.put("mDeptName", deptService.selectById(m.getDeptId()) == null ? "" : deptService.selectById(m.getDeptId()).getFullname());
            mMap.put("remak",m.getRemarks());
            //获取病史
            EntityWrapper<MemberBamedical> wrapper = new EntityWrapper<>();
            wrapper.eq("memberid",m.getId());
            List<MemberBamedical> memberBamedicals = memberBamedicalService.selectList(wrapper);
            for(MemberBamedical ba:memberBamedicals){
                BaMedical baMedical = baMedicalService.selectById(ba.getBamedicalid());
                mMap.put("bs",mMap.get("bs")==null?mMap.get("bs").toString():mMap.get("bs").toString()+baMedical.getName()+",");
            }
            MemberExcel memberExcel = JSON.parseObject(JSON.toJSONString(mMap), new TypeReference<MemberExcel>() {
            });
            memberExcels.add(memberExcel);
        }
        ExportParams params = new ExportParams();
        Workbook workbook = ExcelExportUtil.exportExcel(params, MemberExcel.class, memberExcels);
        response.setHeader("content-Type", "application/vnc.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("会员信息导出", "UTF-8") + ".xls");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            memberExcels.clear();
            outputStream.close();
        }
    }

    @RequestMapping("import_excel")
    public String importExcelViews() {
        return PREFIX + "excel_import.html";
    }

    @BussinessLog(value = "会员客户资料导入", key = "importE")
    @RequestMapping("/importE")
    @ResponseBody
    public Object importExcel(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        String message = "";
        Integer total = 0;
        Integer nowNum = 0;
        JSONObject resJson = new JSONObject();
        try {
            List<MemberExcel> excelUpload = ExcelImportUtil.importExcel(file.getInputStream(), MemberExcel.class, params);
//            System.out.println(JSON.toJSONString(excelUpload));
            Membermanagement membermanagement = new Membermanagement();
            BaseEntityWrapper<Membershipcardtype> typeWrapper = new BaseEntityWrapper<>();
            typeWrapper.eq("deptid", ShiroKit.getUser().getDeptId());
            typeWrapper.eq("upamount", "0");
            Membershipcardtype membershipcardtype = membershipcardtypeService.selectOne(typeWrapper);
            for (MemberExcel excelParams : excelUpload) {
                membermanagement.setName(excelParams.getmName());
                membermanagement.setCadID(excelParams.getmCadID());
                membermanagement.setSex(excelParams.getmSex());
                membermanagement.setPhone(excelParams.getmPhone());
                membermanagement.setAddress(excelParams.getmAddress());
//                membermanagement.setIntegral(excelParams.getmIntegral());
                membermanagement.setLevelID(membershipcardtype.getId().toString());
                membermanagement.setDeptId(ShiroKit.getUser().getDeptId().toString());
                membermanagement.setCreateTime(DateUtil.getTime());
                membermanagement.setIsoldsociety(excelParams.getmIsoldsociety());
//                membermanagement.setCountPrice(excelParams.getmCountPrice());
                membermanagementService.insert(membermanagement);
                total += 1;
            }
            if (total == excelUpload.size()) {
                message = "导入成功,共" + total + "条";
            } else if (total == -1) {
                message = "客户编号已存在,在第" + (nowNum + 1) + "条错误,导入失败！";
            } else {
                message = "导入失败，第" + (total + 1) + "条错误!";
            }
            resJson.put("msg", message);
        } catch (Exception e) {
            message = "导入失败，第" + (total + 1) + "条错误!";
            resJson.put("msg", message);
            e.printStackTrace();
        }
        return resJson;
    }

    @BussinessLog(value = "积分赠送", key = "jifenzengsong")
    @RequestMapping("/jifenzengsong")
    @ResponseBody
    public Object jifenzengsong(String id, Double jifenNum) throws Exception {
        BaseEntityWrapper<Membermanagement> wrapper = new BaseEntityWrapper<>();
        wrapper.eq("id", id);
        List<Membermanagement> ms = membermanagementService.selectList(wrapper);
        //积分添加操作
        //integralrecordController.insertIntegral(jifenNum,2,10,ms,0,1);
        {
            //----
            double integral = jifenNum;
            Integer type = 2;
            Integer typeId = 4;
            List<Membermanagement> mList = ms;
            int price = 0;
            int parseIntproductNums = 1;
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
                            memberIdo.setPrice(memberIdo.getPrice().doubleValue() + (price * parseIntproductNums)); //总消费额
                        } else {
                            throw new Exception("可用积分不足！");
                        }
                    } else {
                        memberIdo.setIntegral(nowIntegral + integral);
                        memberIdo.setCountPrice(nowCountPrice + integral);
                        memberIdo.setPrice(memberIdo.getPrice().doubleValue() + (price * parseIntproductNums)); //总消费额
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
                if (type != 2) {
                    updateMemberLeave(memberIdo.getId() + "");
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

    @BussinessLog(value = "可签到积分次数赠送", key = "jifencishuzengsong")
    @RequestMapping("/jifencishuzengsong")
    @ResponseBody
    public Object jifencishuzengsong(String id, int jifenNum) throws Exception {
        BaseEntityWrapper<Membermanagement> wrapper = new BaseEntityWrapper<>();
        wrapper.eq("id", id);
        List<Membermanagement> ms = membermanagementService.selectList(wrapper);
        if (ms.size() == 1) {
            Membermanagement membermanagement = ms.get(0);
            membermanagement.setCheckInNum(membermanagement.getCheckInNum() == null ? (0 + jifenNum) : membermanagement.getCheckInNum() + jifenNum);
            membermanagement.updateById();
        }
        return SUCCESS_TIP;
    }

    @BussinessLog(value = "门店可签到积分次数赠送", key = "deptjifencishuzengsong")
    @RequestMapping("/deptjifencishuzengsong")
    @ResponseBody
    public Object deptjifencishuzengsong(String id, int jifenNum) throws Exception {
        EntityWrapper<Dept> deptEntityWrapper = new EntityWrapper<>();
        if (!com.alibaba.druid.util.StringUtils.isEmpty(id)) deptEntityWrapper.eq("id", id);
        List<Dept> depts = deptService.selectList(deptEntityWrapper);
        depts.forEach(a -> {
            BaseEntityWrapper<Membermanagement> wrapper = new BaseEntityWrapper<>();
            wrapper.eq("deptId", a.getId());
            List<Membermanagement> ms = membermanagementService.selectList(wrapper);
            ms.forEach(b -> {
                Membermanagement membermanagement = b;
                membermanagement.setCheckInNum(membermanagement.getCheckInNum() == null ? (0 + jifenNum) : membermanagement.getCheckInNum() + jifenNum);
                membermanagement.updateById();
            });
        });

        return SUCCESS_TIP;
    }

    @BussinessLog(value = "积分清零", key = "jifenqingchu")
    @RequestMapping("/jifenqingchu")
    @ResponseBody
    public Object jifenqingchu(String id) throws Exception {
//        Membermanagement membermanagement = membermanagementService.selectById(id);
//        membermanagement.setIntegral(0.0);
//        membermanagementService.updateById(membermanagement);

        BaseEntityWrapper<Membermanagement> wrapper = new BaseEntityWrapper<>();
        wrapper.eq("id", id);
        List<Membermanagement> ms = membermanagementService.selectList(wrapper);
        //积分添加操作
        //integralrecordController.insertIntegral(jifenNum,2,10,ms,0,1);
        {
            //----
            double integral = ms.get(0).getIntegral();
            System.out.println("----" + ms.get(0).getIntegral());
            Integer type = 2;
            Integer typeId = 5;
            List<Membermanagement> mList = ms;
            int price = 0;
            int parseIntproductNums = 1;
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
                            memberIdo.setPrice(memberIdo.getPrice().doubleValue() + (price * parseIntproductNums)); //总消费额
                        } else {
                            throw new Exception("可用积分不足！");
                        }
                    } else {
                        memberIdo.setIntegral(nowIntegral + integral);
                        memberIdo.setCountPrice(nowCountPrice + integral);
                        memberIdo.setPrice(memberIdo.getPrice().doubleValue() + (price * parseIntproductNums)); //总消费额
                    }
                    // type=1 商品积分
                    integralrecord.setIntegralType(type.toString());
                    integralrecord.setTypeId(typeId.toString());
                } else if (type == 2) {
                    if (2 == 2) { //扣除积分
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
                if (type != 2) {
                    updateMemberLeave(memberIdo.getId() + "");
                }

                //添加积分记录
                integralrecord.setIntegral(integral);
                if (type == 2 && 2 == 2) integralrecord.setIntegral(-integral);
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

    public void updateMemberInfo(Membermanagement membermanagement) {
        logger.info("deptId:" + membermanagement.getDeptId() + "-userId:" + membermanagement.getId() + "json:" + JSON.toJSONString(membermanagement));
    }

    @RequestMapping("/AllMembers")
    public String AllMembers(Model model) {
        return PREFIX + "AllMembers.html";
    }

    @RequestMapping("/AllMembersHeadData")
    @ResponseBody
    public Object AllMembersHeadData() {
        EntityWrapper<Dept> wrapper = new EntityWrapper<>();
        wrapper.ne("id",53);
        List<Dept> depts = this.deptService.selectList(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Dept de : depts) {
            Map<String, Object> map = new HashMap<>();
            map.put("field", "deptId" + de.getId());
            map.put("title", de.getFullname());
            map.put("minWidth", "100");
            map.put("align:", "center");
            result.add(map);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("field", "leaveName");
        map.put("title", "---------------");
        map.put("minWidth", "100");
        map.put("align:", "center");
        result.add(0, map);

        map = new HashMap<>();
        map.put("field", "deptCount");
        map.put("title", "总门店会员数");
        map.put("minWidth", "100");
        map.put("align:", "center");
        result.add( map);
        return result;
    }

    @RequestMapping("/leaveData")
    @ResponseBody
    public Object leaveData() {
        //{ "top":"","deptId53":"20" }
        EntityWrapper<Membershipcardtype> membershipcardtypeEntityWrapper = new EntityWrapper<>();
        membershipcardtypeEntityWrapper.groupBy("cardname");
        membershipcardtypeEntityWrapper.orderBy("upamount",true);
        List<Membershipcardtype> membershipcardtypes = membershipcardtypeService.selectList(membershipcardtypeEntityWrapper);
        EntityWrapper<Dept> wrapper = new EntityWrapper<>();
        wrapper.ne("id",53);
        List<Dept> depts = this.deptService.selectList(wrapper);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Membershipcardtype me : membershipcardtypes) {
            Map<String, Object> map = new HashMap<>();
            int deptCount=0;
            map.put("leaveName", me.getCardname());
            for (Dept dept : depts) {
                membershipcardtypeEntityWrapper = new EntityWrapper<>();
                membershipcardtypeEntityWrapper.eq("deptId", dept.getId());
                membershipcardtypeEntityWrapper.eq("cardname", me.getCardname());
                Membershipcardtype membershipcardtype = membershipcardtypeService.selectOne(membershipcardtypeEntityWrapper);
                if (membershipcardtype != null) {
                    //获取当前等级当前门店会员数量
                    EntityWrapper<Membermanagement> membermanagementEntityWrapper = new EntityWrapper<>();
                    membermanagementEntityWrapper.eq("levelID", membershipcardtype.getId());
                    int i = membermanagementService.selectCount(membermanagementEntityWrapper);
                    deptCount+=i;
                    map.put("deptId" + dept.getId(), i);
                }
            }

            map.put("deptCount", deptCount);
            list.add(map);
        }
        Map<String, Object> map = new HashMap<>();//统计各个门店总会员数
        map.put("leaveName","总计：");
        for (Dept dept : depts) {
            EntityWrapper<Membermanagement> membermanagementEntityWrapper = new EntityWrapper<>();
            membermanagementEntityWrapper.eq("deptId",dept.getId());
            int i = membermanagementService.selectCount(membermanagementEntityWrapper);
            map.put("deptId" + dept.getId(), i);
        }
        map.put("deptCount",membermanagementService.selectCount(null));
        list.add(map);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("count", membershipcardtypes.size());
        result.put("data", list);
        result.put("msg", "");

        return result;
    }
}
