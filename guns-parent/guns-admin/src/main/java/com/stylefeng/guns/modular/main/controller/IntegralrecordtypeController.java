package com.stylefeng.guns.modular.main.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.system.model.Dept;
import com.stylefeng.guns.modular.system.service.IDeptService;
import com.stylefeng.guns.modular.system.utils.IntegralRecordTypeExcel;
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
import com.stylefeng.guns.modular.system.model.Integralrecordtype;
import com.stylefeng.guns.modular.main.service.IIntegralrecordtypeService;
import org.springframework.web.multipart.MultipartFile;
import yongyou.util.YongYouAPIUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 积分类型控制器
 *
 * @author fengshuonan
 * @Date 2018-10-30 07:52:51
 */
@Controller
@RequestMapping("/integralrecordtype")
public class IntegralrecordtypeController extends BaseController {

    private String PREFIX = "/main/integralrecordtype/";

    @Autowired
    private IIntegralrecordtypeService integralrecordtypeService;
    @Autowired
    private IDeptService deptService;

    /**
     * 跳转到积分类型首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "integralrecordtype.html";
    }

    /**
     * 跳转到添加积分类型
     */
    @RequestMapping("/integralrecordtype_add")
    public String integralrecordtypeAdd() {
        return PREFIX + "integralrecordtype_add.html";
    }

    /**
     * 跳转到修改积分类型
     */
    @RequestMapping("/integralrecordtype_update/{integralrecordtypeId}")
    public String integralrecordtypeUpdate(@PathVariable Integer integralrecordtypeId, Model model) {
        Integralrecordtype integralrecordtype = integralrecordtypeService.selectById(integralrecordtypeId);
        model.addAttribute("item", integralrecordtype);
        LogObjectHolder.me().set(integralrecordtype);
        return PREFIX + "integralrecordtype_edit.html";
    }

    /**
     * 获取积分类型列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition, Integer producttype, Integer id) {
        Page<Integralrecordtype> page = new PageFactory<Integralrecordtype>().defaultPage();
        BaseEntityWrapper<Integralrecordtype> baseEntityWrapper = new BaseEntityWrapper<>();
        if (id != null) {
            baseEntityWrapper.eq("id", id);
        } else {
            if (!StringUtils.isEmpty(condition)) baseEntityWrapper.like("productname", condition);
        }

        if (producttype != null) baseEntityWrapper.eq("producttype", producttype);
        baseEntityWrapper.eq("status", 0);
        Page<Integralrecordtype> result = integralrecordtypeService.selectPage(page, baseEntityWrapper);
        return super.packForBT(result);
    }

    /**
     * 新增积分类型
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Integralrecordtype integralrecordtype) {
        integralrecordtype.setCreatetime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        integralrecordtype.setCreateuserid(ShiroKit.getUser().id + "");
        integralrecordtype.setDeptid(ShiroKit.getUser().getDeptId() + "");
        integralrecordtype.setStatus(0);
        integralrecordtypeService.insert(integralrecordtype);
        return SUCCESS_TIP;
    }

    /**
     * 删除积分类型
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer integralrecordtypeId) {
        Integralrecordtype integralrecordtype = integralrecordtypeService.selectById(integralrecordtypeId);
        integralrecordtype.setStatus(1);
        integralrecordtypeService.updateById(integralrecordtype);
        return SUCCESS_TIP;
    }

    /**
     * 修改积分类型
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Integralrecordtype integralrecordtype) {
        integralrecordtype.setUpdatetime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        integralrecordtype.setUpdateuserid(ShiroKit.getUser().getId() + "");
        integralrecordtypeService.updateById(integralrecordtype);
        return SUCCESS_TIP;
    }

    /**
     * 积分类型详情
     */
    @RequestMapping(value = "/detail/{integralrecordtypeId}")
    @ResponseBody
    public Object detail(@PathVariable("integralrecordtypeId") Integer integralrecordtypeId) {
        return integralrecordtypeService.selectById(integralrecordtypeId);
    }

    @RequestMapping(value = "/findCode/{productCode}")
    @ResponseBody
    public Object findCode(@PathVariable("productCode") String productCode) {
        BaseEntityWrapper<Integralrecordtype> integralrecordtypeBaseEntityWrapper = new BaseEntityWrapper<>();
        integralrecordtypeBaseEntityWrapper.eq("InventoryCode",productCode);
        Integralrecordtype integralrecordtype = integralrecordtypeService.selectOne(integralrecordtypeBaseEntityWrapper);
        return integralrecordtype==null?"error":integralrecordtype;
    }

    /**
     * 同步数据
     *
     * @return
     */
    @RequestMapping(value = "/tongbuData")
    @ResponseBody
    public Object tongbuData() throws Exception {
        Dept dept = deptService.selectById(ShiroKit.getUser().deptId);
        String Warehouse = "";
        if (dept.gettPlusWarehouseCode() != null) {
            Warehouse = " Warehouse:[{Code:\"" + dept.gettPlusWarehouseCode() + "\" }]";
        }
        String s = YongYouAPIUtils.postUrl(YongYouAPIUtils.CURRENTSTOCK_QUERYBYTIME, "{queryParam:{ " + Warehouse + " }}");
        System.out.println(s);
        System.out.println("同步数据。。。");
        List<Integralrecordtype> integralrecordtypes = JSON.parseArray(s, Integralrecordtype.class);
        for (Integralrecordtype integralrecordtype : integralrecordtypes) {
            String inventoryCode = integralrecordtype.getInventoryCode();
            BaseEntityWrapper<Object> objectBaseEntityWrapper = new BaseEntityWrapper<>();
            objectBaseEntityWrapper.eq("InventoryCode", inventoryCode);
            Integralrecordtype integralrecordtype1 = integralrecordtypeService.selectOne(objectBaseEntityWrapper);
            if (integralrecordtype1 == null) {
                integralrecordtype.setNames(integralrecordtype.getInventoryName());
                integralrecordtype.setProductname(integralrecordtype.getInventoryName());
                integralrecordtype.setProductnum(0);
                integralrecordtype.setProductjifen("0");
                integralrecordtype.setProducttype(2);
                integralrecordtype.setProductspecification(integralrecordtype.getSpecification());
                integralrecordtype.setStatus(0);
                integralrecordtype.setDeptid(ShiroKit.getUser().deptId + "");
                integralrecordtype.setProductnum(integralrecordtype.getAvailableQuantity().intValue());
                integralrecordtypeService.insert(integralrecordtype);
            } else {
                integralrecordtype1.setProductnum(integralrecordtype.getAvailableQuantity().intValue());
                integralrecordtypeService.updateById(integralrecordtype1);
            }
        }
        System.out.println("同步数据完成。。。");
        return "success";
    }

    /**
     * 跳转到excel 导入
     */
    @RequestMapping("import_excel")
    public String importExcel() {
        return PREFIX + "excel_import.html";
    }

    @RequestMapping(value = "/importE")
    @ResponseBody
    public Object importE(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        String message = "";
        Integer total = 0;
        Integer nowNum = 0;
        JSONObject resJson = new JSONObject();
        try {
            List<IntegralRecordTypeExcel> excelUpload = ExcelImportUtil.importExcel(file.getInputStream(), IntegralRecordTypeExcel.class, params);
            Integralrecordtype integralrecordtype = new Integralrecordtype();
            for (IntegralRecordTypeExcel integralRecordTypeExcel : excelUpload) {
                integralrecordtype.setNames(integralRecordTypeExcel.getInventoryName());
                integralrecordtype.setProductname(integralRecordTypeExcel.getInventoryName());
                integralrecordtype.setProducttype(integralRecordTypeExcel.getProducttype());
                integralrecordtype.setProductspecification(integralRecordTypeExcel.getSpecification());
                integralrecordtype.setProductnum(integralRecordTypeExcel.getAvailableQuantity().intValue());
                integralrecordtype.setProductjifen("");
                integralrecordtype.setDeptid(ShiroKit.getUser().getDeptId()+"");
                integralrecordtype.setCreatetime(DateUtil.getTime());
//                integralrecordtype.setUpdateuserid();
                integralrecordtype.setStatus(0);
                integralrecordtype.setWarehouseCode(integralRecordTypeExcel.getWarehouseCode());
                integralrecordtype.setWarehouseName(integralRecordTypeExcel.getWarehouseName());
                integralrecordtype.setInventoryCode(integralRecordTypeExcel.getInventoryCode());
                integralrecordtype.setInventoryName(integralRecordTypeExcel.getInventoryName());
                integralrecordtype.setSpecification(integralRecordTypeExcel.getSpecification());
                integralrecordtype.setAvailableQuantity(integralRecordTypeExcel.getAvailableQuantity());
                integralrecordtype.setUnitName(integralRecordTypeExcel.getUnitName());
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
        }catch (Exception e){
            message = "导入失败，第" + (total + 1) + "条错误!";
            resJson.put("msg", message);
            e.printStackTrace();
        }
        return null;
    }

}
