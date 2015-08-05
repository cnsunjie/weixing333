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
	 	wxDao.delete(Shop.class, id);
	}
	
	int total = wxDao.queryCount(Shop.class);
	PageDisplay pd = new PageDisplay(request, response, total, 20, 1);
	QueryCond qc = new QueryCond();
	qc.setStart(pd.getSqlBegin());
	qc.setLimit(pd.getSqlNum());
	List<Shop> list = wxDao.queryList(new Shop(), qc);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>商户列表</title>
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
<div class="nav">当前位置：&nbsp;&nbsp;商户管理</div>
<div class="div_content">
<br/>        
<form name="mainForm" id="mainForm" action="<%=pd.getCurPageLink(new String[]{"Id", "act"})%>" method="post">
    <table class="table_content" width="100%">
        <tbody>
            <tr>
				<th width="8%">&nbsp;序号&nbsp;</th>
                <th width="25%">&nbsp;&nbsp;名称&nbsp;&nbsp;</th>
                <th width="12%">&nbsp;联系人&nbsp;</th>
                <th width="12%">&nbsp;&nbsp;电话&nbsp;&nbsp;</th>
                <th width="15%">&nbsp;&nbsp;商圈&nbsp;&nbsp;</th>
                <th width="15%">&nbsp;行业&nbsp;</th>
                <th>&nbsp;&nbsp;操作&nbsp;&nbsp;</th>
            </tr>
            <%
            	int idx = pd.getIndexOfAllRecord();
                for (Shop o : list) {
            %>
            <tr class="table_content_line">
                <td><%=idx++%></td>
                <td><%=o.Name%></td>
                <td><%=o.Contact%></td>
                <td><%=o.Tel%></td>
                <td><%=n(WxEnum.getValue(WxEnum.Name.Area, o.Area))%></td>
                <td><%=n(WxEnum.getValue(WxEnum.Name.Category, o.Category))%></td>
                <td>
                    <a class="opt_icon edit" title="修改" href="shop_add.jsp?Id=<%=o.Id%>&backurl=<%=Util.urlEncode(pd.getCurPageLink(new String[]{"Id","act"}))%>"></a>
                    <a class="opt_icon delete" title="删除" href="javascript:deleteOne('<%=pd.getCurPageLink(new String[]{"Id", "act"}) %>&Id=<%=o.Id%>&act=delete')"></a>
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
