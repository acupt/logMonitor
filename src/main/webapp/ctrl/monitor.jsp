<%@ page import="com.acupt.model.NotifyConf" %>
<%@ page import="com.acupt.monitor.LogMonitor" %>
<%@ page import="com.acupt.service.NotifyConfService" %>
<%@ page import="com.acupt.util.ApiResult" %>
<%@ page import="com.acupt.util.BeanFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ApiResult apiResult = new ApiResult();
    String action = request.getParameter("action");
    if (action == null) {
        apiResult.setMessage("缺少参数");
    }
    if (action.equals("switchChange")) {
        String s = request.getParameter("switch");
        if (s == null) {
            apiResult.setMessage("缺少参数");
        } else {
            LogMonitor logMonitor = BeanFactory.getBean(LogMonitor.class);
            boolean run = Boolean.valueOf(s);
            logMonitor.switchChange(run);
            apiResult.setSuccess(true);
        }
    } else if (action.equals("saveConf")) {
        String mailNotify = request.getParameter("mailNotify");
        String mail = request.getParameter("mail");
        String smsNotify = request.getParameter("smsNotify");
        String mobile = request.getParameter("mobile");
        String level = request.getParameter("level");
        NotifyConfService notifyConfService = BeanFactory.getBean(NotifyConfService.class);
        NotifyConf notifyConf = notifyConfService.getNotifyConf();
        notifyConf.setMailNotify(Boolean.parseBoolean(mailNotify));
        notifyConf.setMail(mail);
        notifyConf.setSmsNotify(Boolean.parseBoolean(smsNotify));
        notifyConf.setMobile(mobile);
        notifyConf.setLevel(level);
        notifyConfService.updateNotifyConf(notifyConf);
        apiResult.setSuccess(true);
    } else {
        apiResult.setMessage("未知请求");
    }
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=utf-8");
    response.getWriter().write(apiResult.toString());
%>