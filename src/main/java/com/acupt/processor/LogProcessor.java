package com.acupt.processor;

/**
 * 日志处理类，可根据具体需要设计日志的处理逻辑
 * Created by liujie on 2017/5/19.
 */
public interface LogProcessor {

    /**
     * 需实现此接口完成对日志的处理
     *
     * @param log 日志记录，JSON格式，具体字段由生成日志的角色决定
     * @return 根据实际需要返回值
     */
    String process(String log);
}
