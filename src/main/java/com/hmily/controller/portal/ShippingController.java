package com.hmily.controller.portal;

import com.github.pagehelper.PageInfo;
import com.hmily.common.Const;
import com.hmily.common.ResponseCode;
import com.hmily.common.ServerResponse;
import com.hmily.pojo.Shipping;
import com.hmily.pojo.User;
import com.hmily.service.IShippingService;
import com.hmily.util.CookieUtil;
import com.hmily.util.JsonUtil;
import com.hmily.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> add(Shipping shipping, HttpServletRequest request){
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse<String> del(HttpServletRequest request, Integer shippingId) {
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.del(user.getId(), shippingId);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<String> update(HttpServletRequest request, Shipping shipping) {
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.update(user.getId(), shipping);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<Shipping> select(HttpServletRequest request, Integer shippingId) {
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.select(user.getId(), shippingId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         HttpServletRequest request) {
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.list(user.getId(), pageNum, pageSize);
    }
}
