package com.acupt.notifier;

/**
 * Created by liujie on 2017/5/15.
 */
public interface Notifier {

    void send(String head, String content, String... to);
}
