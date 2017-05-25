package com.acupt.notifier.impl;

import com.acupt.notifier.Notifier;
import com.acupt.util.JsonUtil;
import org.apache.log4j.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by liujie on 2017/5/15.
 */
public class MailNotifier implements Notifier {

    private Logger logger = Logger.getLogger(getClass());

    //发件邮箱配置
//    private String email = "acupt@foxmail.com";
//    private String password = "crxcnqwutsgnbdhi";
    private String email = "acupjava@163.com";
    private String password = "acupjava329";
    private String smtpHost = "smtp.163.com";
    private boolean smtpAuth = true;
    //邮件线程池配置
    private int queueCapacity = 10000;
    private int corePoolSize = 1;
    private int maximumPoolSize = 5;
    private long keepAliveTime = 60;//秒
    private ThreadPoolExecutor mailPool;

    public MailNotifier() {
        init();
    }

    public void init() {
        mailPool = new ThreadPoolExecutor(corePoolSize/*线程池保持存活线程数*/,
                maximumPoolSize/*线程池最大线程数*/,
                keepAliveTime/*线程等待时间*/,
                TimeUnit.SECONDS/*线程等待时间单位*/,
                new ArrayBlockingQueue<Runnable>(queueCapacity)/*任务队列*/,
                new RejectedExecutionHandler() {
                    /**
                     * 任务队列溢出时对应任务不会放入线程池
                     * 在此捕获并执行任务
                     * 同时记录日志以便改善
                     */
                    @Override
                    public void rejectedExecution(Runnable r,
                                                  ThreadPoolExecutor executor) {
                        logger.error("mail rejected " + JsonUtil.toJson(r));
                        MailWorker worker = (MailWorker) r;
                        worker.run();
                    }
                });
    }

    @Override
    public void send(String head, String content, String... to) {
        MailWorker worker = new MailWorker(head, content, null, to);
        if (mailPool != null) {
            mailPool.execute(worker);
        } else {
            worker.run();
        }
    }

    private class MailWorker implements Runnable {

        private String subject;
        private String content;
        private String contentType;
        private String[] to;

        public MailWorker(String subject, String content, String contentType, String... to) {
            this.subject = subject;
            this.content = content;
            this.contentType = contentType;
            this.to = to;
        }

        public void run() {
            send();
        }

        public void send() {
            if (to == null || to.length == 0) {
                logger.info("to null," + JsonUtil.toJson(this));
                return;
            }
            long start = System.currentTimeMillis();
            Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.auth", String.valueOf(smtpAuth));
            Session sendMailSession = Session.getInstance(props,
                    new Authenticator() {
                        public PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(email, password);
                        }
                    }
            );
            try {
                Message newMessage = new MimeMessage(sendMailSession);
                newMessage.setFrom(new InternetAddress(email)); // 发件人
                for (String des : to) {
                    newMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(des));
                }
                newMessage.setSubject(subject);
                if (contentType != null && !"".equals(contentType)) {
                    newMessage.setContent(content, contentType);
                } else {
                    newMessage.setText(content);
                }
                Transport.send(newMessage);
            } catch (Exception e) {
                logger.error("send error," + JsonUtil.toJson(this) + "," + e.getMessage(), e);
            }
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public boolean isSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(boolean smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public static void main(String[] args) {
        MailNotifier mailNotifier = new MailNotifier();
        mailNotifier.send("你好撒", "你好老哥qaaa", "liujiest@foxmail.com", "liujie@qipeng.com");
    }

}
