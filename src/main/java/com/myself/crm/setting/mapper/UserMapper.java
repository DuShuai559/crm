package com.myself.crm.setting.mapper;

import com.myself.crm.setting.domain.Users;

import java.util.Map;

/**
 * Create by DS on 2022/04/04 14:05
 */
public interface UserMapper {
    Users checkLogin(Map<String , String> map);
}
