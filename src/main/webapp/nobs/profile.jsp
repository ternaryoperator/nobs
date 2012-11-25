<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.jumbalakka.commons.file.types.JumbalakkaFile"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://com.jumbalakka.commons/taglib/jumb-commons" prefix="jumbCommons" %>
<%@ taglib uri="http://com.jumbalakka.quickfileshare/taglib/nobs" prefix="nobs" %>
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
				<title>Nobs</title>
				<script>
					function goHome()
					{
						$( '#E' ).val( 'jq.nobs' );
						$( '#profile' ).submit();
					}
				</script>
			</head>
			<body>
				<div class="container">
					<jsp:include page="../inc/include_body_top.jsp" />
					<c:if test="${param['E'] == 'jq.profile.update' }">
						<nobs:profileTag mode="UPDATE" />
					</c:if>
					
					<jumbCommons:userFeedback />
					
					<form id="profile" action="jwr" method="post">
					  <input type="hidden" id="E" name="E" value="jq.profile.update" />
					  <fieldset>
					    <legend>Update Profile</legend>
					    <label>Userid</label>
					    <input type="text" name="userid" value="${sessionScope.user.userid}" readonly="readonly"><br/>
					    <label>Username</label>
					    <input type="text" name="userName" value="${sessionScope.user.userName }"><br/>
					    <label>Current Password</label>
					    <input type="password" name="passwd"><br/>
					    <label>New Password</label>
					    <input type="password" name="passwd1"><br/>
					    <label>Confirm New Password</label>
					    <input type="password" name="passwd2"><br/>
					    <button type="submit" class="btn btn-primary">Save</button>
					    <button type="button" class="btn" onclick="goHome();" >Cancel</button>
					  </fieldset>
					</form>
					<jsp:include page="../inc/include_body_bottom.jsp" />
				</div>
			</body>
		</html>
	</c:otherwise>
</c:choose>
