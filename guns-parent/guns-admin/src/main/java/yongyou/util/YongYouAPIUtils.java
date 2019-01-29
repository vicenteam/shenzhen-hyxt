package yongyou.util;

import yongyou.openAPI.openAPI;

public class YongYouAPIUtils {
    public static final String user = "冯芳";
    public static final String pwd="";
    public static final String account= "1";

    //授权信息
    private static String appKey = "a4e5cec6-7070-4858-a802-57e13adbd699";
    private static String appSecret="bjc4b5";

    /**
     * 获取商品
     */
    private final static String CURRENTSTOCK_QUERY="currentStock/Query";
    /**
     * 销售出库单
     */
    private final static String SALEDISPATCH_CREATE="saleDispatch/Create";
    /**
     * 其他出库单
     */
    private final static String OTHERDISPATCH_CREATE="otherDispatch/Create";
    /**
     * 产品入库单
     */
    private final static String PRODUCTRECEIVE_CREATE="productReceive/Create";
    /**
     * 其他入库单
     */
    private final static String OTHERRECEIVE_CREATE="otherReceive/Create";



    public  static String postUrl(String url,String json) throws Exception {
        String jsonStr="";
        openAPI api = new openAPI("http://47.107.224.140:8080/TPlus/api/v1/", appKey, appSecret);
//		jsonStr = api.get("Authorization/Logout"); //登出方法
        jsonStr = api.Login(user, pwd, account);
        jsonStr = api.getData(url, json);
        return jsonStr;
    }
}
