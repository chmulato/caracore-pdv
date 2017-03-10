$(document).on("change", ".js-calcular-desconto", function() {
    var subTotal = 0;
    var desconto = 0;
    var total = 0;
    var parcial = 0;
	subTotal  = $(".js-calcular-sub-total").val().replace('.','').replace(',','.');
	desconto = $(".js-calcular-desconto").val().replace('.','').replace(',','.');
	if ((desconto > 0) && (desconto <= 100)) {
		parcial = (subTotal * desconto)/100;
		total = subTotal - parcial;
	    total = parseFloat(Math.round(total * 100) / 100).toFixed(2);
	    total = total.replace('.','');
	} else {
		total = subTotal.replace('.','');
	}
    $(".js-calcular-total").val(formatReal(total));
});