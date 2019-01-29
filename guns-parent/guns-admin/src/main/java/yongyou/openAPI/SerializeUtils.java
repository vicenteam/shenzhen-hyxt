package yongyou.openAPI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtils {
    //private static final Logger LOG = LoggerFactory.getLogger(SerializeUtils.class);

    /**
     * �������л�Ϊ�ַ���
     */
    public static String serialize(Object obj) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);  
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");//������ISO-8859-1
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");//������ַ����������루����Ҳ��Ӱ�칦�ܣ�
         //LOG.info("����obj����" + obj + "�����л�serStr����" + serStr + "��");
        
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }

    /**
     * �ַ��� �����л�Ϊ ����
     */
    public static Object unSerialize(String serStr) throws Exception {
        String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream); 
        Object obj = objectInputStream.readObject();
        //LOG.info("����obj����" + obj + "�������л�serStr����" + serStr + "��");
        
        objectInputStream.close();
        byteArrayInputStream.close();
        return obj;
    }

    public static void main(String[] args) throws Exception {
        
    	userinfo json = new userinfo();
        json.set("name","����");
        json.set("age",30);
        //System.out.print(json);
    	
    	//List[] list = {"1","1","1","1","1"};
        //HashMap<String, String> map = new HashMap<String, String>();
        //map.put("number", "123");
        //map.put("name", "test");
        //list.add(map);
        //HashMap<String, String> map2 = new HashMap<String, String>();
        //map2.put("number", "1232");
        //map2.put("name", "test2");
        //list.add(map2);
        
        String str=serialize(json);
        System.out.println(str);
        
        //System.err.println(str);
        //List<HashMap<String, String>> newList=(List<HashMap<String, String>>) unSerialize(str);

        //for(Map m : newList) {
            //System.out.println(m.get("number") + " " + m.get("name"));
        //}
    }/**/
}