package com.hmily.service.impl;

import com.hmily.common.ResponseCode;
import com.hmily.common.ServerResponse;
import com.hmily.dao.UserMapper;
import com.hmily.pojo.User;
import com.hmily.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse selectByPrimaryKey(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if(null == user){
            return ServerResponse.createByErrorMessage("没有该用户！");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }
}
