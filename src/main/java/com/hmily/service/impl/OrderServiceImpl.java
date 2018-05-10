package com.hmily.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hmily.common.Const;
import com.hmily.common.ServerResponse;
import com.hmily.dao.*;
import com.hmily.pojo.*;
import com.hmily.service.IOrderService;
import com.hmily.util.BigDecimalUtil;
import com.hmily.util.DateTimeUtil;
import com.hmily.util.FTPUtil;
import com.hmily.util.PropertiesUtil;
import com.hmily.vo.OrderItemVo;
import com.hmily.vo.OrderProductVo;
import com.hmily.vo.OrderVo;
import com.hmily.vo.ShippingVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service("iOrderService")
@Slf4j
public class OrderServiceImpl implements IOrderService {

    private static AlipayTradeService tradeService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse<PageInfo> manageList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        //根据时间倒序，分页查找订单
        List<Order> orderList = orderMapper.selectAllOrder();
        //根据订单集合去找订单详情集合
        List<OrderVo> orderVoList = this.assembleOrderVoList(orderList,null);
        //返回前端
        PageInfo pageResult = new PageInfo(orderList);
        pageResult.setList(orderVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    //根据订单集合和userId（若无userId，即后台管理员查看所有订单）去查找订单详情，并且转成OrderVo的集合
    private List<OrderVo> assembleOrderVoList(List<Order> orderList, Integer userId){
        List<OrderVo> orderVoList = Lists.newArrayList();
        //遍历订单集合
        for(Order order : orderList){
            List<OrderItem>  orderItemList = Lists.newArrayList();
            //根据有没有传userId，判断是管理员查询还是用户自己查自己的
            if(userId == null){
                //todo 管理员查询的时候 不需要传userId
                orderItemList = orderItemMapper.getByOrderNo(order.getOrderNo());
            }else{
                orderItemList = orderItemMapper.getByOrderNoUserId(order.getOrderNo(),userId);
            }
            //转一下，获取生成的订单信息，详细信息，地址等详细信息
            OrderVo orderVo = assembleOrderVo(order, orderItemList);
            //加到集合里
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

    //把生成的订单信息，详细信息，地址，都返回给前端
    private OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList){
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());

        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());

        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if(shipping != null){
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShippingVo(assembleShippingVo(shipping));
        }

        orderVo.setPaymentTime(DateTimeUtil.dateToStr(order.getPaymentTime()));
        orderVo.setSendTime(DateTimeUtil.dateToStr(order.getSendTime()));
        orderVo.setEndTime(DateTimeUtil.dateToStr(order.getEndTime()));
        orderVo.setCreateTime(DateTimeUtil.dateToStr(order.getCreateTime()));
        orderVo.setCloseTime(DateTimeUtil.dateToStr(order.getCloseTime()));


        orderVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));


        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        for(OrderItem orderItem : orderItemList){
            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    //把地址转成更详细点
    private ShippingVo assembleShippingVo(Shipping shipping){
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverMobile(shipping.getReceiverMobile());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        shippingVo.setReceiverPhone(shippingVo.getReceiverPhone());
        return shippingVo;
    }

    //把订单详情转成更详细点
    private OrderItemVo assembleOrderItemVo(OrderItem orderItem){
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());

        orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }

    @Override
    public ServerResponse<OrderVo> manageDetail(Long orderNo) {
        //根据订单号查看订单详情
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order != null){
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
            OrderVo orderVo = assembleOrderVo(order,orderItemList);
            return ServerResponse.createBySuccess(orderVo);
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }

    @Override
    public ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        //这里的搜索是有问题的，应该可以根据订单号进行模糊搜索！！！
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order != null){
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
            OrderVo orderVo = assembleOrderVo(order,orderItemList);

            PageInfo pageResult = new PageInfo(Lists.newArrayList(order));
            pageResult.setList(Lists.newArrayList(orderVo));
            return ServerResponse.createBySuccess(pageResult);
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }

    @Override
    public ServerResponse<String> manageSendGoods(Long orderNo) {
        //先判断一下是否存在这条订单
        Order order= orderMapper.selectByOrderNo(orderNo);
        if(order != null){
            //存在这条订单就根据订单号修改成已发货状态
            if(order.getStatus() == Const.OrderStatusEnum.PAID.getCode()){
                order.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
                order.setSendTime(new Date());
                orderMapper.updateByPrimaryKeySelective(order);
                return ServerResponse.createBySuccess("发货成功");
            }
        }
        return ServerResponse.createByErrorMessage("订单不存在");
    }

    /***********用户接口*************/
    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        //从购物车中获取数据, 根于用户id去购物车查找被勾选上的商品
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);

        //从被选中的购物车集合里面获取商品信息，转成订单详细的实体集合
        ServerResponse serverResponse = this.getCartOrderItem(userId, cartList);
        //判断这里商品里面是否有商品下架，库存不足的情况，返回对应错误提示
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        //获取本订单明细实体集合
        List<OrderItem> orderItemList = (List<OrderItem>)serverResponse.getData();
        //判断本订单是否包含有效商品
        if(CollectionUtils.isEmpty(orderItemList)){
            return ServerResponse.createByErrorMessage("购物车为空");
        }
        //计算这个订单的总价
        BigDecimal payment = this.getOrderTotalPrice(orderItemList);
        //生成订单
        Order order = this.assembleOrder(userId, shippingId, payment);
        if(order == null){
            return ServerResponse.createByErrorMessage("生成订单错误");
        }
        //生成订单成功，把订单id回填到订单详细实体的集合里面
        for(OrderItem orderItem : orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }
        //mybatis 批量插入订单详细数据
        int row = orderItemMapper.batchInsert(orderItemList);

        //订单生成成功,我们要减少我们产品的库存
        this.reduceProductStock(orderItemList);

        //清空一下购物车里面被勾选上的商品
        this.cleanCart(cartList);

        //返回给前端数据
        //把生成的订单信息，详细信息， 本次购买的商品详情，地址详情，都返回给前端
        OrderVo orderVo = assembleOrderVo(order, orderItemList);
        return ServerResponse.createBySuccess(orderVo);
    }

    //从被选中的购物车集合里面获取商品信息，转成订单详细的实体集合，要进行一些商品信息的判断
    private ServerResponse getCartOrderItem(Integer userId, List<Cart> cartList){
        //判断传入的购物车集合是否为空
        if(CollectionUtils.isEmpty(cartList)){
            return ServerResponse.createByErrorMessage("购物车为空");
        }
        //校验购物车的数据, 包括产品的状态和数量
        List<OrderItem> orderItemList = Lists.newArrayList();
        for (Cart cartItem: cartList) {
            OrderItem orderItem = new OrderItem();
            //根据购物车里面的商品id，去查找该商品的信息
            Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            //判断该商品是不是售卖状态
            if(Const.ProductStatusEnum.ON_SALE.getCode() != product.getStatus()){
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "不是在线售卖状态");
            }
            //校验库存，库存不足时，返回该商品库存不足的提示
            if(cartItem.getQuantity() > product.getStock()){
                return ServerResponse.createByErrorMessage("产品" + product.getName() + "库存不足");
            }
            //把商品信息做一下快照，放到订单详细实体里面
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartItem.getQuantity()));
            //把用户id也放到订单详细实体里面
            orderItem.setUserId(userId);
            //把订单详细实体添加到此次订单详细集合里面
            orderItemList.add(orderItem);
        }
        //返回订单详细的集合
        return ServerResponse.createBySuccess(orderItemList);
    }

    //根据传入的订单明细集合，计算本订单的总钱数
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList){
        BigDecimal payment = new BigDecimal("0");
        for(OrderItem orderItem : orderItemList){
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    //根据用户id、收货地址id，订单总钱数，生成一条订单记录
    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment){
        Order order = new Order();
        //生成一个不重复的订单id
        long orderNo = this.generateOrderNo();
        order.setOrderNo(orderNo);
        //订单实体的状态为未支付状态
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        //支付类型为在线支付
        order.setPaymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        //本订单的总钱数
        order.setPayment(payment);

        order.setUserId(userId);
        //当前的收货地址id
        order.setShippingId(shippingId);
        //发货时间等等
        //付款时间等等
        //把填充好的订单信息存到订单表里面
        int rowCount = orderMapper.insert(order);
        //判断一下本订单是否持久化成功
        if(rowCount > 0){
            return order;
        }
        return null;
    }

    //生成一个不重复的id
    private long generateOrderNo(){
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    //订单生成成功，要对订单里面的商品进行修改库存
    private void reduceProductStock(List<OrderItem> orderItemList){
        for(OrderItem orderItem : orderItemList){
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(product);
        }
    }

    //订单生成成功，清空一下购物车里面被勾选上的商品
    private void cleanCart(List<Cart> cartList){
        for(Cart cart : cartList){
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }



    @Override
    public ServerResponse<String> cancel(Integer userId, Long orderNo) {
        //根据用户id和订单号去查询是否有这条订单
        Order order  = orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
        if(order == null){
            return ServerResponse.createByErrorMessage("该用户此订单不存在");
        }
        //有该订单，还有判断一下是否还在未付款状态，不是未付款就不能取消订单
        if(order.getStatus() != Const.OrderStatusEnum.NO_PAY.getCode()){
            return ServerResponse.createByErrorMessage("已付款,无法取消订单");
        }
        Order updateOrder = new Order();
        //订单还处在未付款状态，那么就可以取消订单，根据订单id、去修改订单状态

        updateOrder.setId(order.getId());
        updateOrder.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
        int row = orderMapper.updateByPrimaryKeySelective(updateOrder);
        //还要去把该订单减掉的库存加上（暂时未写加库存的方法）
        if(row > 0){
            //还要去把减掉的库存加回去！！！！！！！
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse getOrderCartProduct(Integer userId) {
        OrderProductVo orderProductVo = new OrderProductVo();
        //从购物车中获取数据, 把购物车里面被选中的商品找出来
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        //从被选中的购物车集合里面获取商品信息，转成订单详细的实体集合，要进行一些商品信息的判断
        ServerResponse serverResponse =  this.getCartOrderItem(userId, cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        //订单详情集合获取成功了
        //取出订单详情集合
        List<OrderItem> orderItemList =( List<OrderItem> ) serverResponse.getData();

        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        //计算该订单的总钱数，以及把订单详细实体转一下，增加一些信息
        BigDecimal payment = new BigDecimal("0");
        for(OrderItem orderItem : orderItemList){
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVoList.add(assembleOrderItemVo(orderItem));
        }
        orderProductVo.setProductTotalPrice(payment);
        orderProductVo.setOrderItemVoList(orderItemVoList);
        orderProductVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        //返回订单—商品的Vo
        return ServerResponse.createBySuccess(orderProductVo);
    }

    @Override
    public ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo) {
        //根据userId和订单号去查找该订单信息
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        //判断是否有该订单
        if(order != null){
            //有该订单
            //去查该订单的详细信息
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);
            //把订单的详细信息实体再转一下，更详细
            OrderVo orderVo = assembleOrderVo(order, orderItemList);
            //返回订单的最详细信息
            return ServerResponse.createBySuccess(orderVo);
        }
        return  ServerResponse.createByErrorMessage("没有找到该订单");
    }

    @Override
    public ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize) {
        //要先对分页插件设置第几页，每页条数
        PageHelper.startPage(pageNum, pageSize);
        //根据用户id去查找订单集合，注意要根据时间倒序
        List<Order> orderList = orderMapper.selectByUserId(userId);
        //根据订单id和用户id去查找这些订单的订单详情表信息，并转一下成OrderVo集合
        List<OrderVo> orderVoList = assembleOrderVoList(orderList, userId);
        PageInfo pageResult = new PageInfo(orderList);
        pageResult.setList(orderVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse pay(Long orderNo, Integer userId, String path) {
        // 判断用户是否有该订单
        Map<String ,String> resultMap = Maps.newHashMap();
        Order order = orderMapper.selectByUserIdAndOrderNo(userId,orderNo);
        if(order == null){
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }

        resultMap.put("orderNo",String.valueOf(order.getOrderNo()));
        //2.根据支付宝的demo里面的方法进行修改，然后生成支付二维码

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo =  order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("mall商场扫码支付，订单号：").append(outTradeNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单：").append(outTradeNo).append("购买商品共").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        //获取该订单的详情
        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        for (OrderItem orderItem: orderItemList) {
            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                    BigDecimalUtil.mul(orderItem.getCurrentUnitPrice().doubleValue(), new Double(100)).longValue(),
                    orderItem.getQuantity());
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);
        }


        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl("alipay_callback.do")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                File folder = new File(path);
                if(!folder.exists()){
                    folder.setWritable(true);
                    folder.mkdirs();
                }

                // 需要修改为运行机器上的路径
                //细节细节细节
                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                //在项目录下生成支付二维码
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                File targetFile = new File(path, qrFileName);
                //把生成的二维码图片上传到ftp服务器
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    log.error("上传二维码异常",e);
                    return ServerResponse.createByErrorMessage("上传二维码异常");
                }
                log.info("qrPath:" + qrPath);
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFile.getName();
                resultMap.put("qrUrl", qrUrl);
                //至此，生成和上传支付二维码已经成功了，返回图片信息给前端展示
                return ServerResponse.createBySuccess(resultMap);
            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }

    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    @Override
    public ServerResponse aliCallback(Map<String, String> params) {
        //订单号
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        //订单状态
        String tradeStatus = params.get("trade_status");
        //根据订单号查找订单信息
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            //找不到该订单
            return ServerResponse.createByErrorMessage("非快乐慕商城的订单,回调忽略");
        }
        //判断订单的状态，防止出现，订单已发货，支付宝还在调用我们的回调，导致状态修改成已付款的情况
        if(order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess("支付宝重复调用");
        }

        if(Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
            //不是重复调用，根据支付宝传回来的信息，修改订单信息（支付时间、状态）
            order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
            order.setStatus(Const.OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }

        //去支付表里面插入对应的支付信息。
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);

        payInfoMapper.insert(payInfo);

        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if(order == null){
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }
        if(order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
