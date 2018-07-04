package com.hmily.service.impl;

import com.hmily.common.Const;
import com.hmily.common.ServerResponse;
import com.hmily.common.TokenCache;
import com.hmily.dao.UserMapper;
import com.hmily.pojo.User;
import com.hmily.service.IUserService;
import com.hmily.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("iUserService")
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        if (null == username || null == password || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ServerResponse.createByErrorMessage("用户名和密码不能为空");
        }

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }

        String md5Pwd = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectByUsernameAndPwd(username, md5Pwd);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        if (null == user) {
            return ServerResponse.createByErrorMessage("参数不能全为空");
        }
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getEmail())) {
            return ServerResponse.createByErrorMessage("用户名、密码、手机、邮箱不能为空");
        }

        //
        ServerResponse<String> checkUsernameResult = checkValid(user.getUsername(), Const.USERNAME);
        if (!checkUsernameResult.isSuccess()) {
            return checkUsernameResult;
        }

        ServerResponse<String> checkEmailResult = checkValid(user.getEmail(), Const.EMAIL);
        if (!checkEmailResult.isSuccess()) {
            return checkEmailResult;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int row = userMapper.insert(user);
        if (row == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isBlank(str)) {
            return ServerResponse.createByErrorMessage(type == Const.USERNAME ? "用户名" : "邮箱" + "不能为空");
        }

        if (type == Const.USERNAME) {
            int count = userMapper.checkUsername(str);
            if (count != 0) {
                return ServerResponse.createByErrorMessage("该用户名已被注册");
            }
            return ServerResponse.createBySuccess();
        }

        if (type == Const.EMAIL) {
            int count = userMapper.checkEmail(str);
            if (count != 0) {
                return ServerResponse.createByErrorMessage("该邮箱已被注册");
            }
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createBySuccessMessage("参数有误");
    }

    @Override
    public ServerResponse<String> forgetGetQuestion(String username) {
        if (StringUtils.isBlank(username)) {
            return ServerResponse.createByErrorMessage("用户名不能为空");
        }
        int count = userMapper.checkUsername(username);
        if (count == 0) {
            return ServerResponse.createByErrorMessage("该用户名不存在");
        }
        return ServerResponse.createBySuccess(userMapper.selectQuestionByUsername(username));
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int count = userMapper.checkAnswer(username, question, answer);
        if (0 == count) {
            return ServerResponse.createByErrorMessage("找回密码答案错误");
        }
        String forgetToken = UUID.randomUUID().toString();
        TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
        return ServerResponse.createBySuccess(forgetToken);
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误,token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }

        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);

            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");
        }

        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        if (StringUtils.isNotBlank(user.getEmail())) {
            int count = userMapper.checkEmail(user.getEmail());
            if (count > 0) {
                int row = userMapper.checkEmailAndId(user.getEmail(), user.getId());
                if (row > 0) {
                    //没修改邮箱，传入的邮箱和原来的一样
                    user.setEmail(null);
                } else {
                    ServerResponse.createByErrorMessage("该邮箱已被注册");
                }
            }
        } else {
            user.setEmail(null);
        }
        int row = userMapper.updateByPrimaryKeySelective(user);
        if (row == 0) {
            ServerResponse.createByErrorMessage("更新个人信息失败");
        }

        User updateUser = userMapper.selectByPrimaryKey(user.getId());
        updateUser.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(updateUser);
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (null == user) {
            return ServerResponse.createByErrorMessage("没有该用户！");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        if (null != user && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }


    @Override
    public ServerResponse testTime() {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        log.info("当前时间：" + format.format(new Date()));

        List<User> byTime = userMapper.getByTime(format.format(new Date()));

        log.info("当前时间注册的：" + byTime.size());

        //过去3天
        c.setTime(new Date());
        c.add(Calendar.DATE, -3);
        Date d3 = c.getTime();
        String day3 = format.format(d3);
        log.info("过去3天：" + day3);

        //过去七天
        c.setTime(new Date());
        c.add(Calendar.DATE, -7);
        Date d = c.getTime();
        String day = format.format(d);
        log.info("过去七天：" + day);

        List<User> byTime7 = userMapper.getByTime(day);

        log.info("过去七天内注册的：" + byTime7.size());
        //过去一月
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        String mon = format.format(m);
        log.info("过去一个月：" + mon);

        //过去三个月
        c.setTime(new Date());
        c.add(Calendar.MONTH, -3);
        Date m3 = c.getTime();
        String mon3 = format.format(m3);
        log.info("过去三个月：" + mon3);

        //过去一年
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        Date y = c.getTime();
        String year = format.format(y);
        log.info("过去一年：" + year);
        return ServerResponse.createBySuccess();
    }


}
