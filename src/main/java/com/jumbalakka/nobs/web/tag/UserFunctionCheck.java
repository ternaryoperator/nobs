package com.jumbalakka.nobs.web.tag;

import javax.servlet.jsp.JspException;
import com.jumbalakka.commons.spring.SpringContextUtils;
import com.jumbalakka.commons.web.JumbalakkaAbstractTag;
import com.jumbalakka.nobs.dao.NobsDAO;
import com.jumbalakka.nobs.exception.NobsException;
import com.jumbalakka.nobs.type.NobsUser;
import com.jumbalakka.nobs.web.servlet.LoginServlet;

public class UserFunctionCheck extends JumbalakkaAbstractTag
{
	String functionId;
	
	public void setFunctionId( String functionId )
	{
		this.functionId = functionId;
	}

	@Override
	protected int doJumbEndTag() throws JspException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int doJumbStartTag() throws JspException
	{
		NobsUser user = getSessionAttribute( LoginServlet.SESSION_VAR_USER, NobsUser.class );
		if( user == null )
		{
			return SKIP_PAGE;
		}
		NobsDAO dao = SpringContextUtils.getBean( "nobsDAO", NobsDAO.class );
		try
		{
			if( dao.isAuthorized( user, functionId ) == false )
			{
				return SKIP_BODY;
			}
		}
		catch ( NobsException e )
		{
			return SKIP_PAGE;
		}
		return EVAL_BODY_INCLUDE;
	}

	@Override
	protected void jumbRelease()
	{
		// TODO Auto-generated method stub
		
	}

}
