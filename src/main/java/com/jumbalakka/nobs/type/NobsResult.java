package com.jumbalakka.nobs.type;

import java.util.Date;
import com.jumbalakka.commons.types.JumbalakkaObject;

public class NobsResult extends JumbalakkaObject
{
	Integer id;
	String title;
	String description;
	Date dateCreated;
	Double cost;
	String payer;
	String type;
	
	public enum PAYEE_TYPE
	{
		OWE, GETPAID
	}
	
	public String getType()
	{
		return type;
	}
	public void setType( String type )
	{
		this.type = type;
	}
	public String getPayer()
	{
		return payer;
	}
	public void setPayer( String payee )
	{
		this.payer = payee;
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
	public Date getDateCreated()
	{
		return dateCreated;
	}
	public void setDateCreated( Date dateCreated )
	{
		this.dateCreated = dateCreated;
	}
	public Double getCost()
	{
		return cost;
	}
	public void setCost( Double cost )
	{
		this.cost = cost;
	}
}
