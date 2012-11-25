<%@page import="com.jumbalakka.commons.spring.SpringContextUtils"%>
<%@page import="com.jumbalakka.nobs.dao.NobsDAO"%>
<%@page import="com.jumbalakka.nobs.type.NobsUser"%>
<%@page import="com.jumbalakka.nobs.web.servlet.LoginServlet"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.jumbalakka.commons.file.types.JumbalakkaFile"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://com.jumbalakka.commons/taglib/jumb-commons" prefix="jumbCommons" %>
<%
	//hardcoded for now later move
	if( session != null )
	{
		if( session.getAttribute( "user" ) != null )
		{
			NobsUser user = 
					(NobsUser) request.getSession().getAttribute( LoginServlet.SESSION_VAR_USER ); 
			NobsDAO dao = SpringContextUtils.getBean( "nobsDAO", NobsDAO.class );
			dao.logInfo( user, user.getUserid(), "LOGOUT", "User Logged Out!" );
			session.invalidate();
		}
		session.setMaxInactiveInterval( 30 );
	}
%>
<!DOCTYPE HTML>
<html>
	<head>
		<LINK REL="SHORTCUT ICON" HREF="${pageContext.request.contextPath}/jqs.ico">
		<jsp:include page="inc/include_resources.jsp" />
		<title>Nobs</title>
	</head>
	<body>
		<div class="container fill-height">
			<jsp:include page="inc/include_body_top.jsp" />
			<jumbCommons:userFeedback />
			<form id='loginForm' action="jwr" method="post" style="margin-left: 30%; width: 50%">
			  <input type="hidden" name="E" value="jq.login" />
			  <fieldset>
			    <legend>Login</legend>
			    <label>Username</label>
			    <input type="text" name="user" data-field-name='Username' 
			    	class="jumbRequiredField" value="${param['user']}" placeholder="E-Mail Address"><br/>
			    <label>Password</label>
			    <input type="password" name="passwd" data-field-name='Password' class="jumbRequiredField"><br/>
			    <button type="submit" data-jumb-formId='loginForm' class="btn jumbSubmit">Submit</button>
			  </fieldset>
			</form>
			<jsp:include page="inc/include_body_bottom.jsp" />
		</div>
	</body>
</html>
