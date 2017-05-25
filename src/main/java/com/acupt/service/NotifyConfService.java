package com.acupt.service;

import com.acupt.model.NotifyConf;
import com.acupt.util.BeanFactory;
import com.acupt.util.JsonUtil;

/**
 * Created by liujie on 2017/5/17.
 */
public class NotifyConfService {

    private RedisService redisService = BeanFactory.getBean(RedisService.class);

    private static String NOTIFY_CONF = "log_monitor_notify_conf";

    public NotifyConf getNotifyConf() {
        String s = redisService.get(NOTIFY_CONF);
        return JsonUtil.fromJson(s, NotifyConf.class);
    }

    public void updateNotifyConf(NotifyConf notifyConf) {
        redisService.set(NOTIFY_CONF, JsonUtil.toJson(notifyConf));
    }
}
