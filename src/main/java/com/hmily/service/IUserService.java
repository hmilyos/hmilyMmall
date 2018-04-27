package com.hmily.service;

import com.hmily.pojo.User;

public interface IUserService {
    User selectByPrimaryKey(Integer id);
}
