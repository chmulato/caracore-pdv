jQuery("#cpf").mask("999.999.999-99");
jQuery("#cpf_modal").mask("999.999.999-99");
$('[rel="tooltip"]').tooltip();
$('.js-moeda').maskNumber({
	decimal: ',',
	thousands: '.'
});
$("#pesquisa_cliente").easyAutocomplete(options);

