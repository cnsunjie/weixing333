<%@page import="wx.bean.AdminUser"%>
<%@page import="wx.WxDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="wx.util.Constants"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="org.springframework.web.util.HtmlUtils"%>
<%@page import="wx.bean.BaseBean"%>
<%@page import="java.util.concurrent.atomic.AtomicLong"%>
<%@page import="wx.util.Util"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page contentType="text/html; charset=utf-8" language="java"%>

<%!String n(Object obj) {
		if (obj == null)
			return "&nbsp;";
		String s = String.valueOf(obj).trim();
		if (s.isEmpty())
			return "&nbsp;";
		return HtmlUtils.htmlEscape(s);
	}

	String b(Object obj) {
		if (obj == null)
			return "";
		return String.valueOf(obj).trim();
	}%>
<%
    AdminUser loginUser = (AdminUser)session.getAttribute(Constants.SESSION_ATTR_USER);
	if (loginUser == null) {
		response.sendRedirect("/logout.jsp");
		return; 
	}
	WebApplicationContext ctx = (WebApplicationContext) application.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	final WxDao wxDao = ctx.getBean("wxDao", WxDao.class);
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
%>

