package com.myself.crm.setting.service.impl;

import com.myself.crm.setting.domain.Users;
import com.myself.crm.setting.exception.LoginException;
import com.myself.crm.setting.mapper.UserMapper;
import com.myself.crm.setting.service.UserService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by DS on 2022/04/05 14:05
 */
public class UserServiceImpl implements UserService {

    SimpleDateFormat sdf;
    @Override
    public Users login(String loginAct, String loginPwd, String ip) throws IOException, LoginException {
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Map map = new HashMap();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        Users users = mapper.checkLogin(map);

        if (users == null){
            throw new LoginException("账号不存在或密码错误");
        }
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        if (users.getExpireTime().compareTo(date) < 0){
            throw new LoginException("账号已失效");
        }
        if (!users.getAllowIps().contains(ip)){
            throw new LoginException("IP访问受限");
        }
        if ("0".equals(users.getLockState())){
            throw new LoginException("账号被锁定");
        }
        sqlSession.close();
        return users;
    }
}
