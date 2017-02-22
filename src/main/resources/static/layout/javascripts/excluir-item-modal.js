$('#confirmaExclusaoModal').on('show.bs.modal', function (event) {
	  var botao = $(event.relatedTarget);
	  var nome = botao.data('nome');
	  var url = botao.data('url-apagar');
	  
	  var modal = $(this);
	  var form = modal.find('form');
	  form.attr('action', url).append('<input type="hidden" name="_method" value="DELETE" />');
	  
	  modal.find('.modal-body span').html('Tem certeza que deseja excluir o <strong>' + nome + '</strong>?');
	});		
