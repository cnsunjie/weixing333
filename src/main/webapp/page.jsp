<%@page import="wx.util.PageDisplay"%>
<%@ page contentType="text/html; charset=utf-8" language="java"%>
<script type="text/javascript">
   function checkJump(){
	 var p_jump = $("#p_jump").val();
	 if(p_jump!=null && p_jump.length!=0){
		 return true;
	 }
	 return false ;
   }

</script>
<%
	PageDisplay _pd = (PageDisplay)request.getAttribute(PageDisplay.class.getName());
%>
        <td style="text-align:right;padding-top:px; padding-right:0px;">
			共<%=_pd.getTotalRecordCount()%>条
			<%=_pd.getCurPage() %>/<%=_pd.getTotalPageCount() %>页
        	<a href="<%=_pd.getFirstPageLink()%>">首页</a>
            <a href="<%=_pd.getPrePageLink()%>">上一页</a>
            <a href="<%=_pd.getNextPageLink()%>">下一页</a>
            <a href="<%=_pd.getLastPageLink()%>">尾页</a>
           	跳到&nbsp;<input type="text" size="2" id='p_jump' />&nbsp;页
            <a href="javascript:if(checkJump())document.location.href='<%=_pd.getPageLink(0)%>' + $('#p_jump').val();"><img title="跳转" src="/img/go.gif" />&nbsp;</a>
         </td>
         <td width="1%"> 
            <div>
			<div>
				<button id="page_rownum">每页显示<%=_pd.getRecordCountPerPage() %>条</button>
			</div>
			<ul>
				<li><a href="<%=_pd.getFirstPageLink()%>&p_num=20"><span style="width: 15px;display: inline-block;"><%=(_pd.getRecordCountPerPage() == 20 ? "√" : "&nbsp;")%></span>20条</a></li>
				<li><a href="<%=_pd.getFirstPageLink()%>&p_num=50"><span style="width: 15px;display: inline-block;"><%=(_pd.getRecordCountPerPage() == 50 ? "√" : "&nbsp;")%></span>50条</a></li>
				<li><a href="<%=_pd.getFirstPageLink()%>&p_num=100"><span style="width: 15px;display: inline-block;"><%=(_pd.getRecordCountPerPage() == 100 ? "√" : "&nbsp;")%></span>100条</a></li>
			</ul>
			</div>
         </td>