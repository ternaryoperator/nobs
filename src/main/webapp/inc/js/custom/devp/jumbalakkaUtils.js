Date.prototype.isGreaterOrEqualDate = function( param2 )
{
	if( this.getDate() == param2.getDate() &&  
			this.getMonth() == param2.getMonth() && 
			this.getFullYear() == param2.getFullYear() )
	{
		return true;
	}
	else if( this > param2 )
	{
		return true;
	}
	return false;
}