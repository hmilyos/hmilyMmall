package com.hmily.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.hmily.common.Const;
import com.hmily.common.ResponseCode;
import com.hmily.common.ServerResponse;
import com.hmily.pojo.User;
import com.hmily.service.IOrderService;
import com.hmily.util.CookieUtil;
import com.hmily.util.JsonUtil;
import com.hmily.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/order/")
@Slf4j
public class OrderController {

    @Autowired
    private IOrderService iOrderService;



    /**
     * 创建订单
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponse create(HttpServletRequest request, Integer shippingId){
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.createOrder(user.getId(),shippingId);
    }

    /**
     * 取消订单
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("cancel.do")
    @ResponseBody
    public ServerResponse cancel(HttpServletRequest request, Long orderNo){
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancel(user.getId(), orderNo);
    }

    /**
     *.获取订单的商品信息
     * @param session
     * @return
     */
    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpServletRequest request){
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    /**
     * 获取某条订单详情
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpServletRequest request, Long orderNo){
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderDetail(user.getId(),orderNo);
    }

    /**
     * 用户分页查看订单
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpServletRequest request, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(),pageNum,pageSize);
    }


    /**
     * 根据用户和订单id生成支付二维码图片，再把图片上传到ftp服务器，最后把图片路径给到前端
     * @param session
     * @param orderNo
     * @param request
     * @return
     */
    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(Long orderNo, HttpServletRequest request){
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo, user.getId(), path);
    }

    /**
     * 给支付宝的回调
     * @param request
     * @return
     */
    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
        Map<String,String> params = Maps.newHashMap();

        //从支付宝返回的HttpServletRequest里面获取所有参数信息
        Map requestParams = request.getParameterMap();
        //用迭代器遍历requestParams，把里面的信息放到HashMap里面 key:name ,value: valueStr
        for (Iterator iterator = requestParams.keySet().iterator(); iterator.hasNext();){
            String name = (String)iterator.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for(int i = 0 ; i <values.length;i++){
                valueStr = (i == values.length -1) ? valueStr + values[i] : valueStr + values[i]+",";
            }
            params.put(name, valueStr);
        }

        log.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        //验签非常重要,验证回调的正确性,是不是支付宝发的.并且还要避免重复通知.
        //验签要把刚才迭代器得到的map里面的sign_type去掉，要不然会验签失败
        params.remove("sign_type");
        try {
            //验签
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            //验签结果
            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求,验证不通过,再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常",e);
        }

        //todo 验证各种数据
        //最好是除了sign_type、sign外的数据都验证一遍，（暂时未写验证）

        //
        ServerResponse serverResponse = iOrderService.aliCallback(params);
        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    /**
     * 给前端用于查询订单状态
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpServletRequest request, Long orderNo){
        String key = CookieUtil.readLoginToken(request);
        if(StringUtils.isBlank(key)){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        String userInfoStr = RedisShardedPoolUtil.get(key);
        User user = JsonUtil.stringToObj(userInfoStr, User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(),orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }
}
