<%@page import="wx.bean.AdminUser"%>
<%@page import="wx.WxEnum"%>
<%@page import="java.util.Map"%>
<%@page import="wx.bean.Shop"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Arrays"%>
<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common.jsp"%>
<%
	int id = Util.str2int(request.getParameter("Id"));
	String name = (id==0 ? "添加管理员" : "修改管理员");

	AdminUser user = new AdminUser();
	if (request.getParameter("Name") != null) {
	    Util.fillBean(user, request);
	    if (user.Id > 0)
	    	wxDao.update(user);
	    else
	        wxDao.insert(user);
	    String url = "/result.jsp?Success=true&Text=" + Util.urlEncode(name + "成功！");
	    if (id > 0) {
	        url += "&BackUrl=shop_list.jsp";
	    }
	    response.sendRedirect(url);
	    return;
	}
	
	if (id > 0) {
	    user = wxDao.queryObject(AdminUser.class, id);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=name %></title>
<link rel="stylesheet" href="/css/wx.css" />
<link rel="stylesheet" href="/js/jquery-ui-1.10.3.custom/css/smoothness/jquery-ui-1.10.3.custom.css">
<script src="/js/jquery-1.9.1.js"></script>
<script src="/js/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="/js/yx.js"></script>
<script>
$(function() {
	YX.globalInit();
	YX.initForm('form1', <%=user.toJson()%>);
});
function doSave() {
	if (confirm("您确定要保存么？")) {
		form1.submit();		
	}
}
</script>
</head>
<body>
<div class="nav">当前位置：&nbsp;&nbsp;<%=name %></div>
<div class="div_content">
<br/><br/>
<div align="center">
<form name="form1" id="form1" action="" method="post">
	<table style="width: 70%;" border="0">
        <tbody>
            <tr>
        		<td width="45%" style="text-align: right;"><strong>账户名：</strong></td>
        		<td style="text-align: left;"><input type="text" name="Account" size="20"  /></td>
        	</tr>
        	<tr>
        		<td width="45%" style="text-align: right;"><strong>姓名：</strong></td>
        		<td style="text-align: left;"><input type="text" name="Name" size="20" /></td>
        	</tr>
        	<tr>
        		<td width="45%" style="text-align: right;"><strong>联系电话：</strong></td>
        		<td style="text-align: left;"><input type="text" name="Mobile" size="20" value="" />&nbsp;</td>
        	</tr>
        	<tr>
        		<td width="45%" style="text-align: right;"><strong>管理区域：</strong></td>
        		<td style="text-align: left;">
        			<select name="Area">
        			    <% for (Map.Entry<Integer, String> entry : WxEnum.getItems(WxEnum.Name.AdminArea).entrySet()) {%>
						 <option value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
						<%}%>
        			</select>
        		</td>
        	</tr>
        	<tr>
        		<td width="45%" style="text-align: right;"><strong>权限：</strong></td>
        		<td style="text-align: left;">
        			<% for (Map.Entry<Integer, String> entry : WxEnum.getItems(WxEnum.Name.Roles).entrySet()) {%>
					<input type="checkbox" name="Roles" value="<%=entry.getKey()%>"/><%=entry.getValue()%>&nbsp;&nbsp;&nbsp;&nbsp;<br/>														    								   							    					  
					<%}%>	
        		</td>
        	</tr>
        </tbody>
     </table>
     	
    <br/>
    <div style="text-align: center;">
    	<input type="hidden" name="Id" id="Id" value="<%=id %>" />
    	<input type="button" value=" 确 定 " onclick="doSave();" />&nbsp;&nbsp;&nbsp;&nbsp;
    </div>
    <br/><br/>
</form>
 </div>
</div>
</body>
</html>
