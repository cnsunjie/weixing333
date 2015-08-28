<%@page import="wx.bean.AdminUser"%>
<%@page import="wx.WxEnum"%>
<%@page import="wx.QueryCond"%>
<%@page import="wx.bean.Shop"%>
<%@page import="java.util.concurrent.atomic.AtomicInteger"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html; charset=utf-8" language="java"%>
<%@ include file="/common.jsp"%>
<%
	String act = request.getParameter("act");
	if ("delete".equals(request.getParameter("act"))) {
		int id = Util.str2int(request.getParameter("Id"));
	 	wxDao.delete(AdminUser.class, id);
	}
	
	int total = wxDao.queryCount(AdminUser.class);
	PageDisplay pd = new PageDisplay(request, response, total, 20, 1);
	QueryCond qc = new QueryCond();
	qc.setStart(pd.getSqlBegin());
	qc.setLimit(pd.getSqlNum());
	List<AdminUser> list = wxDao.queryList(new AdminUser(), qc);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理员列表</title>
<link rel="stylesheet" href="/css/wx.css" />
<link rel="stylesheet" href="/js/jquery-ui-1.10.3.custom/css/smoothness/jquery-ui-1.10.3.custom.css">
<script src="/js/jquery-1.9.1.js"></script>
<script src="/js/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script src="/js/yx.js"></script>
<script>
	$(function() {
		YX.globalInit();
	});
	
	function deleteOne(url) {
		if (confirm("您确定要删除么？")) {
			document.location.href= url;
		}
	}
</script>
</head>
<body>
<div class="nav">当前位置：&nbsp;&nbsp;管理员管理</div>
<div class="div_content">
<br/>        
<form name="mainForm" id="mainForm" action="<%=pd.getCurPageLink(new String[]{"Id", "act"})%>" method="post">
    <table class="table_content" width="100%">
        <tbody>
            <tr>
				<th width="8%">&nbsp;序号&nbsp;</th>
                <th width="20%">&nbsp;&nbsp;账号&nbsp;&nbsp;</th>
                <th width="20%">&nbsp;姓名&nbsp;</th>
                <th width="20%">&nbsp;&nbsp;电话&nbsp;&nbsp;</th>
                <th width="20%">&nbsp;&nbsp;管理区域&nbsp;&nbsp;</th>
                <th>&nbsp;&nbsp;操作&nbsp;&nbsp;</th>
            </tr>
            <%
            	int idx = pd.getIndexOfAllRecord();
                for (AdminUser o : list) {
            %>
            <tr class="table_content_line">
                <td><%=idx++%></td>
                <td><%=o.Account%></td>
                <td><%=o.Name%></td>
                <td><%=o.Mobile%></td>
                <td><%=n(WxEnum.getValue(WxEnum.Name.AdminArea, o.Area))%></td>
                <td>
                	<% if (!o.Account.equals("admin")) { %>
                    	<a class="opt_icon edit" title="修改" href="adminuser_add.jsp?Id=<%=o.Id%>&backurl=<%=Util.urlEncode(pd.getCurPageLink(new String[]{"Id","act"}))%>"></a>
                    	<a class="opt_icon delete" title="删除" href="javascript:deleteOne('<%=pd.getCurPageLink(new String[]{"Id", "act"}) %>&Id=<%=o.Id%>&act=delete')"></a>
                    <%} %>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>
    <input type="hidden" name="act" />
</form>	
<form>
    <table class="table_page">
	<tr >
        <td width="1%">
         	<div>
				&nbsp;
			</div>
        </td>
        <%@ include file="/page.jsp"%>
    </tr>
</table>
</form>
</div>
</body>
</html>
