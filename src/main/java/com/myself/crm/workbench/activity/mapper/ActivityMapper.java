package com.myself.crm.workbench.activity.mapper;

import com.myself.crm.setting.domain.Users;
import com.myself.crm.workbench.activity.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * Create by DS on 2022/04/06 11:54
 */
public interface ActivityMapper {
    List<Users> getAllUser();

    int saveActivity(Activity activity);

    List<Activity> queryActivity(Map<String, Object> map);

    int countActivity(Map<String, Object> map);

    int deleteActivity(String[] args);
}
