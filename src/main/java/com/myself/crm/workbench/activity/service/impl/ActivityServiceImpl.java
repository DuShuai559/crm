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
        //删子先删父。删除activity之前先要将activity_remark表的关联记录删除
        ActivityRemarkMapper activityRemarkMapper = sqlSession.getMapper(ActivityRemarkMapper.class);

        //应该删除的行数。先查再删，不然查询不到
        int realCount = activityRemarkMapper.deleteRemarkForActivity(ids);
        //删除掉的行数
        int delCount = activityRemarkMapper.deleteRemark(ids);

        //如果删除掉的行数等于应删除的行数，表示删除成功,可以继续删除主表记录
        if (delCount == realCount){
            int activityCount = activityMapper.deleteActivity(ids);
            //如果删除的主表的记录数等于数组长度，说明删除成功
            if (activityCount == ids.length){
                flag = true;
            }
            if (flag){
                //如果删除成功则提交
                sqlSession.commit();
           //删除失败则回滚
            }else {
                sqlSession.rollback();
            }
        }
        return flag;
    }
}
