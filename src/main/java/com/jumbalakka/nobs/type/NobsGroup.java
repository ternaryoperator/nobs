package com.jumbalakka.nobs.type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.jumbalakka.commons.types.JumbalakkaObject;

public class NobsGroup extends JumbalakkaObject
{
	Integer id;
	String groupName;
	String description;
	NobsUser groupCreator;
	List< NobsUsersGroup > users = new ArrayList< NobsUsersGroup >();
	
	public NobsUser getGroupCreator()
	{
		return groupCreator;
	}
	public void setGroupCreator( NobsUser groupCreator )
	{
		this.groupCreator = groupCreator;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription( String description )
	{
		this.description = description;
	}
	public Integer getId()
	{
		return id;
	}
	public void setId( Integer id )
	{
		this.id = id;
	}
	public String getGroupName()
	{
		return groupName;
	}
	public void setGroupName( String groupName )
	{
		this.groupName = groupName;
	}
	public List< NobsUsersGroup > getUsers()
	{
		return users;
	}
	public void setUsers( List< NobsUsersGroup > users )
	{
		this.users = users;
	}
}
