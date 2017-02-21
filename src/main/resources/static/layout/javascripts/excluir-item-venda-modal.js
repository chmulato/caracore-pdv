$('#confirmaExclusaoModal').on('show.bs.modal', function(event) {
	var button = $(event.relatedTarget);
	
	var codigoItem = button.data('codigo');
	var descricaoItem = button.data('descricao');
	
	var modal = $(this);
	var form = modal.find('form');
	var action = form.data('url-base');
	if (!action.endsWith('/')) {
		action += '/';
	}
	form.attr('action', action + codigoItem);
	
	modal.find('.modal-body span').html('Tem certeza que deseja excluir o título <strong>' + descricaoItem + '</strong>?');
	
});
