package com.jumbalakka.nobs.type;

import java.util.ArrayList;
import java.util.List;
import com.jumbalakka.commons.types.JumbalakkaObject;

public class NobsUser extends JumbalakkaObject
{
	String userid;
	String password;
	String userName;
	String status;
	List< NobsUsersGroup > groups = new ArrayList< NobsUsersGroup >();
	
	public enum USERSTATUS
	{
		ACTIVE, INACTIVE;
	}
	
	public String getStatus()
	{
		return status;
	}
	public void setStatus( String status )
	{
		this.status = status;
	}
	public List< NobsUsersGroup > getGroups()
	{
		return groups;
	}
	public void setGroups( List< NobsUsersGroup > groups )
	{
		this.groups = groups;
	}
	public String getUserid()
	{
		return userid;
	}
	public void setUserid( String userid )
	{
		this.userid = userid;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword( String password )
	{
		this.password = password;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName( String userName )
	{
		this.userName = userName;
	}
}
