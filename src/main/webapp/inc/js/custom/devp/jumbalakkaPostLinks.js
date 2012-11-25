jumbalakka.prototype.PostLinks = function ()
{
	$( 'a.jumbalakkaPostLinks' ).each( function(index) {
		$( this ).click( function(e){
			e.preventDefault();
			new JumbalakkaLinkPost( $(this).attr('href') ).doPost();
		} );
	});
};
