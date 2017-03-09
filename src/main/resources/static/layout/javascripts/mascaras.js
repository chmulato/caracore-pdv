jQuery("#cpf").mask("999.999.999-99");
jQuery("#cpf_modal").mask("999.999.999-99");
$('[rel="tooltip"]').tooltip();
$('.js-moeda').maskNumber({
	decimal: ',',
	thousands: '.'
});
$("#pesquisa_cliente").easyAutocomplete(options);
$(document).on("change", ".js-somar-valor", function() {
    var sum = 0;
    $(".js-somar-valor").each(function() {
    	sum += +$(this).val().replace('.','').replace(',','.');
    });
    sum = parseFloat(Math.round(sum * 100) / 100).toFixed(2);
    sum = sum.replace('.',',');
    $(".js-total-da-soma").val(sum);
});

