package com.stylefeng.guns.modular.main.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.page.PageInfoBT;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.main.service.IIntegralrecordService;
import com.stylefeng.guns.modular.main.service.IIntegralrecordtypeService;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.system.model.*;
import com.stylefeng.guns.modular.system.service.IDeptService;
import com.stylefeng.guns.modular.system.service.IUserService;
import com.stylefeng.guns.modular.system.utils.MemberExcel;
import com.stylefeng.guns.modular.system.utils.MemberJifenExcel;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 积分记录查询控制器
 *
 * @author fengshuonan
 * @Date 2018-08-14 16:47:26
 */
@Controller
@Scope("prototype")
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
    @Autowired
    private IDeptService deptService;

    @RequestMapping("")
    public String index(Model model){
        BaseEntityWrapper<Integralrecord> iWrapper = new BaseEntityWrapper<>();
        iWrapper.groupBy("integralType");
        List<Integralrecord> types1 = integralrecordService.selectList(iWrapper);
        model.addAttribute("types1",types1);
        BaseEntityWrapper<User> wrapper = new BaseEntityWrapper<>();
        model.addAttribute("users",userService.selectList(wrapper));
        return PREFIX + "integralRecordQuery.html";
    }

    @RequestMapping("findIntegralType")
    @ResponseBody
    public Object findIntegralType(String type){
        BaseEntityWrapper<Integralrecord> iWrapper = new BaseEntityWrapper<>();
        iWrapper.eq("integralType",type);
        iWrapper.lt("otherTypeId",10);
        if(type.equals("1")) iWrapper.groupBy("typeId");
        if(type.equals("2")) iWrapper.groupBy("otherTypeId");
        List<Map<String,String>> types2 = integralrecordService.selectMaps(iWrapper);
        types2.forEach(e->{
            if(type.equals("1")){
                Integralrecordtype integralrecordtype = new Integralrecordtype();
                integralrecordtype.setId(Integer.parseInt(e.get("typeId")));
                Integralrecordtype integralrecordtype1 = integralrecordtypeService.selectById(integralrecordtype);
                e.put("name", integralrecordtype1.getProductname());
            }else{
                if(e.get("otherTypeId").equals("0")){
                    e.put("name","签到积分");
                }else if(e.get("otherTypeId").equals("1")){
                    e.put("name","带人积分");
                }else if(e.get("otherTypeId").equals("2")){
                    e.put("name","活动兑换积分");
                }else if(e.get("otherTypeId").equals("3")){
                    e.put("name","生日积分");
                }else if(e.get("otherTypeId").equals("4")){
                    e.put("name","积分赠送");
                }else if(e.get("otherTypeId").equals("5")){
                    e.put("name","积分清零");
                }
            }
        });
        if(type.equals("1")){
            Map<String ,String> map=new HashMap<>();
            map.put("name","全部");
            types2.add(0,map);
        }

        return types2;
    }

    /**
     * 查询积分记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition, String operator, String memberName, String cadId
                        , String type, String integralType, String begindate, String enddate, String memberId) {

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
        iWrapper.eq("integralType",type);
        if(type.equals("1")){
            if(!"undefined".equals(integralType)){
                iWrapper.eq("typeId",integralType);
            }
        }else if(type.equals("2")){
            iWrapper.eq("otherTypeId",integralType);
        }
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
                if(type.equals("1")){
                    Integralrecordtype integralrecordtype = new Integralrecordtype();
                    integralrecordtype.setId(Integer.parseInt(map.get("typeId").toString()));
                    map.put("typeId",integralrecordtypeService.selectById(integralrecordtype).getProductname());
                }
                if(type.equals("2")){
                    if(map.get("otherTypeId").equals("0")){
                        map.put("typeId","签到积分");
                    }else if(map.get("otherTypeId").equals("1")){
                        map.put("typeId","带新人积分");
                    }else if(map.get("otherTypeId").equals("2")){
                        map.put("typeId","活动兑换积分");
                    }else if(map.get("otherTypeId").equals("3")){
                        map.put("typeId","生日积分");
                    }else if(map.get("otherTypeId").equals("4")){
                        map.put("typeId","积分赠送");
                    }else if(map.get("otherTypeId").equals("5")){
                        map.put("typeId","积分清零");
                    }
                }
//                map.put("typeName",integralrecordtypeService.selectById(map.get("typeId").toString()).getProducttype()); //获取积分类型
                if(map.get("memberid") != null){
                    Membermanagement membermanagement = membermanagementService.selectById(map.get("memberid").toString());
                    map.put("memberName",membermanagement.getName());
                    map.put("memberPhone",membermanagement.getPhone());
                    map.put("membercadid",membermanagement.getCadID());
                    //获取现有积分总数
                    map.put("integral",membermanagement.getIntegral());
                    //获取已消耗积分总数
                    EntityWrapper<Integralrecord> integralrecordEntityWrapper = new EntityWrapper<>();
                    integralrecordEntityWrapper.eq("memberId",membermanagement.getId());
                    integralrecordEntityWrapper.isNotNull("typeId");
                    List<Integralrecord> integralrecords = integralrecordService.selectList(integralrecordEntityWrapper);
                    double sum = integralrecords.stream().mapToDouble(Integralrecord::getIntegral).sum();
                    map.put("useIntegral",sum);
                }
                if(map.get("staffid") != null){
                    map.put("staffName",userService.selectById(map.get("staffid").toString()).getName());
                }
            }
        }
        return super.packForBT(serverPage);
    }

    /**
     * 积分兑换统计
     * @return
     */
    @RequestMapping(value = "jifenduihuantongji")
    @ResponseBody
    public Object jifenduihuantongji(){
        return null;
    }

    @RequestMapping(value = "dataexport")
    public void export(HttpServletResponse response,String condition, String operator, String memberName, String cadId
            , String type, String integralType, String begindate, String enddate, String memberId) throws Exception {
        List<MemberJifenExcel> data=new ArrayList<>();
        PageInfoBT list = (PageInfoBT)list(condition, operator, memberName, cadId, type, integralType, begindate, enddate, memberId);
        List<MemberJifenExcel> memberExcels =  JSON.parseArray(JSON.toJSONString(list.getRows()), MemberJifenExcel.class);
        ExportParams params = new ExportParams();
        Workbook workbook = ExcelExportUtil.exportExcel(params, MemberJifenExcel.class, memberExcels);
        response.setHeader("content-Type","application/vnc.ms-excel");
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("会员积分导出", "UTF-8")+".xls");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            memberExcels.clear();
            outputStream.close();
        }
    }
    @RequestMapping("jfmxb")
    public String jfmxb(Model model){
        return PREFIX + "jifenmingxibiao.html";
    }
    @RequestMapping(value = "/jifenmingxibiaoList")
    @ResponseBody
    public Object jifenmingxibiaoList(String condition) {
        Page<Dept> page = new PageFactory<Dept>().defaultPage();
        EntityWrapper<Dept> iWrapper = new EntityWrapper<>();
        if(!StringUtils.isEmpty(condition))iWrapper.like("fullname",condition);
        Page<Map<String, Object>> mapPage = deptService.selectMapsPage(page, iWrapper);
        for(Map<String, Object> map : mapPage.getRecords()){
            String deptId = map.get("id").toString();
            //查询会员人数
            EntityWrapper<Membermanagement> wrapper = new EntityWrapper<>();
            wrapper.eq("deptId",deptId);
            int i = membermanagementService.selectCount(wrapper);
            map.put("sumRen",i);
            //查询积分总分
            double sum1 = membermanagementService.selectList(wrapper).stream().mapToDouble(Membermanagement::getCountPrice).sum();
            map.put("sumJifenzongshu",sum1);
            //查询已消耗积分总数
            EntityWrapper<Integralrecord> wrapper1 = new EntityWrapper<>();
            wrapper1.eq("deptId",deptId);
            wrapper1.isNotNull("typeId");
            double sum = integralrecordService.selectList(wrapper1).stream().mapToDouble(Integralrecord::getIntegral).sum();
            map.put("sumXiaohao",sum);
            //查询签到积分数
            EntityWrapper<Integralrecord> wrapper2 = new EntityWrapper<>();
            wrapper2.eq("deptId",deptId);
            wrapper2.eq("otherTypeId",0);
            double sum2 = integralrecordService.selectList(wrapper2).stream().mapToDouble(Integralrecord::getIntegral).sum();
            map.put("sumQiandao",sum2);
            //查询空包装兑换加的积分数
            //查询哪种商品兑换的总分总数
            //查询积分兑换总数
            EntityWrapper<Integralrecord> wrapper3 = new EntityWrapper<>();
            wrapper3.eq("deptId",deptId);
            wrapper3.eq("typeId",1).or().eq("typeId",3);
            double sum3 = integralrecordService.selectList(wrapper3).stream().mapToDouble(Integralrecord::getIntegral).sum();
            map.put("sumDuihuan",sum3);
        }
        return super.packForBT(mapPage);
    }
}
