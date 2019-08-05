package com.stylefeng.guns.modular.main.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.modular.main.service.IMembermanagementService;
import com.stylefeng.guns.modular.main.service.IMembershipcardtypeService;
import com.stylefeng.guns.modular.system.controller.DeptController;
import com.stylefeng.guns.modular.system.model.Membermanagement;
import com.stylefeng.guns.modular.system.model.Membershipcardtype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@Scope("prototype")
@RequestMapping("/piesimple")
public class PiesimpleController extends BaseController {
    private String PREFIX = "/main/bar/";
    @Autowired
    private IMembermanagementService membermanagementService;
    @Autowired
    private IMembershipcardtypeService membershipcardtypeService;
    @Autowired
    private DeptController deptController;
    /**
     * 跳转到会员分布数据图表
     * @return
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "piesimple.html";
    }
    @RequestMapping(value = "/getData")
    @ResponseBody
    public Object getData(String deptId) throws ParseException {
        List<Map<String,Object>> result=new ArrayList<>();
        EntityWrapper<Membermanagement> wapper= new EntityWrapper<Membermanagement>();
        wapper.eq("deptId",deptId);
        EntityWrapper<Membershipcardtype> membershipcardtypeBaseEntityWrapper= new EntityWrapper<>();
        membershipcardtypeBaseEntityWrapper.eq("deptId",deptId);
        List<Membershipcardtype> list=membershipcardtypeService.selectList(membershipcardtypeBaseEntityWrapper);
        int index=0;
        for(Membershipcardtype membershipcardtype:list){
            if(ShiroKit.getUser().account.equals("admin" )&& ("53".equals(deptId))){
                Integer meid= membershipcardtype.getId();
                String mename=membershipcardtype.getCardname();
                //获取全部名称为当前会员等级名称的数据
                EntityWrapper<Membershipcardtype> wrapper = new EntityWrapper<>();
                wrapper.eq("cardname",mename);
                String ids="";
                List<Membershipcardtype> membershipcardtypes = membershipcardtypeService.selectList(wrapper);
                for(Membershipcardtype m:membershipcardtypes){
                    ids+=m.getId()+",";
                }
                wapper= new EntityWrapper<Membermanagement>();
                wapper.in("levelID",ids);
                int count= membermanagementService.selectCount(wapper);
                Map<String,Object> map=new HashMap<>();
                map.put("name",mename);
                map.put("value",count);
                map.put("index",index);
                result.add(map);
                index++;
            }else {
                Integer meid= membershipcardtype.getId();
                String mename=membershipcardtype.getCardname();
                wapper= new EntityWrapper<Membermanagement>();
                wapper.eq("deptId",deptId);
                wapper.eq("levelID",meid);
                int count= membermanagementService.selectCount(wapper);
                Map<String,Object> map=new HashMap<>();
                map.put("name",mename);
                map.put("value",count);
                map.put("index",index);
                result.add(map);
                index++;
            }

        }
        return result;
    }
}
