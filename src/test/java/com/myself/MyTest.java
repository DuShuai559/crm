package com.myself;

import com.myself.crm.setting.mapper.UserMapper;
import com.myself.crm.utils.MD5Util;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by DS on 2022/04/04 14:46
 */
public class MyTest {
    SqlSession sqlSession;
    UserMapper mapper;
    @Before
    public void openSession() throws IOException {
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        sqlSession = factory.openSession();
        mapper = sqlSession.getMapper(UserMapper.class);
    }
    @After
    public void close(){
        sqlSession.close();
    }

    @Test
    public void test(){
        String loginAct = "zs";
        Map map = new HashMap();
        map.put("loginAct", loginAct);
        String loginPwd = "123";
        loginPwd = MD5Util.getMD5(loginPwd);
        map.put("loginPwd", loginPwd);
        mapper.checkLogin(map);
    }
}
