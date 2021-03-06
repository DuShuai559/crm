package com.myself.crm.workbench.activity.service.impl;

import com.myself.crm.setting.domain.Users;
import com.myself.crm.vo.ShowActivityVo;
import com.myself.crm.workbench.activity.domain.Activity;
import com.myself.crm.workbench.activity.mapper.ActivityMapper;
import com.myself.crm.workbench.activity.mapper.ActivityRemarkMapper;
import com.myself.crm.workbench.activity.service.ActivityService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Create by DS on 2022/04/06 11:50
 */
public class ActivityServiceImpl implements ActivityService {
    InputStream in;
    SqlSessionFactory factory;
    SqlSession sqlSession;
    {
        try {
            in = Resources.getResourceAsStream("SqlMapConfig.xml");
            factory = new SqlSessionFactoryBuilder().build(in);
            sqlSession = factory.openSession();;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public List<Users> getUser() throws IOException {
        ActivityMapper activityMapper = sqlSession.getMapper(ActivityMapper.class);
        List<Users> userList = activityMapper.getAllUser();
        sqlSession.close();
        return userList;
    }

    @Override
    public int saveActivity(Activity activity) throws IOException {
        ActivityMapper activityMapper = sqlSession.getMapper(ActivityMapper.class);
        int i = activityMapper.saveActivity(activity);
        sqlSession.commit();
        sqlSession.close();
        return i;
    }

    @Override
    public List<Activity> queryActivity(Map map) throws IOException {
        ActivityMapper activityMapper = sqlSession.getMapper(ActivityMapper.class);
        List<Activity> activityList = activityMapper.queryActivity(map);
        return activityList;
    }

    @Override
    public Integer countActivity(Map<String, Object> map) throws IOException {
        sqlSession = factory.openSession();
        ActivityMapper activityMapper = sqlSession.getMapper(ActivityMapper.class);
        int countActivity = activityMapper.countActivity(map);
        return countActivity;
    }

    @Override
    public boolean deleteActivity(String[] ids) throws IOException {
        boolean flag = false;
        ActivityMapper activityMapper = sqlSession.getMapper(ActivityMapper.class);
        //????????????????????????activity???????????????activity_remark????????????????????????
        ActivityRemarkMapper activityRemarkMapper = sqlSession.getMapper(ActivityRemarkMapper.class);

        //?????????????????????????????????????????????????????????
        int realCount = activityRemarkMapper.deleteRemarkForActivity(ids);
        //??????????????????
        int delCount = activityRemarkMapper.deleteRemark(ids);

        //?????????????????????????????????????????????????????????????????????,??????????????????????????????
        if (delCount == realCount){
            int activityCount = activityMapper.deleteActivity(ids);
            //????????????????????????????????????????????????????????????????????????
            if (activityCount == ids.length){
                flag = true;
            }
            if (flag){
                //???????????????????????????
                sqlSession.commit();
           //?????????????????????
            }else {
                sqlSession.rollback();
            }
        }
        return flag;
    }
}
