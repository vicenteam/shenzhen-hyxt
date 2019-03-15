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
public class UserTest  {

    public static void main(String[] args) {
        String cmd =  "mysqldump -u"+ "jksq" +"  -p"+"akmn123" + " shenzhen_guns" + " -r "
                + "d:" + "/beifen/" + "shenzhen_guns"+new java.util.Date().getTime()+ ".sql";
        System.out.println(cmd);
    }

}
