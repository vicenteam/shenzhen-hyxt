package yongyou.test;

import yongyou.openAPI.openAPI;

public class AmoTest {
	public static final String user = "冯芳";
	public static final String pwd="";
	public static final String account= "1";
	
	//授权信息
	private static String appKey = "a4e5cec6-7070-4858-a802-57e13adbd699";
	private static String appSecret="bjc4b5";
	/**
	 * 测试主方法
	 * @param args
	 * @throws Exception
	 * 返回信息：{"code":"EXSM0003","message":"提供令牌信息不正确"}
	 */
	public static void main(String[] args) throws Exception {
		String jsonStr = "";
		String json = "{\n" +
				"\tparam:{\n" +
				"\t\tWarehouse: [{Code:\"001\" },{Code:\"002\" }],\n" +
				"\t\tInvBarCode: \"\",\n" +
				"\t\tBeginInventoryCode: \"\",\n" +
				"\t\tEndInventoryCode: \"\",\n" +
				"\t\tInventoryName: \"\",\n" +
				"\t\tSpecification: \"\",\n" +
				"\t\tBrand: \"\", \n" +
				"\t\tGroupInfo: {\n" +
				"\t\t\tWarehouse: true,\n" +
				"\t\t\tInventory:  true,\n" +
				"\t\t\tBrand:  true,\n" +
				"\t\t\tInvProperty:  true\n" +
				"\t\t}\n" +
				"\t}\n" +
				"}";
		//json = Tools.bianma(json); 自动序列化，此处不需要编码
		openAPI api = new openAPI("http://47.107.224.140:8080/TPlus/api/v1/", appKey, appSecret);
//		jsonStr = api.get("Authorization/Logout"); //登出方法
		jsonStr = api.Login(user, pwd, account);
		jsonStr = api.getData("currentStock/Query", json);
//		json = api.get("Authorization/ReLogin");  //重新登录方法
		System.out.println(jsonStr);

	}

}
