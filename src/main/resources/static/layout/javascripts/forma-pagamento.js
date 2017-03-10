$("#pesquisa_cliente").easyAutocomplete(options);

$(document).on("change", ".js-somar-valor", function() {
    var soma = 0;
    var saldo = 0;
    var totalAPagar = 0;
    
    totalAPagar  = $(".js-pagar-total-a-pagar").val().replace('.','').replace(',','.');
    
    $(".js-somar-valor").each(function() {
    	soma += +$(this).val().replace('.','').replace(',','.');
    });
    
    saldo = totalAPagar - soma;
    
    saldo = parseFloat(Math.round(saldo * 100) / 100).toFixed(2);
    saldo = saldo.replace('.','');
    saldo = saldo.replace(',','');
    
    soma = parseFloat(Math.round(soma * 100) / 100).toFixed(2);
    soma = soma.replace('.','');
    soma = soma.replace(',','');
    $(".js-total-da-soma").val(formatReal(soma));
    $(".js-saldo").val(formatReal(saldo));
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