package com.jumbalakka.nobs.type;

import java.util.Date;
import com.jumbalakka.commons.types.JumbalakkaObject;

public class NobsBillLine extends JumbalakkaObject
{
	Integer id;
	String title;
	String description;
	Double cost;
	NobsUser payee;
	NobsBillHeader header;
	Date dateCreated;
	
	public Date getDateCreated()
	{
		return dateCreated;
	}
	public void setDateCreated( Date dateCreated )
	{
		this.dateCreated = dateCreated;
	}
	public NobsBillHeader getHeader()
	{
		return header;
	}
	public void setHeader( NobsBillHeader header )
	{
		this.header = header;
	}
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
	public Double getCost()
	{
		return cost;
	}
	public void setCost( Double cost )
	{
		this.cost = cost;
	}
	public NobsUser getPayee()
	{
		return payee;
	}
	public void setPayee( NobsUser payee )
	{
		this.payee = payee;
	}
}
