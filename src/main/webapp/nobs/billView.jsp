<%@page import="com.jumbalakka.nobs.type.NobsLinePayers"%>
<%@page import="com.jumbalakka.nobs.type.NobsBillLine"%>
<%@page import="com.jumbalakka.nobs.type.NobsBillHeader"%>
<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.jumbalakka.nobs.type.NobsResult"%>
<%@page import="java.util.List"%>
<%@page import="com.jumbalakka.nobs.dao.NobsDAO"%>
<%@page import="com.jumbalakka.nobs.dao.db.impl.NobsQueryImpl"%>
<%@page import="com.jumbalakka.nobs.type.NobsUser"%>
<%@page import="com.jumbalakka.nobs.web.servlet.LoginServlet"%>
<%@page import="com.jumbalakka.commons.spring.SpringContextUtils"%>
<%@page import="com.jumbalakka.commons.spring.SpringContextUtils"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.jumbalakka.commons.file.types.JumbalakkaFile"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://com.jumbalakka.commons/taglib/jumb-commons" prefix="jumbCommons" %>
<%@ taglib uri="http://com.jumbalakka.quickfileshare/taglib/nobs" prefix="nobs" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<c:choose>
	<c:when test="${ empty sessionScope.user }">
		<c:redirect url="../index.jsp" />
	</c:when>
	<c:otherwise>
		<!DOCTYPE HTML>
		<html>
			<head>
				<LINK REL="SHORTCUT ICON" HREF="${pageContext.request.contextPath}/jqs.ico">
				<jsp:include page="../inc/include_resources.jsp" />
				<%
					NobsDAO nobsDAO = SpringContextUtils.getBean( "nobsDAO", NobsDAO.class ); 
					List<NobsLinePayers> billPayers =
						nobsDAO.getMatchingHeaders( NumberUtils.toInt( request.getParameter( "id" ), 0 ) );
					request.setAttribute( "billPayerse", billPayers );
				%>
			</head>
			<body>
				<div id="mainBody" class="container fill-height">
					<jsp:include page="../inc/include_body_top.jsp" />
					
					<c:choose>
						<c:when test="${param['E'] == 'jq.bill.delete' }">
							<%
								nobsDAO.deleteBillLine( NumberUtils.toInt( request.getParameter( "deleteId" ), 0 ) );
								request.setAttribute( "infoMsg", "Functionality yet to be implemented!" );
							%>
							<jumbCommons:userFeedback />
						</c:when>
						<c:otherwise>
							<form action="${pageContext.request.contextPath}/jwr" id="newbill" method="post">
								<input type="hidden" id="E" name="E" value="jq.bill.delete" />
								<input type='hidden' name='deleteId' value="${billPayerse[0].billLine.id}" />
								<fieldset>
								<legend>Report a bill</legend>
								<label>Title</label>
								${billPayerse[0].billLine.title}<br/>
								<label>Description</label>
								${billPayerse[0].billLine.description}<br/>
								<label>Cost</label>
								${billPayerse[0].billLine.cost}<br/>
								<label>Split Between</label>
								<br/>
								<c:forEach var="billPay" items="${billPayerse }"> 
									${billPay.payer.userName}<br/>
								</c:forEach>
								<span class="label label-info">Hold down the Ctrl (windows) / Command (Mac) button to select multiple options.</span><br/>
								<button type="submit" class="btn jumbSubmit btn-primary">Delete</button>
								</fieldset>
							</form>
						</c:otherwise>
					</c:choose>
				</div>
			</body>
		</html>
	</c:otherwise>
</c:choose>