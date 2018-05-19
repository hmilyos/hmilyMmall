package com.hmily.controller.portal;

import com.hmily.common.Const;
import com.hmily.common.ResponseCode;
import com.hmily.common.ServerResponse;
import com.hmily.pojo.User;
import com.hmily.service.ICartService;
import com.hmily.util.CookieUtil;
import com.hmily.util.JsonUtil;
import com.hmily.util.RedisShardedPoolUtil;
import com.hmily.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private ICartService iCartService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpServletRequest request) {
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpServletRequest request, Integer count, Integer productId) {
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(), productId, count);
    }


    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpServletRequest request, Integer count, Integer productId) {
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.update(user.getId(), productId, count);
    }

    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpServletRequest request, String productIds) {
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }

    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpServletRequest request) {
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
    }

    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest request) {
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpServletRequest request, Integer productId) {
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
    }

    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpServletRequest request, Integer productId) {
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.UN_CHECKED);
    }

    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest request) {
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }


}
