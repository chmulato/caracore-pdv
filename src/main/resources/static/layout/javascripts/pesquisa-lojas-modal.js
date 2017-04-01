$('#relatorioLojaModal').on('show.bs.modal', function(event) {
	  var botao = $(event.relatedTarget);
	  var nome = botao.data('nome');
	  var url = botao.data('url-selecionar');
	  
	  var modal = $(this);
	  var form = modal.find('form');
	  form.attr('action', url);

	  modal.find('.modal-body span').html('Relatório da loja <strong>' + nome + '</strong>?');
	});		

$('#relatorioLojaPeriodoModal').on('show.bs.modal', function(event) {
	  var botao = $(event.relatedTarget);
	  var nome = botao.data('nome');
	  var url = botao.data('url-selecionar');
	  
	  var modal = $(this);
	  var form = modal.find('form');
	  form.attr('action', url);

	  modal.find('.modal-body span').html('Relatório da loja <strong>' + nome + '</strong>?');
	});		

$('#relatorioEstoqueLojaModal').on('show.bs.modal', function(event) {
	  var botao = $(event.relatedTarget);
	  var nome = botao.data('nome');
	  var url = botao.data('url-selecionar');
	  
	  var modal = $(this);
	  var form = modal.find('form');
	  form.attr('action', url);

	  modal.find('.modal-body span').html('Relatório de estoque da loja <strong>' + nome + '</strong>?');
	});		
