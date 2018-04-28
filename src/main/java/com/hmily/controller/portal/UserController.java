package com.hmily.controller.portal;

import com.hmily.common.ServerResponse;
import com.hmily.pojo.User;
import com.hmily.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user/")
@Slf4j
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "selectByPrimaryKey.do")
    @ResponseBody
    public ServerResponse selectByPrimaryKey(Integer id){
        log.info("selectByPrimaryKey--" + id);
        if(id == null){
            return  ServerResponse.createByErrorMessage("id不能为空！");
        }
        ServerResponse res = iUserService.selectByPrimaryKey(id);
        return res;
    }
}
