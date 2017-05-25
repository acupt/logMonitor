package com.acupt.model;

/**
 * Created by liujie on 2017/5/17.
 */
public class NotifyConf {

    private boolean mailNotify;

    private boolean smsNotify;

    private String mail = "";

    private String mobile = "";

    private String level = "";

    public boolean isMailNotify() {
        return mailNotify;
    }

    public void setMailNotify(boolean mailNotify) {
        this.mailNotify = mailNotify;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isSmsNotify() {
        return smsNotify;
    }

    public void setSmsNotify(boolean smsNotify) {
        this.smsNotify = smsNotify;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
