$(document).ready(function() {
    var options = {
    	    url: /*[[@{/clientes/clientes}]]*/ 'http://localhost:8080/clientes/clientes',
    	    getValue: function(element) {
    	        return element.nome;
    	    },

    	    list: {
    	        onSelectItemEvent: function() {
    	            var selectedItemValueCpf = $("#pesquisa_cliente").getSelectedItemData().cpf;
    	            var selectedItemValueEmail = $("#pesquisa_cliente").getSelectedItemData().email;
    	            $("#cpf_modal").val(selectedItemValueCpf).trigger("change");
    	            $("#email_modal").val(selectedItemValueEmail).trigger("change");
    	        }		        	    
    	    }
    };
   $("#pesquisa_cliente").easyAutocomplete(options);
 });

$(document).on("change", ".js-somar-valor", function() {
    
	var dinheiro = 0;
	var a_pagar = 0;
    var soma = 0;
    var troco = 0;
    var total = 0;
    
    dinheiro = $(".js-dinheiro").val().replace('.','').replace(',','.');
    total = $(".js-total").val().replace('.','').replace(',','.');
    
    $(".js-somar-valor").each(function() {
    	soma += +$(this).val().replace('.','').replace(',','.');
    });
    
    a_pagar = soma - total;
    
    if (a_pagar > 0) {
    	
    	troco = a_pagar;
    	
    	if ((dinheiro > 0) && (troco <= dinheiro)) {
    		a_pagar = 0;
        	document.getElementsByClassName('js-troco')[0].style = "border-color: green";
    	} else {
        	document.getElementsByClassName('js-troco')[0].style = "border-color: none";
    	}

    	if (dinheiro == 0) {
        	document.getElementsByClassName('js-troco')[0].style = "border-color: none";
    	}
    	
    	if ((troco > dinheiro)) {
    		a_pagar = 0;
        	document.getElementsByClassName('js-troco')[0].style = "border-color: red";
    	}
    }
    
    troco = parseFloat(Math.round(troco * 100) / 100).toFixed(2);
    troco = troco.replace('.','');
    troco = troco.replace(',','');
    
    a_pagar = parseFloat(Math.round(a_pagar * 100) / 100).toFixed(2);
    a_pagar = a_pagar.replace('.','');
    a_pagar = a_pagar.replace(',','');
    
    $(".js-troco").val(formatReal(troco));
    $(".js-a-pagar").val(formatReal(a_pagar));
});
