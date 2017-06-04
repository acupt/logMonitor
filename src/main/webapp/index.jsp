<%@ page import="com.acupt.monitor.LogMonitor" %>
<%@ page import="com.acupt.util.BeanFactory" %>
<%@ page import="com.acupt.model.NotifyConf" %>
<%@ page import="com.acupt.service.NotifyConfService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    NotifyConfService notifyConfService = BeanFactory.getBean(NotifyConfService.class);
    NotifyConf notifyConf = notifyConfService.getNotifyConf();
    if (notifyConf == null) {
        notifyConf = new NotifyConf();
        notifyConfService.updateNotifyConf(notifyConf);
    }
    LogMonitor logMonitor = BeanFactory.getBean(LogMonitor.class);
    boolean working = logMonitor.isWorking();
    String state = working ? "已开启" : "未开启";
    String switchBtn = working ? "停止" : "开启";
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Log Monitor</title>
</head>
<body>
<table>
    <tr>
        <td><%=state%>
        </td>
        <td>
            <button onclick="switchChange(<%=!working%>)"><%=switchBtn%>
            </button>
        </td>
    </tr>
</table>

<h3>报警设置</h3>
<table width="50%">
    <tr>
        <td width="20%">属性</td>
        <td width="80%">参数</td>
    </tr>
    <tr>
        <td>邮件报警</td>
        <td><input id="mailNotify" type="checkbox" <%=notifyConf.isMailNotify()?"checked":""%>></td>
    </tr>
    <tr>
        <td>报警邮箱</td>
        <td><input id="mail" type="text" value="<%=notifyConf.getMail()%>" style="width: 100%" "></td>
    </tr>
    <tr>
        <td>短信报警</td>
        <td><input id="smsNotify" type="checkbox" <%=notifyConf.isSmsNotify()?"checked":""%>></td>
    </tr>
    <tr>
        <td>手机号码</td>
        <td><input id="mobile" type="text" value="<%=notifyConf.getMobile()%>" style="width: 100%" "></td>
    </tr>
    <tr>
        <td>报警级别</td>
        <td><input id="level" type="text" value="<%=notifyConf.getLevel()%>" style="width: 100%"></td>
    </tr>
</table>
<button onclick="saveConf()">保存</button>
</body>
</html>
<script>
    function switchChange(v) {
        $.ajax({
            type: 'GET',
            dataType: 'json',
            url: 'ctrl/monitor.jsp?action=switchChange&switch=' + v,
            success: function (result) {
                if (result.success) {
                    location.reload();
                } else {
                    alert(result.message);
                }
            },
            error: function (xhr, status, e) {
                alert('shirt error! ' + status + ' ' + e);
            }
        });
    }

    function saveConf() {
        var mailNotify = document.getElementById('mailNotify').checked;
        var mail = document.getElementById('mail').value;
        var smsNotify = document.getElementById('smsNotify').checked;
        var mobile = document.getElementById('mobile').value;
        var level = document.getElementById('level').value;
        $.ajax({
            type: 'GET',
            dataType: 'json',
            url: 'ctrl/monitor.jsp?action=saveConf&mailNotify=' + mailNotify + "&mail=" + mail + "&level=" + level
            + "&smsNotify=" + smsNotify + "&mobile=" + mobile,
            success: function (result) {
                if (result.success) {
                    alert("SUCCESS")
                } else {
                    alert(result.message);
                }
            },
            error: function (xhr, status, e) {
                alert('shirt error! ' + status + ' ' + e);
            }
        });
    }
</script>
<script src='static/js/jquery.1.7.1.min.js'></script>