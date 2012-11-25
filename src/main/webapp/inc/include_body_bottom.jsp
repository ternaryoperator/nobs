<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<hr/>
<div class="row">
	<div class="span5">&nbsp;</div>
	<c:choose>
		<c:when test="${ empty sessionScope.user }">
			<div class="span1"><a href="#">Register</a></div>
		</c:when>
		<c:otherwise>
			<div class="span1"><a href="${pageContext.request.contextPath}/jwr?E=jq.profile.view">Profile</a></div>
		</c:otherwise>
	</c:choose>
	<div class="span1"><a href="#">About</a></div>
	<div class="span4"></div>
	<div class="span1">${param['E']}</div>
</div>