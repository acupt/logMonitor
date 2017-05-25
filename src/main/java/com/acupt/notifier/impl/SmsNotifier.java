package com.acupt.notifier.impl;

import com.acupt.notifier.Notifier;
import com.acupt.util.HttpClientUtil;
import com.acupt.util.JsonUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujie on 2017/5/19.
 */
public class SmsNotifier implements Notifier {

    private Logger logger = Logger.getLogger(getClass());

    private static String apikey = "bf4665765794940401fa37ce27912a08";

    private static String apiSingleSend = "https://sms.yunpian.com/v2/sms/single_send.json";

    private static String apiBatchSend = "https://sms.yunpian.com/v2/sms/batch_send.json";

    //邮件线程池配置
    private int queueCapacity = 10000;
    private int corePoolSize = 1;
    private int maximumPoolSize = 5;
    private long keepAliveTime = 60;//秒
    private ThreadPoolExecutor smsPool;

    public SmsNotifier() {
        init();
    }

    public void init() {
        smsPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueCapacity),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        logger.error("sms rejected " + JsonUtil.toJson(r));
                        SmsNotifier.SmsWorker worker = (SmsNotifier.SmsWorker) r;
                        worker.run();
                    }
                });
    }

    /**
     * 短信报警，因为短信接口往往对一条短信字数有限制，
     * 在此只提取重要新发送
     *
     * @param head    日志在es中的_id，便于收到报警后检索
     * @param content 日志摘要或其他提示信息
     * @param to      发往手机号，多个用逗号隔开
     */
    @Override
    public void send(String head, String content, String... to) {
        if (to == null || to.length == 0) {
            logger.warn("send sms to null " + head + " " + content);
            return;
        }
        //短信模板，一般要现在API提供者处事先审核
        String text = "【有趣的日志】日志_id=#id#,#msg#";
        text = text.replaceAll("#id#", head).replaceAll("#msg#", content);
        String mobile = to[0];
        for (int i = 1; i < to.length; i++) {
            mobile += "," + to[i];
        }
        SmsWorker smsWorker = new SmsWorker(text, mobile);
        if (smsPool != null) {
            smsPool.execute(smsWorker);
        } else {
            smsWorker.run();
        }
    }

    private class SmsWorker implements Runnable {

        /**
         * 短信内容
         */
        private String content;

        /**
         * 手机号
         */
        private String mobile;

        public SmsWorker(String content, String mobile) {
            this.content = content;
            this.mobile = mobile;
        }

        @Override
        public void run() {
            //根据第三方API要求组装参数
            Map<String, String> params = new HashMap<String, String>();
            params.put("apikey", apikey);
            params.put("text", content);
            params.put("mobile", mobile);
            try {
                String res = HttpClientUtil.post(mobile.contains(",") ?
                        apiBatchSend : apiSingleSend, params);
                logger.warn("send sms " + res + " " + mobile + " " + content);
            } catch (Exception e) {
                logger.error("send sms " + e.getMessage(), e);
            }
        }
    }

}
