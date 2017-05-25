package com.acupt.processor;

import com.acupt.model.NotifyConf;
import com.acupt.notifier.Notifier;
import com.acupt.notifier.impl.MailNotifier;
import com.acupt.notifier.impl.SmsNotifier;
import com.acupt.service.NotifyConfService;
import com.acupt.util.BeanFactory;
import com.acupt.util.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

/**
 * 日志报警处理类，根据日志内容和用户配置通知用户
 * Created by liujie on 2017/5/19.
 */
public class NotifyLogProcessor implements LogProcessor {

    private Logger logger = Logger.getLogger(getClass());

    private NotifyConfService notifyConfService =
            BeanFactory.getBean(NotifyConfService.class);

    private Notifier mailNotifier =
            BeanFactory.getBean(MailNotifier.class);

    private Notifier smsNotifier =
            BeanFactory.getBean(SmsNotifier.class);

    @Override
    public String process(String log) {
        logger.info("process," + log);
        // 获取用户的报警配置
        NotifyConf conf = notifyConfService.getNotifyConf();
        logger.info("process-conf," + JsonUtil.toJson(conf));
        if (conf == null) {
            return null;
        }
        JSONObject logJson = JSONObject.parseObject(log);
        // 日志的错误等级
        String level = logJson.getString("level");
        // 日志的生成时间
        String time = logJson.getString("@timestamp");
        if (conf.getLevel() != null && conf.getLevel().contains(level)) {
            //通过邮件报警
            if (conf.isMailNotify()) {
                mailNotifier.send("[Log Monitor] risky log at "
                        + time, log, conf.getMail().split(","));
            }
            //通过短信报警
            if (conf.isSmsNotify()) {
                // 发送短信要控制字数，提取记录id即可准确定位
                String id = logJson.getString("_id");
                String msg = logJson.containsKey("message") ?
                        logJson.getString("message") : log;
                if (msg.length() > 20) {
                    msg = msg.substring(0, 20) + "...";
                }
                smsNotifier.send(id, msg, conf.getMobile().split(","));
            }
        }
        return null;
    }
}
