package com.hmily.service.impl;

import com.hmily.dao.UserMapper;
import com.hmily.pojo.User;
import com.hmily.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
