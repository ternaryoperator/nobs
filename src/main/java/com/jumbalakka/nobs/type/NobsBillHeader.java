package com.jumbalakka.nobs.type;

import java.util.Date;
import com.jumbalakka.commons.types.JumbalakkaObject;

public class NobsBillHeader extends JumbalakkaObject
{
	Integer id;
	String title;
	String description;
	Date dateCreated;
	
	public Integer getId()
	{
		return id;
	}
	public void setId( Integer id )
	{
		this.id = id;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle( String title )
	{
		this.title = title;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription( String description )
	{
		this.description = description;
	}
	public Date getDateCreated()
	{
		return dateCreated;
	}
	public void setDateCreated( Date dateCreated )
	{
		this.dateCreated = dateCreated;
	}
}
