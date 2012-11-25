package com.jumbalakka.nobs.type;

import java.io.Serializable;
import com.jumbalakka.commons.types.JumbalakkaObject;

public class NobsLinePayers extends JumbalakkaObject implements Serializable
{
	NobsBillLine billLine;
	NobsUser payer;
	Double pays;
	
	public Double getPays()
	{
		return pays;
	}
	public void setPays( Double pays )
	{
		this.pays = pays;
	}
	public NobsBillLine getBillLine()
	{
		return billLine;
	}
	public void setBillLine( NobsBillLine billLine )
	{
		this.billLine = billLine;
	}
	public NobsUser getPayer()
	{
		return payer;
	}
	public void setPayer( NobsUser payer )
	{
		this.payer = payer;
	}
}
