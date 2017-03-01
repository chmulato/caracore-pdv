$('#atualizaDescontoModal').on('show.bs.modal', function(event) {
	  var botao = $(event.relatedTarget);
	  var nome = botao.data('nome');
	  var desconto = botao.data('desconto');
	  var url = botao.data('url-atualizar');
	  
	  var modal = $(this);
	  var form = modal.find('form');
	  form.attr('action', url);
	  
	  $('#desconto').val(desconto);

	  modal.find('.modal-body span').html('Deseja alterar o desconto ' + desconto + ' do item <strong>' + nome + '</strong>?');
	});		

$('#atualizaQuantidadeModal').on('show.bs.modal', function(event) {
	  var botao = $(event.relatedTarget);
	  var nome = botao.data('nome');
	  var quantidade = botao.data('quantidade');
	  var url = botao.data('url-atualizar');
	  
	  var modal = $(this);
	  var form = modal.find('form');
	  form.attr('action', url);
	  
	  $('#quantidade').val(quantidade);

	  modal.find('.modal-body span').html('Deseja alterar a quantidade ' + quantidade + ' do item <strong>' + nome + '</strong>?');
	});		
