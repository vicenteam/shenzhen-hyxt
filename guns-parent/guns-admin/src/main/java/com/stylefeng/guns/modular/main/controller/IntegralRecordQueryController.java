package com.stylefeng.guns.modular.main.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.modular.main.service.IIntegralrecordService;
import com.stylefeng.guns.modular.main.service.IIntegralrecordtypeService;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.system.model.Integralrecord;
import com.stylefeng.guns.modular.system.model.Integralrecordtype;
import com.stylefeng.guns.modular.system.model.Membermanagement;
import com.stylefeng.guns.modular.system.model.User;
import com.stylefeng.guns.modular.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 积分记录查询控制器
 *
 * @author fengshuonan
 * @Date 2018-08-14 16:47:26
 */
@Controller
@RequestMapping("/integralrecordquery")
public class IntegralRecordQueryController extends BaseController {

    private String PREFIX = "/main/integralrecord/";

    @Autowired
    private IIntegralrecordService integralrecordService;
    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IIntegralrecordtypeService integralrecordtypeService;

    @RequestMapping("")
    public String index(Model model){
        List<Integralrecordtype> types = integralrecordtypeService.selectList(new EntityWrapper<Integralrecordtype>());
        model.addAttribute("type",types);
        BaseEntityWrapper<User> wrapper = new BaseEntityWrapper<>();
        model.addAttribute("users",userService.selectList(wrapper));
        return PREFIX + "integralRecordQuery.html";
    }

    /**
     * 查询积分记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition, String operator, String memberName, String cadId
                        , String integralType, String begindate, String enddate, String memberId) {

        BaseEntityWrapper<Membermanagement> mWrapper = new BaseEntityWrapper<>();
        if(memberId != null && ! memberId.equals("")){ //按直接读卡
            mWrapper.eq("id",memberId);
        }else { //按搜索条件
            if(! StringUtils.isEmpty(cadId)) mWrapper.like("cadID",cadId);
            if(! StringUtils.isEmpty(memberName)) mWrapper.like("name",memberName);
        }
        //会员 memberName、cadId 条件筛选
        List<Membermanagement> membermanagements = membermanagementService.selectList(mWrapper);
        Integer[] mIdArray = new Integer[membermanagements.size()];
        for(int i=0; i<mIdArray.length; i++){
            mIdArray[i] = membermanagements.get(i).getId();
        }

        //操作人 operator 条件筛选
        BaseEntityWrapper<User> uWrapper = new BaseEntityWrapper<>();
        if(! operator.equals("-1")) uWrapper.eq("id",operator);
        List<User> users = userService.selectList(uWrapper);
        Integer[] uIdArray = new Integer[users.size()];
        for(int i=0; i<uIdArray.length; i++){
            uIdArray[i] = users.get(i).getId();
        }

        //把 membermanagement 与 user 条件放入 积分记录表实现条件分页查询
        Page<Integralrecord> page = new PageFactory<Integralrecord>().defaultPage();
        BaseEntityWrapper<Integralrecord> iWrapper = new BaseEntityWrapper<>();
        if(! integralType.equals("-1")) iWrapper.eq("typeId",integralType);
        if(mIdArray.length <= 0) mIdArray = new Integer[]{-1}; //判断数组 <=0 赋予初始值 方便查询
        iWrapper.in("memberid",mIdArray);
        if(uIdArray.length <= 0) uIdArray = new Integer[]{-1}; //判断数组 <=0 赋予初始值 方便查询
        iWrapper.in("staffid",uIdArray);
        if(! StringUtils.isEmpty(begindate) || ! StringUtils.isEmpty(enddate)){
            iWrapper.between("createTime",begindate,enddate);
        }
        iWrapper.orderBy("createTime",false);
        Page<Map<String, Object>> serverPage = integralrecordService.selectMapsPage(page, iWrapper);
        if (serverPage.getRecords().size() >= 0){
            for(Map<String, Object> map : serverPage.getRecords()){
                map.put("typeName",integralrecordtypeService.selectById(map.get("typeId").toString()).getNames()); //获取积分类型名称
                if(map.get("memberid") != null){
                    Membermanagement membermanagement = membermanagementService.selectById(map.get("memberid").toString());
                    map.put("memberName",membermanagement.getName());
                    map.put("memberPhone",membermanagement.getPhone());
                    map.put("membercadid",membermanagement.getCadID());
                }
                if(map.get("staffid") != null){
                    map.put("staffName",userService.selectById(map.get("staffid").toString()).getName());
                }
            }
        }
        return super.packForBT(serverPage);
    }

    @RequestMapping(value = "test")
    @ResponseBody
    public Object shouwMemberInfo(){
        return null;
    }

}
