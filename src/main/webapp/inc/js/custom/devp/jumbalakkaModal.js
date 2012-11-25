//keeps on the page a modal id for later ref#
jumbalakka.prototype.modalId = 0;

jumbalakka.prototype.jumbCreateModal = function( title, msg, closeButton, buttonNames, eventNames )
{
	jumbalakka.prototype.modalId++; 
	var modalBody = 	"<div id='jumbModalId" + jumbalakka.prototype.modalId + "' class='modal hide fade'>" +
						"	<div class='modal-header'>";
	if( closeButton )	//if true show close button
	{
		modalBody += "<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>";
	}
	modalBody += "" +					
				"		<h3>" + title + "</h3>" +
				"	</div>" +
				"	<div class='modal-body'>" +
				"		<p>" + msg + "</p>" +
				"	</div>";
	if( typeof buttonName !=  'undefined' && 
			buttonName.length > 0 )
	{
		modalBody += "" +
					"	<div class='modal-footer'>";
		for( i=0;i<buttonName.length; i++ )
		{
			modalBody += "" +
						"		<a href='#' class='btn _" + buttonNames[i] + 
									jumbalakka.prototype.modalId + "Click'>" + 
									buttonNames[i]  + "</a>";
		}
		modalBody += "" +
					"	</div>";
	}
	modalBody += "" +
				"</div>";
	
	$( 'body' ).append( modalBody );
	jumbalakka.prototype.jumbModal = 
		$( "#jumbModalId" + jumbalakka.prototype.modalId ).modal({show: false, });
	
	if( typeof buttonName !=  'undefined' && 
			buttonName.length > 0 )
	{
		for( i=0;i<buttonName.length; i++ )
		{
			$( ".btn _" + buttonNames[i] + jumbalakka.prototype.modalId + "Click" ).click( function(e){
				eval( 'eventNames[i](this)' );
			} );
		}
	}
	
	this.show = function()
	{
		jumbalakka.prototype.jumbModal.modal( 'show' );
	};
	
	this.hide = function()
	{
		jumbalakka.prototype.jumbModal.modal( 'hide' );
	};
	
	this.destroy = function()
	{
		$( "#jumbModalId" + jumbalakka.prototype.modalId ).remove();
	};
	return this;
};

jumbalakka.prototype.jumbSimpleModal = function( title, msg )
{
	//var modal = jumbalakka.prototype.jumbCreateModal.call( this, title, msg, true );
	var modal = jumbalakka.prototype.jumbCreateModal(title, msg, true);
	modal.show();
};