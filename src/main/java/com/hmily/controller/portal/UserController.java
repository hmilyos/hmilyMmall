package com.hmily.controller.portal;

import com.hmily.common.Const;
import com.hmily.common.ServerResponse;
import com.hmily.pojo.User;
import com.hmily.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
@Slf4j
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "getInformation.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getInformation(Integer id){
        log.info("getInformation--id==" + id);
        if(id == null){
            return  ServerResponse.createByErrorMessage("id不能为空！");
        }
        ServerResponse res = iUserService.getInformation(id);
        return res;
    }

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> res = iUserService.login(username, password);
        if(res.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, res.getData());
        }
        return res;
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return  iUserService.register(user);
    }

}
