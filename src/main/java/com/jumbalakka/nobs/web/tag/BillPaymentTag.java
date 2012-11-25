package com.jumbalakka.nobs.web.tag;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import com.jumbalakka.commons.spring.SpringContextUtils;
import com.jumbalakka.commons.web.JumbalakkaAbstractTag;
import com.jumbalakka.nobs.dao.NobsDAO;
import com.jumbalakka.nobs.exception.NobsException;
import com.jumbalakka.nobs.type.NobsUser;

public class BillPaymentTag extends JumbalakkaAbstractTag
{

	@Override
	protected int doJumbEndTag() throws JspException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int doJumbStartTag() throws JspException
	{
		NobsUser user = getSessionAttribute( USER_SESSION, NobsUser.class );
		NobsDAO dao = SpringContextUtils.getBean( "nobsDAO", NobsDAO.class );
		String title = getRequest().getParameter( "title" );
		String desc = getRequest().getParameter( "description" );
		double cost = NumberUtils.toDouble( getRequest().getParameter( "cost" ) );
		String[] payee = getRequest().getParameterValues( "splitBetween" ) ;
		List< NobsUser > payers = new ArrayList< NobsUser >();
		NobsUser userTemp = null;
		if( payee != null )
		{
			for( String payErss: payee )
			{
				try
				{
					userTemp = dao.getUser( payErss );
					payers.add( userTemp );
				}
				catch ( NobsException e )
				{
					setErrorMessage( payErss + " - " + e.getMessage() );
					return EVAL_PAGE;
				}
			}
		}
		if( StringUtils.isBlank( title ) )
		{
			setErrorMessage( "Title cannot be blank" );
			return EVAL_PAGE;
		}
		else if( cost <= 0 )
		{
			setErrorMessage( "Cost cannot be less than zero" );
			return EVAL_PAGE;
		}
		else if( payers.isEmpty() )
		{
			setErrorMessage( "You did not select any payer for this bill" );
			return EVAL_PAGE;
		}
		
		dao.addNewBill( user, title, desc, cost, payers );
		setInfoMessage( "Bill was added!" );
		
		return 0;
	}

	@Override
	protected void jumbRelease()
	{
		// TODO Auto-generated method stub
		
	}
}
