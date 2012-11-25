package com.jumbalakka.nobs.type;

import java.io.Serializable;
import com.jumbalakka.commons.types.JumbalakkaObject;

public class NobsUsersGroup extends JumbalakkaObject implements Serializable
{
	NobsUser user;
	NobsGroup group;
	
	public NobsUser getUser()
	{
		return user;
	}
	public void setUser( NobsUser user )
	{
		this.user = user;
	}
	public NobsGroup getGroup()
	{
		return group;
	}
	public void setGroup( NobsGroup group )
	{
		this.group = group;
	}
}
