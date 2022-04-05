package com.myself.crm.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create by DS on 2022/04/04 13:32
 */
@WebFilter("*.do")
public class EncodingFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.setContentType("text/html;charst=UTF-8");
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }
}
