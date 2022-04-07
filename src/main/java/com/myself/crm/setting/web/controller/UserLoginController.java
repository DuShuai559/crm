package com.myself.crm.setting.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myself.crm.setting.domain.Users;
import com.myself.crm.setting.exception.LoginException;
import com.myself.crm.setting.service.UserService;
import com.myself.crm.setting.service.impl.UserServiceImpl;
import com.myself.crm.utils.MD5Util;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by DS on 2022/04/04 13:16
 */
@WebServlet({"/user/login.do"})
public class UserLoginController extends HttpServlet {
    SimpleDateFormat sdf;
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if ("/user/login.do".equals(servletPath)){
            login(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String loginAct = request.getParameter("loginName");
        String loginPwd = request.getParameter("loginPwd");
        loginPwd = MD5Util.getMD5(loginPwd);
        String ip = request.getRemoteAddr();

        try {
            UserService us = new UserServiceImpl();
            Users users = null;
            users = us.login(loginAct, loginPwd, ip);

            HttpSession session = request.getSession();
            session.setAttribute("user", users);
            Map<String, Boolean> map = new HashMap();
            map.put("success", true);
            ObjectMapper om = new ObjectMapper();
            String json = om.writeValueAsString(map);
            response.getWriter().print(json);
        } catch (LoginException e) {
            String msg = e.getMessage();
            Map<String, Object> map = new HashMap();
            map.put("success",false);
            map.put("msg", msg);
            ObjectMapper om = new ObjectMapper();
            String json = om.writeValueAsString(map);
            response.getWriter().print(json);
        }

    }
}
