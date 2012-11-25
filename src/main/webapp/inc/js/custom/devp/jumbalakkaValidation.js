jumbalakka.prototype.jumbalakkaValidation = function()
{
	var jumb = this;
	$( '.jumbSubmit' ).click( function(e) {
		var idOfFormToSubmit = $( this ).attr( 'data-jumb-formId' );
		$( '.jumbUserValidationErrorMsg' ).remove();
		var validation = new JumbValidation( $('#' + idOfFormToSubmit) );
		if( validation.error )
		{
			var bodyOfPopup = "<span>" + validation.listOfErrMsg + "</span>";
			var objTempNotifyPopup = new jumb.jumbSimpleModal( 'Error', bodyOfPopup );
			return false;
		}
		return true;
	} );
};

/*
 * title 	- title for the msgbox
 * msg 		- msg for msgbox
 * closeButton - if true will show close button
 * buttonNames - arry of buttonNames
 * eventNames - event to call on buttonpress
 * 
 */

function JumbValidation( formObj )
{
	this.errorMsg = '';
	this.listOfErrMsg = '';
	this.form = formObj;
	this.error = false;
	this.customValidation( );
	this.currDateValidation( );
	this.checkForPositiveInt( );
	this.checkForPositiveFloat( );
	this.requiredField( );
}
JumbValidation.prototype.getTodayDateInString = function()
{
	var d = new Date();
	var month = d.getMonth()+1;
	
	if (month < 10)
	{
		month = "0" + month;
	}
	var day = d.getDate();
	if (day  < 10)
	{
		day = "0" + day;
	}
	
	var datum =  month   + '/' + day + '/' + d.getFullYear(); 
	return datum;	
}
JumbValidation.prototype.setDateForComparision = function( dateToCompare )
{
	dateToCompare.setHours( 0 );
	dateToCompare.setMinutes( 0 );
	dateToCompare.setSeconds( 0 );
	dateToCompare.setMilliseconds( 0 );
	return dateToCompare;
}

JumbValidation.prototype.convertStringToDate = function( strDate )
{
	if( strDate == '' || strDate == 'undefined' || strDate == null )
	{
		return null;
	}
	var arrStr = strDate.split('/');
	if( arrStr.length != 3 )
	{
		return null;
	}
	var d= new Date();
	d.setFullYear( parseInt( arrStr[2], 10 ) );
	d.setMonth( parseInt( arrStr[0] ) - 1, 10 );
	d.setDate( parseInt( arrStr[1], 10 ) );
	return this.setDateForComparision( d );
}

/**
 * just add jumbCurrDateValidator to the field 
 * that needs a check of value greater than current date
 * note that on null this wont display error
 */
JumbValidation.prototype.currDateValidation = function( )
{
	var currObj = this;
	$( this.form ).find( '.jumbCurrDateValidator' ).each( function( index ){
		var objCurrDate = currObj.setDateForComparision( new Date() );
		var objDateSelected = currObj.convertStringToDate( $(this).val() );
		if( objDateSelected != null && objCurrDate.isGreaterOrEqualDate( objDateSelected ) )
		{
			currObj.error = true;
			currObj.errorMsg = $(this).attr( 'data-field-name' ) + ' should be greater than current date';
			currObj.showError( this );
		}
	} );
}
/*
 * source of regex - 
 * 			http://ntt.cc/2008/05/10/over-10-useful-javascript-regular-expression-functions-to-improve-your-web-applications-efficiency.html
 */
JumbValidation.prototype.checkForPositiveInt = function( )
{
	var currObj = this;
	$( this.form ).find( '.jumbCheckForPositveInt' ).each( function( index ){
		var strCurrVal = $(this).val();
		//number -> var reg = new RegExp(Ó^[-]?[0-9]+[\.]?[0-9]+$Ó);
		if( strCurrVal != '' )
		{
			var regEx = /^\s*\d+\s*$/;
			if( regEx.test( strCurrVal ) == false || 
					( regEx.test( strCurrVal ) && parseInt( strCurrVal ) <= 0 ) )
			{
				currObj.error = true;
				currObj.errorMsg = $(this).attr( 'data-field-name' ) + ' should be positive integer';
				currObj.showError( this );
			}
		}
	} );
}
/*
 * source of regex - 
 * 			http://ntt.cc/2008/05/10/over-10-useful-javascript-regular-expression-functions-to-improve-your-web-applications-efficiency.html
 */
JumbValidation.prototype.checkForPositiveFloat = function( )
{
	var currObj = this;
	$( this.form ).find( '.jumbCheckForPositveFloat' ).each( function( index ){
		var strCurrVal = $(this).val();
		if( strCurrVal != '' )
		{
			var regEx = new RegExp("^[-]?[0-9]+([\.]?[0-9]+)?$");
			if( regEx.test( strCurrVal ) == false || 
					( regEx.test( strCurrVal ) && parseFloat( strCurrVal ) <= 0 ) )
			{
				currObj.error = true;
				currObj.errorMsg = $(this).attr( 'data-field-name' ) + ' should be positive number';
				currObj.showError( this );
			}
		}
	} );
}

JumbValidation.prototype.requiredField = function( )
{
	var currObj = this;
	$( this.form ).find( '.jumbRequiredField' ).each( function( index ){
		var strCurrVal = $(this).val();
		if( strCurrVal==null || strCurrVal == '' )
		{
			currObj.error = true;
			currObj.errorMsg = $(this).attr( 'data-field-name' ) + ' is a mandatory field';
			currObj.showError( this );
		}
	} );
}

JumbValidation.prototype.id = 0;

JumbValidation.prototype.showError = function( objCausedError )
{
	if( this.error )
	{
		this.listOfErrMsg += this.errorMsg + "<br/>";
		$(objCausedError).addClass( 'jumbUserValidationError' );
		$(objCausedError).after( "<span id='" + JumbValidation.prototype.id++ + "jumbErrDisp' class='jumbUserValidationErrorMsg'>*" + this.errorMsg + "</span>" );
	}
	else
	{
		$(objCausedError).removeClass( 'jumbUserValidationError' );
	}
}
/**
 * field should contain the class jumbCustomValidtor
 * and function name will be in an attribute jumbCustomValidtor
 * that function will get a parameter which is JumbValidation object
 * 				and the element on which current validation is on.
 * 
 * do custom validation there and if fail set error attr as false
 */
JumbValidation.prototype.customValidation = function( )
{
	var currObj = this;
	$( this.form ).find( '.jumbCustomValidtor' ).each( function( index ){
		var funcNameToCall = $( this ).attr( 'jumbCustomValidtor' );
		if( currObj.isStrAFunction( funcNameToCall ) == false )
		{
			return;
		}
		eval( funcNameToCall + '(currObj, this)' );
		currObj.showError( this );
	});
}
JumbValidation.prototype.isStrAFunction = function( strFuncName )
{
	var toObj = window[ strFuncName ];
	return ( (typeof toObj!='undefined') 
			&& jQuery.isFunction(toObj) );
}
JumbValidation.prototype.getJumbFormValueById = function( strId )
{
	return $( this.form ).find( '#' + strId ).val();
}