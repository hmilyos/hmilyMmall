package com.hmily.service.impl;

import com.hmily.common.ServerResponse;
import com.hmily.service.ICartService;
import com.hmily.vo.CartVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("iCartService")
@Slf4j
public class CartServiceImpl implements ICartService {
    @Override
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        return null;
    }

    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
        return null;
    }

    @Override
    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds) {
        return null;
    }

    @Override
    public ServerResponse<CartVo> list(Integer userId) {
        return null;
    }

    @Override
    public ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked) {
        return null;
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        return null;
    }
}
