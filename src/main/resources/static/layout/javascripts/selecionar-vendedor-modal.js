$('#selecionaVendedorModal').on('show.bs.modal', function(event) {
	  var botao = $(event.relatedTarget);
	  var nome = botao.data('nome');
	  var url = botao.data('url-selecionar');
	  
	  var modal = $(this);
	  var form = modal.find('form');
	  form.attr('action', url);

	  modal.find('.modal-body span').html('Selecionar o vendedor <strong>' + nome + '</strong>?');
	});		
