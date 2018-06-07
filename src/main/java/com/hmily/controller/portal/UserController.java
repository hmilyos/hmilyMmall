package com.hmily.controller.portal;

import com.hmily.common.Const;
import com.hmily.common.ResponseCode;
import com.hmily.common.ServerResponse;
import com.hmily.pojo.User;
import com.hmily.service.IUserService;
import com.hmily.util.CookieUtil;
import com.hmily.util.JsonUtil;
import com.hmily.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
@RequestMapping("/user/")
@Slf4j
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping("test.do")
    @ResponseBody
    public void test() {
        try {
            iUserService.test();
            throw  new SQLException("hehheh");
        } catch (SQLException e) {
           log.error("test: {}", e);
        }
    }

    @RequestMapping(value = "getInformation.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getInformation(Integer id){
        if(id == null){
            return  ServerResponse.createByErrorMessage("id不能为空！");
        }
        ServerResponse res = iUserService.getInformation(id);
        return res;
    }

    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
//        public ServerResponse<User> login(@RequestBody String username, @RequestBody String password, HttpSession session){

        ServerResponse<User> res = iUserService.login(username, password);
        if(res.isSuccess()) {
            CookieUtil.writeLoginToken(httpServletResponse, session.getId());
            RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.objToString(res.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return res;
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return  iUserService.register(user);
    }

    @RequestMapping(value = "checkValid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type){
        return iUserService.checkValid(str, type);
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest request, HttpServletResponse response){
        String loginToken = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request, response);
        RedisShardedPoolUtil.del(loginToken);
        return ServerResponse.createBySuccessMessage("退出成功");
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest request){
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if(null == user){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        return ServerResponse.createBySuccess(user);
    }

    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.forgetGetQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return iUserService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "forget_rest_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken){
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, HttpServletRequest request){
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if(null == user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }

    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(User user, HttpServletRequest request){
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User currentUser = JsonUtil.stringToObj(userInfoStr, User.class);
        if(currentUser == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        user.setPassword(null);
        user.setRole(Const.Role.ROLE_CUSTOMER);
        ServerResponse<User> res = iUserService.updateInformation(user);
        if(res.isSuccess()){
            RedisShardedPoolUtil.setEx(key, JsonUtil.objToString(res.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return res;
    }


    @RequestMapping(value = "get_information.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpServletRequest request){
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if(null == user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
        }
        return ServerResponse.createBySuccess(user);
    }
}
