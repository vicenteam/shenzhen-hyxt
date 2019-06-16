package com.stylefeng.guns.modular.main.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.modular.system.model.Dept;
import com.stylefeng.guns.modular.system.service.IDeptService;
import com.stylefeng.guns.modular.system.utils.BarRankingExcel;
import com.stylefeng.guns.modular.system.utils.IntegralRecordTypeExcel;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.common.BaseEntityWrapper.BaseEntityWrapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.system.model.Integralrecordtype;
import com.stylefeng.guns.modular.main.service.IIntegralrecordtypeService;
import org.springframework.web.multipart.MultipartFile;
import yongyou.util.YongYouAPIUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

/**
 * 积分类型控制器
 *
 * @author fengshuonan
 * @Date 2018-10-30 07:52:51
 */
@Controller
@Scope("prototype")
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
        integralrecordtypeService.updateAllIntegralrecordtype(integralrecordtype.getId().toString());
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
        integralrecordtypeService.updateAllIntegralrecordtype(integralrecordtype.getId().toString());
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
        integralrecordtypeBaseEntityWrapper.eq("InventoryCode", productCode);
        Integralrecordtype integralrecordtype = integralrecordtypeService.selectOne(integralrecordtypeBaseEntityWrapper);
        return integralrecordtype == null ? "error" : integralrecordtype;
    }

    /**
     * 同步数据
     *
     * @return
     */
    @RequestMapping(value = "/tongbuData")
    @ResponseBody
    public Object tongbuData() throws Exception {
        StringBuilder sb = new StringBuilder();
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
            integralrecordtype.setAvailableQuantity(integralrecordtype.getExistingQuantity());
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
                sb.append(integralrecordtype.getId() + ",");
            } else {
                integralrecordtype1.setProductnum(integralrecordtype.getAvailableQuantity().intValue());
                integralrecordtypeService.updateById(integralrecordtype1);
                sb.append(integralrecordtype1.getId() + ",");
            }
        }
        BaseEntityWrapper<Integralrecordtype> wrapper = new BaseEntityWrapper<>();
        wrapper.notIn("id", sb.toString());
//        integralrecordtypeService.delete(wrapper);
        integralrecordtypeService.updateForSet("status=0", wrapper);
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
        JSONObject resJson = new JSONObject();
        StringBuffer resultMessage = new StringBuffer();
        try {
            List<IntegralRecordTypeExcel> excelUpload = ExcelImportUtil.importExcel(file.getInputStream(), IntegralRecordTypeExcel.class, params);
            String createtime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
            for (IntegralRecordTypeExcel integralRecordTypeExcel : excelUpload) {
                BaseEntityWrapper<Integralrecordtype> iWrapper = new BaseEntityWrapper<>();
                iWrapper.eq("InventoryCode", integralRecordTypeExcel.getInventoryCode());
                Integralrecordtype integralrecordtype = integralrecordtypeService.selectOne(iWrapper);
                if (integralrecordtype != null) { //更新导入价格
                    if (!StringUtils.isEmpty(integralRecordTypeExcel.getProductpice())) {
                        integralrecordtype.setProductpice(Double.parseDouble(integralRecordTypeExcel.getProductpice()));
                        integralrecordtype.setProductduihuanjifen(Double.parseDouble(integralRecordTypeExcel.getProductpice())); //兑换积分=金额单价
                    }
                    if (!StringUtils.isEmpty(integralRecordTypeExcel.getRetailPrice()))
                        integralrecordtype.setRetailPrice(Double.parseDouble(integralRecordTypeExcel.getRetailPrice()));
                    if (!StringUtils.isEmpty(integralRecordTypeExcel.getProductnum()))
                        integralrecordtype.setProductnum(Integer.parseInt(integralRecordTypeExcel.getProductnum()));
                    integralrecordtypeService.updateById(integralrecordtype);
                } else {
                    integralrecordtype = new Integralrecordtype();
                    resultMessage.append(integralRecordTypeExcel.getInventoryCode() + "、");
                    if (!StringUtils.isEmpty(integralRecordTypeExcel.getInventoryCode())) {
                        integralrecordtype.setInventoryCode(integralRecordTypeExcel.getInventoryCode());
                    }
                    if (!StringUtils.isEmpty(integralRecordTypeExcel.getProductname())) {
                        integralrecordtype.setProductname(integralRecordTypeExcel.getProductname());
                    }
                    if (!StringUtils.isEmpty(integralRecordTypeExcel.getProductpice())) {
                        integralrecordtype.setProductpice(Double.parseDouble(integralRecordTypeExcel.getProductpice()));
                        integralrecordtype.setProductduihuanjifen(Double.parseDouble(integralRecordTypeExcel.getProductpice()));
                    }
                    if (!StringUtils.isEmpty(integralRecordTypeExcel.getRetailPrice())) {
                        integralrecordtype.setRetailPrice(Double.parseDouble(integralRecordTypeExcel.getRetailPrice()));
                    }
                    if (!StringUtils.isEmpty(integralRecordTypeExcel.getProductspecification())) {
                        integralrecordtype.setProductspecification(integralRecordTypeExcel.getProductspecification());
                    }
                    if (!StringUtils.isEmpty(integralRecordTypeExcel.getProductnum())) {
                        integralrecordtype.setProductnum(Integer.parseInt(integralRecordTypeExcel.getProductnum()));
                    }
                    if (!StringUtils.isEmpty(integralRecordTypeExcel.getUnitName())) {
                        integralrecordtype.setUnitName(integralRecordTypeExcel.getUnitName());
                    }
                    if (!StringUtils.isEmpty(integralRecordTypeExcel.getProducttype())) {
                        integralrecordtype.setProducttype(Integer.parseInt(integralRecordTypeExcel.getProducttype()));
                    }
                    integralrecordtype.setDeptid(ShiroKit.getUser().deptId.toString());
                    integralrecordtype.setCreatetime(createtime);
                    integralrecordtype.setCreateuserid(ShiroKit.getUser().id.toString());
                    integralrecordtype.setStatus(0);
                    integralrecordtypeService.insert(integralrecordtype);
                }
            }
            resJson.put("msg", "导入成功，" + resultMessage == null ? "" : "新增到商品：" + resultMessage.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("msg", "上传成功");
        return map;
    }

    @RequestMapping("/export")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        BaseEntityWrapper<Integralrecordtype> wrapper = new BaseEntityWrapper<>();
//        List<Integralrecordtype> details = integralrecordtypeService.selectList(wrapper);
//        List<IntegralRecordTypeExcel> excelList = new ArrayList<>();
//        for (Integralrecordtype detail : details) {
//            IntegralRecordTypeExcel excel = JSON.parseObject(JSON.toJSONString(detail), new TypeReference<IntegralRecordTypeExcel>() {
//            });
//            excelList.add(excel);
//        }
//
//        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(100);
//        SXSSFSheet sxssfSheet = sxssfWorkbook.createSheet();
//        //创建excel 数据列名
//        SXSSFRow rowTitle = sxssfSheet.createRow(0);
//        CellUtil.createCell(rowTitle, 0, "商品编码");
//        CellUtil.createCell(rowTitle, 1, "商品名称");
//        CellUtil.createCell(rowTitle, 2, "亲民价");
//        CellUtil.createCell(rowTitle, 3, "零售价");
//        CellUtil.createCell(rowTitle, 4, "规格");
//        CellUtil.createCell(rowTitle, 5, "可用数量");
//        CellUtil.createCell(rowTitle, 6, "计量单位");
//        CellUtil.createCell(rowTitle, 7, "商品类型");
//        CellUtil.createCell(rowTitle, 8, "门店名称");
//        CellUtil.createCell(rowTitle, 9, "门店编码");
//        Iterator<IntegralRecordTypeExcel> iter = excelList.iterator();
//        Integer i = 1;
//        while (iter.hasNext()) {
//            SXSSFRow row = sxssfSheet.createRow(i);
//            IntegralRecordTypeExcel integralRecordTypeExcel = iter.next();
//            Field[] fields = integralRecordTypeExcel.getClass().getDeclaredFields();
//            for (int c = 0; c < fields.length; c++) {
//                String name = fields[c].getName();
//                // 将属性的首字符大写，方便构造get，set方法
//                name = name.substring(0, 1).toUpperCase() + name.substring(1);
//                Method m = integralRecordTypeExcel.getClass().getMethod("get" + name);
//                // 调用getter方法获取属性值
//                String value = (String) m.invoke(integralRecordTypeExcel);
//                if (!StringUtils.isEmpty(value)) {
//                    CellUtil.createCell(row, c, value);
//                } else {
//                    CellUtil.createCell(row, c, "");
//                }
//            }
//            i++;
//        }
//        response.setHeader("content-Type", "application/vnc.ms-excel");
//        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("商品导出", "UTF-8") + ".xlsx");
//        response.setCharacterEncoding("UTF-8");
//        ServletOutputStream outputStream = response.getOutputStream();
//        try {
//            sxssfWorkbook.write(outputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            details.clear();
//            outputStream.close();
//        }

        BaseEntityWrapper<Integralrecordtype> wrapper = new BaseEntityWrapper<>();
        wrapper.eq("status",0);
        List<Integralrecordtype> details = integralrecordtypeService.selectList(wrapper);
        List<IntegralRecordTypeExcel> excels = new ArrayList<>();
        for (Integralrecordtype detail : details) {
            IntegralRecordTypeExcel excel = JSON.parseObject(JSON.toJSONString(detail), new TypeReference<IntegralRecordTypeExcel>() {
            });
            excels.add(excel);
        }
        ExportParams params = new ExportParams();
        params.setSheetName("商品列表");
        Workbook workbook = ExcelExportUtil.exportExcel(params, IntegralRecordTypeExcel.class, excels);
        response.setHeader("content-Type", "application/vnc.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("商品导出", "UTF-8") + ".xls");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            details.clear();
            outputStream.close();
        }
    }
}
