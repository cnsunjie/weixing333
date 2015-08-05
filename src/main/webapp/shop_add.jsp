<%@page import="wx.WxEnum"%>
<%@page import="java.util.Map"%>
<%@page import="wx.bean.Shop"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Arrays"%>
<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common.jsp"%>
<%
	int id = Util.str2int(request.getParameter("Id"));
	String name = (id==0 ? "添加商户" : "修改商户");

	Shop shop = new Shop();
	if (request.getParameter("Name") != null) {
	    Util.fillBean(shop, request);
	    if (shop.Id > 0)
	    	wxDao.update(shop);
	    else
	        wxDao.insert(shop);
	    String url = "/result.jsp?Success=true&Text=" + Util.urlEncode(name + "成功！");
	    if (id > 0) {
	        url += "&BackUrl=shop_list.jsp";
	    }
	    response.sendRedirect(url);
	    return;
	}
	
	if (id > 0) {
	    shop = wxDao.queryObject(Shop.class, id);
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
	YX.initForm('form1', <%=shop.toJson()%>);
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
        		<td width="45%" style="text-align: right;"><strong>商户名称：</strong></td>
        		<td style="text-align: left;"><input type="text" name="Name" size="20"  /></td>
        	</tr>
        	<tr>
        		<td width="45%" style="text-align: right;"><strong>联系人：</strong></td>
        		<td style="text-align: left;"><input type="text" name="Contact" size="20" /></td>
        	</tr>
        	<tr>
        		<td width="45%" style="text-align: right;"><strong>联系电话：</strong></td>
        		<td style="text-align: left;"><input type="text" name="Tel" size="20" value="" />&nbsp;</td>
        	</tr>
        	<tr>
        		<td width="45%" style="text-align: right;"><strong>商圈：</strong></td>
        		<td style="text-align: left;">
        			<select name="Area">
        			    <% for (Map.Entry<Integer, String> entry : WxEnum.getItems(WxEnum.Name.Area).entrySet()) {%>
						 <option value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
						<%}%>
        			</select>
        		</td>
        	</tr>
        	<tr>
        		<td width="45%" style="text-align: right;"><strong>行业：</strong></td>
        		<td style="text-align: left;">
        			<select name="Category">
        				<% for (Map.Entry<Integer, String> entry : WxEnum.getItems(WxEnum.Name.Category).entrySet()) {%>
						 <option value="<%=entry.getKey()%>"><%=entry.getValue()%></option>
						<%}%>
        			</select>
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
