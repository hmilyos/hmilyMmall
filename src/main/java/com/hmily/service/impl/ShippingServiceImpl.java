package com.hmily.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hmily.common.ServerResponse;
import com.hmily.dao.ShippingMapper;
import com.hmily.pojo.Shipping;
import com.hmily.service.IShippingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iShippingService")
@Slf4j
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse<String> add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int row = shippingMapper.insert(shipping);
        if(row > 0){
            return ServerResponse.createBySuccess("新建地址成功");
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse<String> del(Integer userId, Integer shippingId) {
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId, shippingId);
        if (resultCount > 0) {
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    @Override
    public ServerResponse<String> update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId, shippingId);
        if (shipping == null) {
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccess(shipping);
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
