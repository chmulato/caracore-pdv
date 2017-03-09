$('#cadastrarClienteModal').on('show.bs.modal', function(event) {
	  var botao = $(event.relatedTarget);
	  var pagamento = botao.data('codigo');
	  var url = botao.data('url-cadastrar');
	  
	  var modal = $(this);
	  var form = modal.find('form');
	  form.attr('action', url);

	  $('#pagamento').val(pagamento);
	  
	  // remove estilo
	  document.getElementsByClassName('easy-autocomplete')[0].style = "none";
	  modal.find('.modal-body span').html('Deseja cadastrar Cliente/CPF?');
	});		
