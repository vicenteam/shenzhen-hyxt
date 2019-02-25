package yongyou.util;

import com.alibaba.fastjson.JSON;
import yongyou.openAPI.openAPI;

public class YongYouAPIUtils {
//    public static final String user = "冯芳";
//    public static final String pwd="";
    public static final String user = "demo";
    public static final String pwd="ak123456";
    public static final String account= "1";

    //授权信息
    private static String appKey = "a4e5cec6-7070-4858-a802-57e13adbd699";
    private static String appSecret="bjc4b5";

    /**{"Code":"20180002","Name":"新疆香妃王葡萄干","Specification":"500g/袋","DefaultBarCode":null}
     * 获取PartnerEntityDTO对象
     * {"param":{}}
     */
    public final static String PARTNER_QUERY="partner/Query";
    /**
     * 仓库查询
     */
    public final static String WAREHOUSE_QUERY="warehouse/Query";
    /**
     * 门店查询
     */
    public final static String STORE_QUERY="store/Query";
    /**
     * 存货编码查询
     */
    public final static String INVENTORY_QUERY="inventory/Query";

  /**
     * 获取商品
     */
    public final static String CURRENTSTOCK_QUERY="currentStock/Query";
    /**{queryParam:{  Warehouse:[{Code:"001" }] }}
     * 同步仓库商品
     */
    public final static String CURRENTSTOCK_QUERYBYTIME="currentStock/QueryByTime";
    /**
     * 商品价格查询
     */
    public final static String INVENTORYPRICE_QUERYINVENTORYPRICE="inventoryPrice/QueryInventoryPrice";
    /**
     * 销售出库单
     */
    public final static String SALEDISPATCH_CREATE="saleDispatch/Create";
    /**
     * 其他出库单
     */
    public final static String OTHERDISPATCH_CREATE="otherDispatch/Create";
    /**
     * 产品入库单
     */
    public final static String PRODUCTRECEIVE_CREATE="productReceive/Create";
    /**
     * 其他入库单
     */
    public final static String OTHERRECEIVE_CREATE="otherReceive/Create";
    /**
     * 收入创建
     */
    public final static String RECEIVEVOUCHER_CREATE="receiveVoucher/Create";
    /**
     * 销货单创建
     */
    public final static String SALEDELIVERY_CREATE="saleDelivery/Create";





    public  static String postUrl(String url,String json) throws Exception {
        String jsonStr="";
        openAPI api = new openAPI("http://47.107.224.140:8080/TPlus/api/v1/", appKey, appSecret);
//        jsonStr = api.get("Authorization/Logout"); //登出方法
        jsonStr = api.Login(user, pwd, account);
        jsonStr= api.ReLogin();
//        Tools.setProper(rstrpara,jsonstr00);
        jsonStr = api.getData(url, json);
        return jsonStr;
    }

    public static void main(String[] args) throws Exception {
//        String s = postUrl("", "{\"param\":{}}");
        String s ="";
        //获取仓库信息
//         s = postUrl(YongYouAPIUtils.WAREHOUSE_QUERY, "{\"param\":{}}");
//        System.out.println(s);
        //获取商品信息
//         s = postUrl(YongYouAPIUtils.CURRENTSTOCK_QUERY, "{\"param\":{}}");
//        System.out.println(s);
        //获取PartnerTDO
//         s = postUrl(YongYouAPIUtils.PARTNER_QUERY, "{\"param\":{}}");
//        System.out.println(s);
        //新增销售出库单
// s = postUrl(YongYouAPIUtils.SALEDISPATCH_CREATE,
// "{\n" +
//         "\tdto:{\n" +
//         "\t\tExternalCode: \"st0002\",\n" +
//         "\t\tVoucherType: {Code: \"ST1024\"},\n" +
//         "\t\tVoucherDate: \"2019-02-13\",\n" +
//         "\t\tBusiType: {Code: \"15\"},\n" +
//         "\t\tWarehouse: {Code: \"003\"},\n" +
//         "\t\tMemo: \"测试\",\n" +
//         "\t\tRDRecordDetails: [{\n" +
//         "\t\t\tInvBarCode: \"\",\n" +
//         "\t\t\tInventory: {Code: \"20180002\"},\n" +
//         "\t\t\tBaseQuantity: 1.00\n" +
//         "\t\t}]\n" +
//         "\t}\n" +
//         "}"
// );
//
//        System.out.println(s);
        s = postUrl(YongYouAPIUtils.INVENTORYPRICE_QUERYINVENTORYPRICE,
// "{\"param\":[{\"RowID\": 1,\"Partner\": {    \"Code\": \"0010001\"       },\"Inventory\": {  \"Code\": \"20180002\"       }}]}"
 "{\n" +
         "    \"param\": [\n" +
         "\n" +
         "        {\n" +

         "\n" +
         "            \"Inventory\": {  \"Code\": \"20180021\"       },\n" +
         "\n" +
         "            \"Unit\": {       \"Code\":    8     }\n" +
         "\n" +
         "\n" +
         "        }      \n" +
         "\n" +
         "    ]\n" +
         "\n" +
         "}"
//                "{ \"param\" :[Inventory]}"
 );

        System.out.println(s);
    }
}
