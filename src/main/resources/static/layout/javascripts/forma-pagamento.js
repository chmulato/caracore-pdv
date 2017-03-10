$("#pesquisa_cliente").easyAutocomplete(options);
$(document).on("change", ".js-somar-valor", function() {
    var sum = 0;
    $(".js-somar-valor").each(function() {
    	sum += +$(this).val().replace('.','').replace(',','.');
    });
    sum = parseFloat(Math.round(sum * 100) / 100).toFixed(2);
    sum = sum.replace('.','');
    sum = sum.replace(',','');
    $(".js-total-da-soma").val(formatReal(sum));
});
$(document).on("change", ".js-pagar-desconto", function() {
    var subTotal = 0;
    var desconto = 0;
    var totalAPagar = 0;
    var valorDesconto = 0;
	subTotal  = $(".js-pagar-sub-total").val().replace('.','').replace(',','.');
	desconto = $(".js-pagar-desconto").val().replace('.','').replace(',','.');
	if ((desconto >= 0) && (desconto <= 100)) {
		valorDesconto = (subTotal * desconto)/100;
		totalAPagar = subTotal - valorDesconto;
		
		valorDesconto = parseFloat(Math.round(valorDesconto * 100) / 100).toFixed(2);
		valorDesconto = valorDesconto.replace('.','');
	    
		totalAPagar = parseFloat(Math.round(totalAPagar * 100) / 100).toFixed(2);
		totalAPagar = totalAPagar.replace('.','');
	} else {
		valorDesconto = valorDesconto.replace('.','');
		totalAPagar = subTotal.replace('.','');
	}
    $(".js-pagar-valor-desconto").val(formatReal(valorDesconto));
    $(".js-pagar-total-a-pagar").val(formatReal(totalAPagar));
});