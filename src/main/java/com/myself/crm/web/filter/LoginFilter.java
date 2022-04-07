package com.myself.crm.web.filter;

import com.myself.crm.setting.domain.Users;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Create by DS on 2022/04/05 21:52
 */
@WebFilter({"*.do", "*.jsp"})
public class LoginFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //首先将登录请求和访问login.jsp请求放行
        String path = request.getServletPath();
        if ("/login.jsp".equals(path) || "/user/login.do".equals(path)){
            chain.doFilter(request, response);
        }else {
            //取出session中的user，判断是否为空。
            HttpSession session = request.getSession();
            Users user = (Users) session.getAttribute("user");
            //如果user为空，表示没有登录过，重定向到登录页
            if (user == null){
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                //如果user不为空，表示登录过，放行
            }else {
                chain.doFilter(request, response);
            }
        }
    }
}
