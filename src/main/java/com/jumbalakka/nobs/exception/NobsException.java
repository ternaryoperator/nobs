package com.jumbalakka.nobs.exception;

import com.jumbalakka.commons.JumbalakkaException;

public class NobsException extends JumbalakkaException
{
	public NobsException( String message )
	{
		super( message );
	}

	public NobsException( Exception e )
	{
		super( e );
	}	
}
