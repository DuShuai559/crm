package com.myself.crm.workbench.activity.service;

import com.myself.crm.setting.domain.Users;
import com.myself.crm.workbench.activity.domain.Activity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Create by DS on 2022/04/06 11:48
 */
public interface ActivityService {
    List<Users> getUser() throws IOException;

    int saveActivity(Activity activity) throws IOException;

    List<Activity> queryActivity(Map<String, Object> map) throws IOException;

    Integer countActivity(Map<String, Object> map) throws IOException;

    boolean deleteActivity(String[] ids) throws IOException;
}
