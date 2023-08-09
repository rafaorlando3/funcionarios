$(document).ready(function() {
	// Função para aplicar as máscaras de formatação
	function aplicarMascaras() {
		$('#dataNascimento').inputmask('99/99/9999', { placeholder: '__/__/____' });
		$('#salario').inputmask('currency', {
			radixPoint: ',',
			groupSeparator: '.',
			allowMinus: false,
			prefix: 'R$ ',
			digits: 2,
			autoUnmask: true,
		});
		$('#aumento').inputmask('numeric', {
			radixPoint: ',',
			suffix: '%',
			allowMinus: false,
			digits: 2,
			autoUnmask: true,
		});
		$('#dataNascimentoAtualizar').inputmask('99/99/9999', { placeholder: '__/__/____' });
		$('#salarioAtualizar').inputmask('currency', {
			radixPoint: ',',
			groupSeparator: '.',
			allowMinus: false,
			prefix: 'R$ ',
			digits: 2,
			autoUnmask: true,
		});
		$('#aumentoAtualizar').inputmask('numeric', {
			radixPoint: ',',
			suffix: '%',
			allowMinus: false,
			digits: 2,
			autoUnmask: true,
		});
	}

	// Função para validar uma data
	  function validarData(input) {
	    const value = input.val();
	    if (value && !/^\d{4}-\d{2}-\d{2}$/.test(value)) {
	      input.addClass('is-invalid'); // Adiciona classe para destacar o campo inválido
	    } else {
	      input.removeClass('is-invalid'); // Remove classe caso seja válido
	    }
	  }

	// Evento blur para limpar campos inválidos e validar data
	$('#dataNascimento').on('blur', function() {
		validarData($(this));
	});

	$('#dataNascimentoAtualizar').on('blur', function() {
		validarData($(this));
	});

	// Evento blur para limpar campos inválidos
	$('#salario').on('blur', function() {
		const value = $(this).val();
		if (value && isNaN(value.replace('.', '').replace(',', '.'))) {
			$(this).val('');
		}
	});

	$('#aumento').on('blur', function() {
		const value = $(this).val();
		if (value && isNaN(value.replace(',', '.'))) {
			$(this).val('');
		}
	});

	$('#salarioAtualizar').on('blur', function() {
		const value = $(this).val();
		if (value && isNaN(value.replace('.', '').replace(',', '.'))) {
			$(this).val('');
		}
	});

	$('#aumentoAtualizar').on('blur', function() {
		const value = $(this).val();
		if (value && isNaN(value.replace(',', '.'))) {
			$(this).val('');
		}
	});

	aplicarMascaras();
});