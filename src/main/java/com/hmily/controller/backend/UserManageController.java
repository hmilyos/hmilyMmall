package com.hmily.controller.backend;

import com.hmily.common.Const;
import com.hmily.common.ServerResponse;
import com.hmily.pojo.User;
import com.hmily.service.IUserService;
import com.hmily.util.CookieUtil;
import com.hmily.util.JsonUtil;
import com.hmily.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user/")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse) {
        ServerResponse<User> res = iUserService.login(username, password);
        if(res.isSuccess()){
            User user = res.getData();
            if(Const.Role.ROLE_ADMIN == user.getRole()){
                CookieUtil.writeLoginToken(httpServletResponse, session.getId());
                RedisShardedPoolUtil.setEx(session.getId(), JsonUtil.objToString(res.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
            else {
                return ServerResponse.createByErrorMessage("不是管理员，无法登陆");
            }
        }
        return res;
    }
}