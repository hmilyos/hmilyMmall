package com.hmily.controller.portal;

import com.hmily.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "selectByPrimaryKey.do")
    @ResponseBody
    public Object selectByPrimaryKey(Integer id){
        return iUserService.selectByPrimaryKey(id);
    }
}
