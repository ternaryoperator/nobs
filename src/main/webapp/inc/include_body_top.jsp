<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="row">
	<div class="span3">
		<c:choose>
			<c:when test="${ empty sessionScope.user }">
				<a href="${pageContext.request.contextPath}">
					<img width="80" height="40" src="${pageContext.request.contextPath}/inc/image/past-due-notice-th.png" />
				</a>
			</c:when>
			<c:otherwise>
				<a href="${pageContext.request.contextPath}/jwr?E=jq.nobs" >
					<img width="80" height="40" src="${pageContext.request.contextPath}/inc/image/past-due-notice-th.png" />
				</a>
			</c:otherwise>
		</c:choose>
	</div>
	<div class="span6  jumb-center-text">
		<h1>NoBS - Split your shared bill</h1>
	</div>
	<div class="span3 jumb-right-text">
		<c:choose>
			<c:when test="${ empty sessionScope.user }">
				<a href="#">Login</a>
			</c:when>
			<c:otherwise>
				${ sessionScope.user.userName } | <a href="${pageContext.request.contextPath}/jwr?E=jq.main">Logout</a>
			</c:otherwise>
		</c:choose>
	</div>
</div>
<hr/>