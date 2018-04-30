package com.hmily.service.impl;

import com.hmily.common.Const;
import com.hmily.common.ResponseCode;
import com.hmily.common.ServerResponse;
import com.hmily.dao.UserMapper;
import com.hmily.pojo.User;
import com.hmily.service.IUserService;
import com.hmily.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        if(null == username || null == password || StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return ServerResponse.createByErrorMessage("用户名和密码不能为空");
        }

        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }

        String md5Pwd = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectByUsernameAndPwd(username, md5Pwd);

        if(user == null){
            return  ServerResponse.createByErrorMessage("用户名或密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        log.info(user.toString());
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        if(null == user){
            return ServerResponse.createByErrorMessage("参数不能全为空");
        }
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getEmail())){
            return ServerResponse.createByErrorMessage("用户名、密码、手机、邮箱不能为空");
        }

        //
        ServerResponse<String> checkUsernameResult = checkValid(user.getUsername(), Const.USERNAME);
        if(!checkUsernameResult.isSuccess()){
            return checkUsernameResult;
        }

        ServerResponse<String> checkEmailResult = checkValid(user.getEmail(), Const.EMAIL);
        if(!checkEmailResult.isSuccess()){
            return checkEmailResult;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int row = userMapper.insert(user);
        if(row == 0){
            return  ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if(StringUtils.isBlank(str)){
            return ServerResponse.createByErrorMessage(type == Const.USERNAME ? "用户名" : "邮箱" +  "不能为空");
        }

        if(type == Const.USERNAME){
            int count = userMapper.checkUsername(str);
            if(count != 0){
                return ServerResponse.createByErrorMessage("该用户名已被注册");
            }
            return ServerResponse.createBySuccess();
        }

        if(type == Const.EMAIL){
            int count = userMapper.checkEmail(str);
            if(count != 0){
                return ServerResponse.createByErrorMessage("该邮箱已被注册");
            }
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createBySuccessMessage("参数有误");
    }

    @Override
    public ServerResponse<String> forgetGetQuestion(String username) {
        return null;
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        return null;
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return null;
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        return null;
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        return null;
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(null == user){
            return ServerResponse.createByErrorMessage("没有该用户！");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        return null;
    }
}
