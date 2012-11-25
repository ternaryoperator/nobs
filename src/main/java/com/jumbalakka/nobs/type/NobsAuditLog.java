package com.jumbalakka.nobs.type;

import java.util.Date;
import com.jumbalakka.commons.types.JumbalakkaObject;

public class NobsAuditLog extends JumbalakkaObject
{
	Integer id;
	Date createDate;
	String userId;
	String userName;
	String message;
	String type;
	String app;
	String reference;
	
	public enum LOGTYPE{
		ERROR, INFO
	}
	
	public String getUserName()
	{
		return userName;
	}
	public void setUserName( String userName )
	{
		this.userName = userName;
	}
	public String getType()
	{
		return type;
	}
	public void setType( String type )
	{
		this.type = type;
	}
	public Integer getId()
	{
		return id;
	}
	public void setId( Integer id )
	{
		this.id = id;
	}
	public Date getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate( Date createDate )
	{
		this.createDate = createDate;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId( String userId )
	{
		this.userId = userId;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage( String message )
	{
		this.message = message;
	}
	public String getApp()
	{
		return app;
	}
	public void setApp( String app )
	{
		this.app = app;
	}
	public String getReference()
	{
		return reference;
	}
	public void setReference( String reference )
	{
		this.reference = reference;
	}
}
