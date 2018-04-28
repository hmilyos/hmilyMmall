package com.hmily.service;

import com.hmily.common.ServerResponse;
import com.hmily.pojo.User;

public interface IUserService {
    ServerResponse<User> selectByPrimaryKey(Integer id);
}
