package com.hmily.controller.common;

import com.hmily.common.ServerResponse;
import com.hmily.util.curator.ZKCurator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test/")
@Slf4j
public class TestController {

    @Autowired
    private ZKCurator zkCurator;

    /**
     * @Description: 判断zk是否连接
     */
    @RequestMapping("/isZKAlive")
    @ResponseBody
    public ServerResponse isZKAlive() {
        boolean isAlive = zkCurator.isZKAlive();
        String result = isAlive ? "连接" : "断开";
        log.info("判断zk是否连接: {}", result);
        return ServerResponse.createBySuccess(result);
    }
}
