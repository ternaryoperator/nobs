<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@page import="com.jumbalakka.commons.config.type.Tuple"%>
<%@page import="com.jumbalakka.commons.config.dao.TupleDAO"%>
<%@page import="com.jumbalakka.nobs.dao.NobsDAO"%>
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
		<nobs:functionCheck functionId="SUPER">
			<!DOCTYPE HTML>
			<html>
				<head>
					<LINK REL="SHORTCUT ICON" HREF="${pageContext.request.contextPath}/jqs.ico">
					<jsp:include page="../inc/include_resources.jsp" />
					<Script>
					$(document).ready(function(){
						$( '#tuple_update,#tuple_delete' ).click(function e(){
							var id = $( this ).attr( 'data-key' );
							var formToSubmit = $( this ).attr( 'data-form' );
							if( $(this).attr( 'id' ) == 'tuple_delete' )
							{
								$(formToSubmit).find( '#tuple_E' ).val( 'jq.admin.config.delete' );
							}
							$( formToSubmit ).find( '#tuple_ID' ).val( id );
							$( formToSubmit ).submit();
						});
						$( '#user_update,#user_delete' ).click(function e(){
							var id = $( this ).attr( 'data-key' );
							var formToSubmit = $( this ).attr( 'data-form' );
							if( $(this).attr( 'id' ) == 'user_delete' )
							{
								$(formToSubmit).find( '#user_E' ).val( 'jq.admin.user.delete' );
							}
							$( formToSubmit ).find( '#user_ID' ).val( id );
							$( formToSubmit ).submit();
						});
					});
					</Script>
				</head>
				<body>
					<div id="mainBody" class="container fill-height">
						<jsp:include page="../inc/include_body_top.jsp" />
						<%
							TupleDAO tupleDAO = SpringContextUtils.getBean( "tupleDAO", TupleDAO.class );
							NobsDAO nobsDAO = SpringContextUtils.getBean( "nobsDAO", NobsDAO.class ); 
							
							String E = request.getParameter( "E" );
							if( StringUtils.equals( E, "jq.admin.config.update" ) )
							{
								int id = NumberUtils.toInt( request.getParameter( "id" ), 0 );
								Tuple tuple = tupleDAO.getById( id );
								if( tuple != null )
								{
									String type= request.getParameter( "type_" + id );
									String key= request.getParameter( "key_" + id );
									String value= request.getParameter( "value_" + id );
									tuple.setType( type );
									tuple.setKey( key );
									tuple.setValue( value );
									tupleDAO.updateTuple( tuple );
									request.setAttribute( "infoMsg", "Value updated" );
								}
								else
								{
									request.setAttribute( "errorMsg", "Config not found" );
								}
							}
							else if( StringUtils.equals( E, "jq.admin.config.add" ) )
							{
								String type= request.getParameter( "type" );
								String key= request.getParameter( "key" );
								String value= request.getParameter( "value" );
								tupleDAO.addTuple( type, key, value );
								request.setAttribute( "infoMsg", "Value added" );	//errorMsg
							}
							else if( StringUtils.equals( E, "jq.admin.config.delete" ) )
							{
								int id = NumberUtils.toInt( request.getParameter( "id" ), 0 );
								Tuple tuple = tupleDAO.getById( id );
								if( tuple != null )
								{
									tupleDAO.delete( tuple );
									request.setAttribute( "infoMsg", "Value deleted" );
								}
								else
								{
									request.setAttribute( "errorMsg", "Config not found" );
								}
							}
							else if( StringUtils.equals( E, "jq.admin.user.update" ) )	//user
							{
								String userId = request.getParameter( "userid" );
								NobsUser userTemp = nobsDAO.getUser( userId );
								if( userTemp != null )
								{
									String password= request.getParameter( "password_" + userId );
									String userName= request.getParameter( "userName_" + userId );
									userTemp.setUserName( userName );
									nobsDAO.updateUserPassword( userTemp, password );
									request.setAttribute( "infoMsg", "Value updated" );
								}
								else
								{
									request.setAttribute( "errorMsg", "User not found" );
								}
							}
							else if( StringUtils.equals( E, "jq.admin.user.add" ) )
							{
								String userid= request.getParameter( "userid" );
								String password= request.getParameter( "password" );
								String userName= request.getParameter( "userName" );
								NobsUser userTemp = new NobsUser();
								userTemp.setUserid( userid );
								userTemp.setUserName( userName );
								nobsDAO.addUser( userTemp, password );
								request.setAttribute( "infoMsg", "Value added" );	//errorMsg
							}
							else if( StringUtils.equals( E, "jq.admin.user.delete" ) )
							{
								String userId = request.getParameter( "userid" );
								NobsUser user = nobsDAO.getUser( userId );
								if( user != null )
								{
									nobsDAO.deleteUser( user );
									request.setAttribute( "infoMsg", "Value deleted" );
								}
								else
								{
									request.setAttribute( "errorMsg", "User not found" );
								}
							}
							List<Tuple> allTuples = tupleDAO.getAll();
							request.setAttribute( "allTuples", allTuples );
							
							List<NobsUser> nobsUser = nobsDAO.getAllUsers();
							request.setAttribute( "allUsers", nobsUser );
							
							request.setAttribute( "allAuditEntry", nobsDAO.getAllLog() );
							
						%>
						<jumbCommons:userFeedback />
						<div id="tabs" class="container fill-height">
							<ul class="nav nav-tabs" data-tabs="tabs">
								<li class="active"><a href="#appConfig" data-toggle="tab">App Config</a></li>
								<li><a href="#userConfig" data-toggle="tab">User Configuration</a></li>
								<li><a href="#auditLog" data-toggle="tab">Audit Log</a></li>
							</ul>
							<div id="my-tab-content" class="tab-content">
								<div class="tab-pane active" id="appConfig">
									<form method="post" id="tupleExistingForm" action="${pageContext.request.contextPath}/jwr">
										<input type="hidden" name="E" id="tuple_E" value="jq.admin.config.update" />
										<input type="hidden" name="id" id="tuple_ID" value="" />
										<display:table name="allTuples" id="tuple" class="table table-striped" >
											<display:column property="id" title="ID" sortable="true"/>
											<display:column property="createDate" format="{0,date,MM-dd-yyyy}" 
												title="CREATE DATE" sortable="true"/>
											<display:column title="TYPE">
												<input type="text" name="type_${tuple.id}" value="${tuple.type}" />
											</display:column>
											<display:column title="KEY">
												<input type="text" name="key_${tuple.id}" value="${tuple.key}" />
											</display:column>
											<display:column title="VALUE">
												<input type="text" name="value_${tuple.id}" value="${tuple.value}" />
											</display:column>
											<display:column title="ACTION">
												<input type="button" id="tuple_update" 
													value="UPDATE" class="btn jumbSubmit btn-warning" 
													data-form="#tupleExistingForm" data-key="${tuple.id}" />
												<input type="button" id="tuple_delete" 
													value="DELETE" class="btn jumbSubmit btn-warning" 
													data-form="#tupleExistingForm" data-key="${tuple.id}" />
											</display:column>
										</display:table>
									</form>
									<form action="${pageContext.request.contextPath}/jwr">
										<input type="hidden" name="E" value="jq.admin.config.add" />
										<fieldset>
								    		<legend>New App Config</legend>
								    		<label>TYPE</label>
								    		<input type="text" name="type" /><br/>
								    		<label>KEY</label>
								    		<input type="text" name="key" /><br/>
								    		<label>VALUE</label>
								    		<input type="text" name="value" /><br/>
								    		<button type="submit" class="btn jumbSubmit btn-primary">Save</button>
								    	</fieldset>
									</form>
								</div>
								<div class="tab-pane" id="userConfig">
									<form method="post" id="userExistingForm" action="${pageContext.request.contextPath}/jwr">
										<input type="hidden" name="E" id="user_E" value="jq.admin.user.update" />
										<input type="hidden" name="userid" id="user_ID" value="" />
										<display:table name="allUsers" id="user" class="table table-striped" >
											<display:column property="userid" title="USERID" sortable="true"/>
											<display:column title="PASSWD">
												<input type="password" name="password_${user.userid}" />
											</display:column>
											<display:column title="USERNAME">
												<input type="text" name="userName_${user.userid}" value="${user.userName}" />
											</display:column>
											<display:column title="ACTION">
												<input type="button" id="user_update" 
													value="UPDATE" class="btn jumbSubmit btn-warning" 
													data-form="#userExistingForm" data-key="${user.userid}" />
												<input type="button" id="user_delete" 
													value="DELETE" class="btn jumbSubmit btn-warning" 
													data-form="#userExistingForm" data-key="${user.userid}" />
											</display:column>
										</display:table>
									</form>
									<form action="${pageContext.request.contextPath}/jwr">
										<input type="hidden" name="E" value="jq.admin.user.add" />
										<fieldset>
								    		<legend>New User</legend>
								    		<label>User ID</label>
								    		<input type="text" name="userid" /><br/>
								    		<label>Password</label>
								    		<input type="password" name="password" /><br/>
								    		<label>User Name</label>
								    		<input type="text" name="userName" /><br/>
								    		<button type="submit" class="btn jumbSubmit btn-primary">Save</button>
								    	</fieldset>
									</form>
								</div>
								<div class="tab-pane" id="auditLog">
									<display:table name="allAuditEntry" id="allAudit" class="table table-striped" >
										<display:column property="createDate" title="USERID" sortable="true"/>
										<display:column property="userId" title="USERID" sortable="true"/>
										<display:column property="userName" title="USER" sortable="true"/>
										<display:column property="type" title="TYPE" sortable="true"/>
										<display:column property="app" title="APP" sortable="true"/>
										<display:column property="message" title="MSG" sortable="true"/>
										<display:column property="reference" title="REF" sortable="true"/>	
									</display:table>
								</div>
							</div>
						</div>
					</div>	
			</html>
		</nobs:functionCheck>							
	</c:otherwise>
</c:choose>