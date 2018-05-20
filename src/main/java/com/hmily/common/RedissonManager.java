package com.hmily.common;

import com.hmily.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class RedissonManager {
    private Config config = new Config();
    private Redisson redisson = null;
    public Redisson getRedisson() {
        return redisson;
    }
    private static String redisIps = PropertiesUtil.getProperty("rediss.ip");
    private static String redisPorts = PropertiesUtil.getProperty("rediss.port");

    @PostConstruct
    private void init(){
        if (StringUtils.isNotBlank(redisIps) && StringUtils.isNotBlank(redisPorts)){
            try {
                String[] ips = redisIps.split(";");
                String[] ports = redisPorts.split(";");
                for(int i = 0; i < ips.length; i++){
                    config.useSingleServer().setAddress(new StringBuilder().append(ips[i]).append(":").append(ports[i]).toString());
                }
                redisson = (Redisson) Redisson.create(config);
                log.info("初始化Redisson结束");
            } catch (Exception e) {
                log.error("redisson init error",e);
            }
        }
        else {
            log.error("redis获取ip或端口失败");
        }
    }
}
