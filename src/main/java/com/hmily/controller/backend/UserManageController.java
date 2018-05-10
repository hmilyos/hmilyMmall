package com.hmily.controller.backend;

import com.hmily.common.Const;
import com.hmily.common.ServerResponse;
import com.hmily.pojo.User;
import com.hmily.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user/")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> res = iUserService.login(username, password);
        if(res.isSuccess()){
            User user = res.getData();
            if(Const.Role.ROLE_ADMIN == user.getRole()){
                session.setAttribute(Const.CURRENT_USER, user);
            }
            else {
                return ServerResponse.createByErrorMessage("不是管理员，无法登陆");
            }
        }
        return res;
    }
}