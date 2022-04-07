package com.myself.crm.workbench.activity.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myself.crm.setting.domain.Users;
import com.myself.crm.utils.DateTimeUtil;
import com.myself.crm.utils.UUIDUtil;
import com.myself.crm.vo.ShowActivityVo;
import com.myself.crm.workbench.activity.domain.Activity;
import com.myself.crm.workbench.activity.service.ActivityService;
import com.myself.crm.workbench.activity.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by DS on 2022/04/06 10:09
 */
@WebServlet({"/workbench/activity/getUser.do", "/workbench/activity/saveActivity.do", "/workbench/activity/queryActivity.do", "/workbench/activity/deleteActivity.do"})
public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/activity/getUser.do".equals(path)){
            getUser(request, response);
        }else if ("/workbench/activity/saveActivity.do".equals(path)){
            saveActivity(request, response);
        }else if ("/workbench/activity/queryActivity.do".equals(path)){
            queryActivity(request, response);
        }else if ("/workbench/activity/deleteActivity.do".equals(path)){
            deleteActivity(request, response);
        }
    }

    private void deleteActivity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] ids = request.getParameterValues("id");
        ActivityService activityService = new ActivityServiceImpl();
        boolean success = activityService.deleteActivity(ids);
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(success);
        response.getWriter().print(json);
    }

    private void queryActivity(HttpServletRequest request, HttpServletResponse response) {
        String pageNostr = request.getParameter("pageNo"); // 页码
        String pageSizestr = request.getParameter("pageSize"); //每页显示的记录数
        int pageNo = Integer.valueOf(pageNostr);
        int pageSize = Integer.valueOf(pageSizestr);
        //求跳过的记录数
        int skipSize = (pageNo - 1) * pageSize;
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        ActivityService as = new ActivityServiceImpl();
        try {
            Map<String , Object> map = new HashMap<>();
            map.put("skipSize", skipSize);
            map.put("pageSize", pageSize);
            map.put("name", name);
            map.put("owner", owner);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            List<Activity> activityList = as.queryActivity(map);
            Integer countSize = as.countActivity(map);
            ShowActivityVo<Activity> vo = new ShowActivityVo<>();
            vo.setCountSize(countSize);
            vo.setActivityData(activityList);
            ObjectMapper om = new ObjectMapper();
            String json = om.writeValueAsString(vo);
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveActivity(HttpServletRequest request, HttpServletResponse response) {
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createBy = request.getParameter("createBy");
        Activity activity = new Activity();
        activity.setId(UUIDUtil.getUUID());
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(DateTimeUtil.getSysTime());
        activity.setCreateBy(createBy);

        //调用业务层的方法 将activity对象传进去
        ActivityService as = new ActivityServiceImpl();
        try {
            int i = as.saveActivity(activity);
            if (i == 1){
                //插入成功，给前端响应成功信息
                Map<String, Boolean> map = new HashMap();
                map.put("success", true);
                ObjectMapper om = new ObjectMapper();
                String json = om.writeValueAsString(map);
                response.getWriter().print(json);
            }else {
                response.getWriter().print("alert('插入失败')");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getUser(HttpServletRequest request, HttpServletResponse response) {
        ActivityService activity = new ActivityServiceImpl();
        try {
            List<Users> usersList = activity.getUser();
            ObjectMapper om = new ObjectMapper();
            String json = om.writeValueAsString(usersList);
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
