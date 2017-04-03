$('#atualizaItemVendaModal').on('show.bs.modal', function(event) {
	  var botao = $(event.relatedTarget);
	  var nome = botao.data('nome');
	  var desconto = botao.data('desconto');
	  var quantidade = botao.data('quantidade');
	  var url = botao.data('url-atualizar');
	  
	  var modal = $(this);
	  var form = modal.find('form');
	  form.attr('action', url);
	  
	  $('#desconto').val(desconto);
	  $('#quantidade').val(quantidade);

	  modal.find('.modal-body span').html('Deseja alterar dados do item <strong>' + nome + '</strong>?');
	});		
