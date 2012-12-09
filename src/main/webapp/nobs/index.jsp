<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.jumbalakka.nobs.type.NobsResult"%>
<%@page import="java.util.List"%>
<%@page import="com.jumbalakka.nobs.dao.db.impl.NobsQueryImpl"%>
<%@page import="com.jumbalakka.nobs.type.NobsUser"%>
<%@page import="com.jumbalakka.nobs.web.servlet.LoginServlet"%>
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
				<!--[if lt IE 9]><script language="javascript" type="text/javascript" src="../inc/js/jqplot/excanvas.min.js"></script><![endif]-->
				<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/inc/js/jqplot/jquery.jqplot.min.js"></script>
				<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/inc/js/jqplot/jquery.jqplot.min.css" />
				<script type="text/javascript" src="${pageContext.request.contextPath}/inc/js/jqplot/plugins/jqplot.pieRenderer.min.js"></script>
				<script type="text/javascript" src="${pageContext.request.contextPath}/inc/js/jqplot/plugins/jqplot.donutRenderer.min.js"></script>
				<%
					NobsUser user = 
						(NobsUser) request.getSession().getAttribute( LoginServlet.SESSION_VAR_USER );
					NobsQueryImpl dao = SpringContextUtils.getBean( "queryNobsDAO", NobsQueryImpl.class );
					request.setAttribute( "result_data", dao.getAllResult( user, 0, 1000 ) );
				%>
				<script>
					$(document).ready(function(){
						$( '.moreInfoClick' ).click(function(e){
							e.preventDefault();
							var key = $(this).attr('data-key');
							window.location='${pageContext.request.contextPath}/jwr?E=jq.bill.view&id=' + key;
						});
						<%
							List<NobsResult> mapResult = dao.getFriendsPay( user );
							request.setAttribute( "friend_pays", mapResult );
							StringBuffer strBuf = new StringBuffer();
							strBuf.append( "var data1= [" );
							if( mapResult.isEmpty() )
							{
								strBuf.append( "[]" );
							}
							for( NobsResult indivResult: mapResult )
							{
								strBuf.append( "['" ).append( indivResult.getPayer() )
									.append( "'," ).append( indivResult.getCost() ).append( "]," );
							}
							
							out.print( StringUtils.removeEnd( strBuf.toString(), "," ) +  "];" );
							
							mapResult = dao.getYouOwe( user );
							request.setAttribute( "you_pay", mapResult );
							strBuf = new StringBuffer();
							strBuf.append( "var data2= [" );
							if( mapResult.isEmpty() )
							{
								strBuf.append( "[]" );
							}
							for( NobsResult indivResult: mapResult )
							{
								strBuf.append( "['" ).append( indivResult.getPayer() )
									.append( "'," ).append( indivResult.getCost() ).append( "]," );
							}
							
							out.print( StringUtils.removeEnd( strBuf.toString(), "," ) +  "];" );
						%>
					  var plot1 = jQuery.jqplot ('jqplot-pays', [data2], 
					    { 
						  title: 'You Pay',
					      seriesDefaults: {
					        // Make this a pie chart.
					        renderer: jQuery.jqplot.PieRenderer, 
					        rendererOptions: {
					          // Put data labels on the pie slices.
					          // By default, labels show the percentage of the slice.
					          showDataLabels: true
					        }
					      }, 
					      legend: { show:true, location: 'e' }
					    }
					  );
					
					var plot2 = jQuery.jqplot ('jqplot-owes', [data1], 
					    { 
						  title: 'Friends Owe',
					      seriesDefaults: {
					        // Make this a pie chart.
					        renderer: jQuery.jqplot.PieRenderer, 
					        rendererOptions: {
					          // Put data labels on the pie slices.
					          // By default, labels show the percentage of the slice.
					          showDataLabels: true
					        }
					      }, 
					      legend: { show:true, location: 'e' }
					    }
					  );

					<c:choose>
						<c:when test="${param['E']=='jq.new.bill' }">
							<nobs:paymentTag />
							$('#tabs a[href="#addnew"]').tab('show');
						</c:when>
					</c:choose>
					});
				</script>
				<title>Nobs</title>
				<script>
					function goHome()
					{
						window.location = '${pageContext.request.contextPath}/jwr?E=jq.nobs';
					}
				</script>
			</head>
			<body>
				<div id="mainBody" class="container fill-height">
					<jsp:include page="../inc/include_body_top.jsp" />
					<jumbCommons:userFeedback />
					<div id="tabs" class="container fill-height">
						<ul class="nav nav-tabs" data-tabs="tabs">
							<li class="active"><a href="#summary" data-toggle="tab">Summary</a></li>
							<li><a href="#addnew" data-toggle="tab">Add New</a></li>
						</ul>
						<div id="my-tab-content" class="tab-content">
							<div class="tab-pane active" id="summary">
								<nobs:functionCheck functionId="SUPER">
									<button class="btn btn-small btn-inverse" 
										type="button" 
										onClick="window.location='${pageContext.request.contextPath}/jwr?E=jq.admin'">
											ADMIN
									</button>
								</nobs:functionCheck>
								<div style="margin-left: 95%">
									<button class="btn btn-small btn-inverse" type="button" onClick="goHome();">Refresh</button>
								</div>
								<div>
									<div id="jqplot-pays" style="float: left; height:20%;width:40%; "></div>
									<div id="jqplot-owes" style="float: right; height:20%;width:40%; "></div>
								</div>
								<div id="tabs1" style="margin-top: 30%">
									<ul class="nav nav-tabs" data-tabs="tabs1">
										<li class="active"><a href="#you_pay" data-toggle="tab">You Pay</a></li>
										<li><a href="#friend_owe" data-toggle="tab">Friends Owe</a></li>
										<li><a href="#detail_list" data-toggle="tab">Details</a></li>
									</ul> 
									<div class="tab-content">
										<div class="tab-pane active" id="you_pay">
											<display:table name="you_pay" class="table table-striped" >
												<display:column property="payer" title="PAY TO" sortable="true"/>
												<display:column property="cost" title="PAYS" format="{0,number,#,#00.00}" sortable="true"/>
											</display:table>
										</div>
										<div class="tab-pane" id="friend_owe">
											<display:table name="friend_pays" class="table table-striped" >
												<display:column property="payer" title="PAYER" sortable="true"/>
												<display:column property="cost" title="PAYS" format="{0,number,#,#00.00}" sortable="true"/>
											</display:table>
										</div>
										<div class="tab-pane" id="detail_list">
											<display:table id="result_data1" name="result_data" class="table table-striped" >
												<display:column title="TITLE" sortable="true">
													<a data-key='${result_data1.id}' class='moreInfoClick'>${result_data1.title}</a>
												</display:column>
												<display:column property="description" title="DESC" sortable="true"/>
												<display:column property="dateCreated" format="{0,date,MM/dd/yyyy}" sortable="true"/>
											    <display:column property="cost" format="{0,number,#,#00.00}" sortable="true"/>
											    <display:column property="payer" title="PAYEE/PAYER" sortable="true"/>
											    <display:column property="type" title="TYPE" sortable="true"/>
											</display:table>
										</div>
									</div>
								</div>
							</div>
							<div class="tab-pane" id="addnew">
								<form action="${pageContext.request.contextPath}/jwr" id="newbill" method="post">
								  <input type="hidden" id="E" name="E" value="jq.new.bill" />
								  <fieldset>
								    <legend>Report a bill</legend>
								    <label>Title</label>
								    <input type="text" name="title" class="jumbRequiredField" 
								    	data-field-name='Title' value="${param['title'] }" placeholder="Title ..."><br/>
								    <label>Description</label>
								    <textarea name="description" placeholder="Description ..."><c:if test="${not empty param['description'] }">${param['description'] }</c:if></textarea><br/>
								    <label>Cost</label>
								    <input type="text" name="cost" class="jumbRequiredField jumbCheckForPositveFloat" 
								    	data-field-name='Cost' value="${param['cost'] }" placeholder="0"><br/>
								    <label>Split Between <span class="label label-important">Include yourself if you where in contribution</span></label>
								    <nobs:friendsMultiSelectTag compName="splitBetween" compId="splitBetween"
								    	styleClass="jumbRequiredField" compField="Split Between" /><br/>
								    <span class="label label-info">Hold down the Ctrl (windows) / Command (Mac) button to select multiple options.</span><br/>
								    <button type="submit" data-jumb-formId='newbill' class="btn jumbSubmit btn-primary">Save</button>
								    <button type="button" class="btn" onclick="goHome();" >Cancel</button>
								  </fieldset>
								</form>
							</div>
						</div>
					</div>
					<jsp:include page="../inc/include_body_bottom.jsp" />
				</div>	<!-- mainBody ends -->
			</body>
		</html>
	</c:otherwise>
</c:choose>
