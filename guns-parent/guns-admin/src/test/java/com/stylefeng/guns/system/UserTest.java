package com.stylefeng.guns.system;

import com.stylefeng.guns.base.BaseJunit;
import com.stylefeng.guns.modular.system.dao.UserMapper;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * 用户测试
 *
 * @author fengshuonan
 * @date 2017-04-27 17:05
 */
public class UserTest {


    static int a=20;
    public static void main(String[] args) {
//        String cmd =  "mysqldump -u"+ "jksq" +"  -p"+"akmn123" + " shenzhen_guns" + " -r "
//                + "d:" + "/beifen/" + "shenzhen_guns"+new java.util.Date().getTime()+ ".sql";
//        System.out.println(cmd);
        for(int i=0;i<5;i++){
            new Runnable() {
                @Override
                public void run() {
                    new UserTest().a1();
                }
            }.run();
            new Runnable() {
                @Override
                public void run() {
                    new UserTest().a2();
                }
            }.run();
        }
    }


    public synchronized void a1(){
        System.out.println("this a 1");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public synchronized void a2(){
        System.out.println("this a 2");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
