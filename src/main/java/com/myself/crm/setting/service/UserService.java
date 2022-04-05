package com.myself.crm.setting.service;

import com.myself.crm.setting.domain.Users;
import com.myself.crm.setting.exception.LoginException;

import java.io.IOException;

/**
 * Create by DS on 2022/04/05 14:01
 */
public interface UserService {
    Users login(String loginAct, String loginPwd, String ip) throws IOException, LoginException;
}
