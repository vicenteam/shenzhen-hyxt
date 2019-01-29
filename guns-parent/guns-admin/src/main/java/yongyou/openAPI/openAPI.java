package yongyou.openAPI;

import yongyou.util.Tools;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jdiy.json.JsonObject;
import org.jdiy.json.JsonUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/**   */

public class openAPI {
	String jsonstr = "";
	String headers = "";
	String surl = "http://demo.chanjet.com/TPlus116/api/v1/";
	String AppKey = "a4e5cec6-7070-4858-a802-5 7e13adbd699";
	String AppSecret = "bjc4b5";
	String c_companyid = "0";
	String Access_Token = "";
	public static String Access_Token_str = "";
	static userinfo args = null;
	int pp = 1;
	
	//设置服务器Url
	public void setUrl(String ip){
		surl = ip;
	}
	
	public openAPI(String ssurl,String sAppKey,String sAppSecret){
		surl = ssurl;
		AppKey = sAppKey;
		AppSecret = sAppSecret;
	}
	
	public static void main(String[] args) throws Exception {
	    //String sss = "";
	    //sss = getAccess("demo","","2");
	    //System.out.println(sss);
		//System.out.println(getGMTDateStr());
	}
	
	private static String getGMTDateStr() {
	    Date d = new Date(System.currentTimeMillis());
	    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
	    dateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
	    return dateFormat.format(d);
	}
	
	public String Login(String UserName,String Password,String AccountNumber) throws Exception{
		String sdate = Tools.getDate("yyyy-MM-dd");		
		Password = md5(Password);
		String postString = "{\"UserName\":\""+UserName+"\",\"Password\":\""+Password+"\", \"AccountNumber\":\""+AccountNumber+"\",\"LoginDate\":\""+sdate+"\"}";
		postString = postString.replaceAll(" ","");
		//postString = Tools.bianma(postString);
		String rstr = "";
		String methodstr = "Authorization";
		rstr = getData(methodstr,postString);
		//System.out.println(rstr+"000");
		setJson(rstr,"access_token");
		
		return rstr;
	}
	
	public String ReLogin() throws Exception{
		String rstr = "";
		String methodstr = "Authorization/ReLogin";
		
		if (Tools.getProper("access_token").equals(""))
		{
			rstr = "你已经退出了，请先登录！！！";
		}else{
			rstr = get(methodstr);
		}
		return rstr;
	}
	
	//此方法持久化 access_token，不必每次都登录了，此前运行不稳定主要是因为屏蔽了此方法！
	public void setJson(String rstr,String rstrpara) throws Exception{ 
		JsonObject jsonobj = (JsonObject) JsonUtil.parse(rstr);
		String jsonstr00 = "";
		//System.out.println(rstr+"aaa");
		//System.out.println(jsonobj.toString()+"bbb");
		if (jsonobj.get(rstrpara)==null&&jsonobj.get("data")!=null)
		{
			//如果是 返回data，不是access_token 说明是重复登录，自动读取持久化数据，不必重新持久化
			 jsonstr00 = jsonobj.get("data").toString();  
			Access_Token_str = jsonstr00;
			//System.out.println(Access_Token_str+"ddd");
			
		}else{
			//jsonstr00 = Tools.getProper(rstrpara);
			jsonstr00 = jsonobj.get(rstrpara).toString();
			//System.out.println(jsonstr00+"ccc");
			Tools.setProper(rstrpara,jsonstr00);
			Access_Token_str = jsonstr00;
			//System.out.println(Access_Token_str+"ddd");
		}
	}
	
	//此方法暂时保留
	public String getJson(String rstr,String rstrpara) throws Exception{
		JsonObject jsonobj = (JsonObject) JsonUtil.parse(rstr);
		String jsonstr = "";
		if (jsonobj.get(rstrpara)!=null)
		{
			jsonstr = jsonobj.get(rstrpara).toString();
		}
		return jsonstr;
	}/**/
	
	public String get(String methodstr) throws Exception{
		String authstr = getAuth();
		try {

			HttpClient client = new HttpClient();
	        HttpMethod method = new GetMethod(surl+methodstr);
	        // if post
	        /*HttpMethodParams params = new HttpMethodParams();
	        params.setParameter("entId", "00000716");
	        method.setParams(params);*/
	        //if (!para.equals("")) method.setParams(para);
	        //if(!para.equals(""))method.setQueryString(para);
	         //设置http头  
	        //method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	        method.addRequestHeader("Content-Type", "application/json;charset=utf-8");
			method.addRequestHeader("Authorization",authstr);
	        // 使用POST方法
	        // HttpMethod method = new PostMethod("http://java.sun.com");
	        client.executeMethod(method);
	        // 打印服务器返回的状态
	        //System.out.println(method.getStatusLine());
	        // 打印返回的信息
	        //System.out.println(method.getResponseBodyAsString());
			jsonstr = method.getResponseBodyAsString();
	        // 释放连接
	        method.releaseConnection();
	        
	        if (methodstr.equals("Authorization/Logout")){	         	
	        	Tools.setProper("access_token","");		     
	        }else if(methodstr.equals("Authorization/ReLogin")){	        	
	        	if (Tools.getProper("access_token").equals(""))
	    		{
	        		jsonstr = "你已经退出了，请先登录！！！";
	    		}	        	
	        }
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonstr;
	}	

	public String getData(String methodstr,String para) throws Exception{
	String authstr = getAuth();
	try {
		HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(surl+methodstr);
        if(para.equals(""))
        {
        	//para = para.replaceAll("\r","");
        	//para = para.replaceAll("\n","");
        	//para = para.replaceAll(" ","");
        }
    	para = "_args="+Tools.bianma(para);
    	
        method.setQueryString(para);
        
         //设置http头  
        //method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        method.addRequestHeader("Content-Type", "application/json;charset=utf-8");
		method.addRequestHeader("Authorization",authstr);
        // 使用POST方法
        // HttpMethod method = new PostMethod("http://java.sun.com");
        client.executeMethod(method);
        // 打印服务器返回的状态
        //System.out.println(method.getStatusLine());
        // 打印返回的信息
        //System.out.println(method.getResponseBodyAsString());
		jsonstr = method.getResponseBodyAsString();
        // 释放连接
        method.releaseConnection();
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return jsonstr;
}
	
	//获取认证头
	public String getAuth()throws Exception{  //Mon, 11 May 2015 02:42:11 GMT
			String authstr = "";              //Thu, 07 May 2015 08:05:46 GMT    
			String access_token = Tools.getProper("access_token");
			if (access_token.equals("")) access_token = Access_Token_str;
			//System.out.println(access_token);
			String utcDate = getGMTDateStr();
			String authParamInfo = "{\"uri\":\""+surl+"Authorization\",\"access_token\":\""+access_token+"\",\"date\":\""+utcDate+"\"}";
			//System.out.println(authParamInfo);
		try {		
		    String HMAC_SHA1 = "HmacSHA1";
			byte[] keyBytes=AppSecret.getBytes();  
		    SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1);     
		    Mac mac = Mac.getInstance(HMAC_SHA1);     
		    mac.init(signingKey);     
		    byte[] rawHmac = mac.doFinal(authParamInfo.getBytes());  			 
			String encodedHashValue = Base64.encodeBase64String(rawHmac);
			//String encodedHashValue = Base64.encodeBase64String(hashValue);		
			//String encode = Convert.ToBase64String(UTF8Encoding.UTF8.GetBytes(authStr));			
			encodedHashValue = "{\"appKey\":\"" + AppKey + "\",\"authInfo\":\"hmac-sha1 " + encodedHashValue + "\",\"paramInfo\":" + authParamInfo + "}";
					
			//encode = Convert.ToBase64String(UTF8Encoding.UTF8.GetBytes(authStr));			
			//System.out.println(authParamInfo);			
			//System.out.println(encodedHashValue);	
			
			encodedHashValue = Base64.encodeBase64String(encodedHashValue.getBytes());
				
			authstr = encodedHashValue;					
			//System.out.println(authstr);
			
		}finally{
			
		}
		return authstr;
		
	}
	
	public static String md5(String value){
		
		String encode = "";
		
		try {
			MessageDigest md5;
			md5 = MessageDigest.getInstance("MD5");
		
			//sun.misc.BASE64Encoder baseEncoder = new sun.misc.BASE64Encoder(); 
			//String value=baseEncoder.encode(md5.digest(s.getBytes("utf-8"))); 
			//System.out.println(value);
			
	        byte[] hashByte = md5.digest(value.getBytes());       
	
	        encode = Base64.encodeBase64String(hashByte);
        
        } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
		return encode;
	}
}
