$(document).ready(function() {
	let funcionarios = [];
	let mainOptions = [
		{ id: 'none', text: 'Filtros' },
		{ id: 'todos', text: 'Exibir Todos' },
		{ id: 'funcao', text: 'Função' },
		{ id: 'idade', text: 'Maior idade' },
		{ id: 'salarioMinimo', text: 'Salários' },
		{ id: 'ordemAlfabetica', text: 'Ordem alfabética' },
		{ id: 'aniversariantes', text: 'Aniversariantes' }
	];
	const totalSalariosElement = $("#total-salarios");

	function carregarFuncionarios() {
		return new Promise(function(resolve, reject) {
			$.get('/api/funcionarios/', function(data) {
				funcionarios = data;
				resolve();
			}).fail(function() {
				reject();
			});
		});
	}

	carregarFuncionarios().then(function() {
		atualizarTabela(funcionarios);
	}).catch(function() {
		exibirMensagem('Erro ao carregar funcionários!', 'alert-danger');
	});

	$('#salvarFuncionario').click(function() {
		let nome = $('#nome').val();
		let dataNascimento = $('#dataNascimento').val();
		let salario = $('#salario').val();
		let funcao = $('#funcao').val();
		let salarioFormatted = salario.toLocaleString('pt-br', { style: 'currency', currency: 'BRL' });

		$.ajax({
			type: 'POST',
			url: '/api/funcionarios/add',
			data: JSON.stringify({
				'nome': nome,
				'dataNascimentoFormatted': dataNascimento,
				'salarioFormatted': salarioFormatted,
				'funcao': funcao
			}),
			success: function() {
				carregarFuncionarios().then(function() {
					atualizarTabela(funcionarios);

					$('#modalNovoFuncionario').modal('hide');
					$('#novoFuncionarioForm').trigger('reset');

					exibirMensagem('Funcionário salvo com sucesso!', 'alert-success');

					carregarFuncionarios().then(function() {
						atualizarTabela(funcionarios);
						atualizarTotalSalarios();
					}).catch(function() {
						$('#modalNovoFuncionario').modal('hide');
						exibirMensagem('Erro ao carregar funcionários!', 'alert-danger');
					});
				}).catch(function() {
					$('#modalNovoFuncionario').modal('hide');
					exibirMensagem('Erro ao carregar funcionários!', 'alert-danger');
				});
			},
			error: function() {
				exibirMensagem('Erro ao salvar o funcionário!', 'alert-danger');
			},
			contentType: 'application/json',
			dataType: 'json'
		});
	});

	// Abrir modal de edição ao clicar no botão Editar
	$(document).on('click', '.btn-editar', function(event) {
		event.preventDefault();
		let funcionarioId = $(this).data('id');
		let funcionario = funcionarios.find(function(item) {
			return item.id === funcionarioId;
		});

		if (!funcionario) {
			$('#modalAtualizaFuncionario').modal('hide');
			exibirMensagem('Funcionário não encontrado!', 'alert-danger');
			return;
		}

		preencherModalEdicao(funcionario);
	});

	// Salvar funcionário editado
	$('#salvarFuncionarioEdicao').click(function() {
		const funcionarioId = $('#modalAtualizaFuncionario').data('funcionario-id');
		const funcionario = funcionarios.find((item) => item.id === funcionarioId);

		if (!funcionario) {
			$('#modalAtualizaFuncionario').modal('hide');
			exibirMensagem('Funcionário não encontrado!', 'alert-danger');
			return;
		}

		const nome = $('#nomeAtualizar').val();
		const dataNascimento = $('#dataNascimentoAtualizar').val();
		const salario = $('#salarioAtualizar').val();
		const funcao = $('#funcaoAtualizar').val();
		const aumento = $('#aumento').val() ? $('#aumento').val() : 0;

		// Calcular o novo salário com o aumento
		const novoSalario = calcularSalarioAtualizado(parseFloat(salario), parseFloat(aumento));

		// Atualizar os dados do funcionário com as informações editadas
		funcionario.nome = nome;
		funcionario.dataNascimentoFormatted = dataNascimento;
		funcionario.salarioFormatted = novoSalario.toLocaleString('pt-BR', {
			style: 'currency',
			currency: 'BRL'
		});
		funcionario.salario = novoSalario;
		funcionario.funcao = funcao;

		$.ajax({
			type: 'PUT',
			url: `/api/funcionarios/${funcionario.id}`,
			data: JSON.stringify(funcionario),
			contentType: 'application/json',
			dataType: 'json',
			success: function() {
				const index = funcionarios.findIndex((item) => item.id === funcionario.id);

				$('#modalAtualizaFuncionario').modal('hide');
				exibirMensagem('Funcionário atualizado com sucesso!', 'alert-success');

				// Verificar se o funcionário foi encontrado no array
				if (index !== -1) {
					// Formatar a data para o formato "dd/MM/yyyy"
					const dataNascimento = $('#dataNascimentoAtualizar').val();
					const dataNascimentoFormatted = formatarData(dataNascimento);

					// Atualizar o funcionário no array com as informações do retorno da requisição
					funcionarios[index] = {
						id: funcionario.id,
						nome: nome,
						dataNascimentoFormatted: dataNascimentoFormatted,
						salarioFormatted: novoSalario.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }),
						salario: novoSalario,
						funcao: funcao
					};

					atualizarTabela(funcionarios);
					atualizarTotalSalarios();
				}
			},
			error: function() {
				$('#modalAtualizaFuncionario').modal('hide');
				exibirMensagem('Erro ao salvar o funcionário!', 'alert-danger');
			}
		});
	});

	// Função para excluir um funcionário da lista pelo ID
	function excluirFuncionario(id) {
		return new Promise(function(resolve, reject) {
			$.ajax({
				type: 'DELETE',
				url: `/api/funcionarios/${id}`,
				success: function() {
					// Atualizar a lista de funcionários após a exclusão
					funcionarios = funcionarios.filter(function(funcionario) {
						return funcionario.id !== id;
					});

					// Atualizar a lista de funcionários filtrados com a pesquisa
					let search = $('#searchField').val().toLowerCase();
					if (search === "") {
						atualizarTabela(funcionarios);
					} else {
						let filteredFuncionarios = funcionarios.filter(function(funcionario) {
							return funcionario.nome.toLowerCase().includes(search);
						});
						atualizarTabela(filteredFuncionarios);
					}
					resolve();
				},
				error: function() {
					reject();
				},
				contentType: 'application/json',
				dataType: 'json'
			});
		});
	}

	// Evento de clique para o botão de exclusão na tabela de funcionários
	$(document).on('click', '.btn-excluir', function(event) {
		event.preventDefault();
		const funcionarioId = $(this).data('id');

		if (confirm('Tem certeza que deseja excluir este funcionário?')) {
			excluirFuncionario(funcionarioId)
				.then(function() {
					// Remove a linha da tabela correspondente ao funcionário excluído
					$(`tr[data-id="${funcionarioId}"]`).remove();
					exibirMensagem('Funcionário excluído com sucesso!', 'alert-success');
					atualizarTotalSalarios();
				})
				.catch(function() {
					exibirMensagem('Erro ao excluir funcionário!', 'alert-danger');
				});
		}
	});


	$('#searchField').keyup(function() {
		let search = $('#searchField').val().toLowerCase();
		if (search === "") {
			atualizarTabela(funcionarios);
		} else {
			let filteredFuncionarios = funcionarios.filter(function(funcionario) {
				return funcionario.nome.toLowerCase().includes(search);
			});
			atualizarTabela(filteredFuncionarios);
		}
	});

	$('#restaurar').click(function() {
		$.get('/api/funcionarios/restaurar')
			.done(function(data) {
				exibirMensagem(data, 'alert-success');
				carregarFuncionarios().then(function() {
					atualizarTabela(funcionarios);
					atualizarTotalSalarios();
				}).catch(function() {
					exibirMensagem('Erro ao carregar funcionários!', 'alert-danger');
				});
			})
			.fail(function() {
				exibirMensagem('Erro ao restaurar lista padrão.', 'alert-danger');
			});
	});

	function atualizarTabela(data, agruparPorFuncao = false) {
		let table = $('tbody');
		table.empty();

		if (agruparPorFuncao) {
			for (let funcao in data) {
				let funcionarios = data[funcao];

				funcionarios.forEach(function(item) {
					let row = $('<tr data-id="' + item.id + '">');
					row.append('<td>' + item.nome + '</td>');
					row.append('<td>' + item.dataNascimentoFormatted + '</td>');
					row.append('<td>' + item.salarioFormatted + '</td>');
					row.append('<td>' + item.funcao + '</td>');

					let editButton = $('<button type="button" class="btn btn-sm btn-primary btn-editar" data-id="' + item.id + '"><i class="fas fa-edit"></i></button>');
					editButton.attr('data-toggle', 'modal');
					editButton.attr('data-target', '#modalAtualizaFuncionario');
					let editColumn = $('<td>').append(editButton);
					row.append(editColumn);

					let deleteButton = $('<button type="button" class="btn btn-sm btn-danger btn-excluir" data-id="' + item.id + '"><i class="fas fa-trash-alt"></i></button>');
					let deleteColumn = $('<td>').append(deleteButton);
					row.append(deleteColumn);

					table.append(row);
				});
			}
		} else {
			data.forEach(function(item) {
				let row = $('<tr data-id="' + item.id + '">');
				row.append('<td>' + item.nome + '</td>');
				row.append('<td>' + item.dataNascimentoFormatted + '</td>');
				row.append('<td>' + item.salarioFormatted + '</td>');
				row.append('<td>' + item.funcao + '</td>');

				let editButton = $('<button type="button" class="btn btn-sm btn-primary btn-editar" data-id="' + item.id + '"><i class="fas fa-edit"></i></button>');
				editButton.attr('data-toggle', 'modal');
				editButton.attr('data-target', '#modalAtualizaFuncionario');
				let editColumn = $('<td>').append(editButton);
				row.append(editColumn);

				let deleteButton = $('<button type="button" class="btn btn-sm btn-danger btn-excluir" data-id="' + item.id + '"><i class="fas fa-trash-alt"></i></button>');
				let deleteColumn = $('<td>').append(deleteButton);
				row.append(deleteColumn);

				table.append(row);
			});
		}
	}

	// Filtro por Função
	$('#filterCombo').on('select2:select', function(e) {
		let selectedFilter = e.params.data.id;
		let monthsData = [
			{ id: '1', text: 'Janeiro' },
			{ id: '2', text: 'Fevereiro' },
			{ id: '3', text: 'Março' },
			{ id: '4', text: 'Abril' },
			{ id: '5', text: 'Maio' },
			{ id: '6', text: 'Junho' },
			{ id: '7', text: 'Julho' },
			{ id: '8', text: 'Agosto' },
			{ id: '9', text: 'Setembro' },
			{ id: '10', text: 'Outrubro' },
			{ id: '11', text: 'Novembro' },
			{ id: '12', text: 'Dezembro' }
		];

		if (selectedFilter === 'funcao') {
			$.get('/api/funcionarios/agrupar-por-funcao')
				.done(function(data) {
					atualizarTabela(data, true);
					exibirMensagem('Ordenando funcionários agrupados por função.', 'alert-info');
				})
				.fail(function() {
					exibirMensagem('Erro ao filtrar por função', 'alert-danger');
				});
		} else if (selectedFilter === 'aniversariantes') {

			$(this).empty().select2({
				minimumResultsForSearch: Infinity,
				data: [
					{ id: 'aniversariantes', text: 'Aniversariantes' },
					{ id: 'months', text: 'Meses', children: monthsData }
				]
			});

			$(this).select2('open');
		} else if (!isNaN(selectedFilter)) {
			let selectedMonth = parseInt(selectedFilter);

			if (selectedMonth) {
				$.get(`/api/funcionarios/aniversariantes/${selectedMonth}`)
					.done(function(data) {
						atualizarTabela(data);

						if (data.length > 0) {
							exibirMensagem('Exibindo os aniversariantes do mês ' + selectedMonth + '.', 'alert-info');
						} else {
							exibirMensagem('Nenhum aniversariantes no mês ' + selectedMonth + '.', 'alert-warning');
						}

					})
					.fail(function() {
						exibirMensagem('Erro ao filtrar por idade maior', 'alert-danger');
					});
			}
		} else if (selectedFilter === 'idade') {
			$.get('/api/funcionarios/maior-idade')
				.done(function(data) {
					if (Object.keys(data).length !== 0) {
						let funcionario = data;
						const modalBody = $("#funcionarioMaiorIdade .modal-body");

						modalBody.empty();

						let infoHtml = `<p><strong>Nome:</strong> ${funcionario.nome}</p>
							            <p><strong>Idade:</strong> ${calcularIdade(funcionario.dataNascimentoFormatted)}</p>
							            <p><strong>Função:</strong> ${funcionario.funcao}</p>
							            <p><strong>Data de Nascimento:</strong> ${funcionario.dataNascimentoFormatted}</p>
							            <p><strong>Salário:</strong> ${funcionario.salarioFormatted}</p>
							        `;
						modalBody.html(infoHtml);
						$("#funcionarioMaiorIdade").modal("show");
					} else {
						exibirMensagem('Nenhum funcionário encontrado.', 'alert-info');
					}
				})
				.fail(function() {
					exibirMensagem('Erro ao filtrar por maior idade.', 'alert-danger');
				});
		} else if (selectedFilter === 'ordemAlfabetica') {
			$.get('/api/funcionarios/ordenar-por-nome')
				.done(function(data) {
					atualizarTabela(data);
					exibirMensagem('Ordenando funcionários em ordem alfabetica.', 'alert-info');
				})
				.fail(function() {
					exibirMensagem('Erro ao filtrar por ordem alfabética', 'alert-danger');
				});
		} else if (selectedFilter === 'salarioMinimo') {
			$.get('/api/funcionarios/salarios-minimos')
				.done(function(data) {
					if (data.length !== 0) {
						const salariosMinimosList = data;
						const modalBody = $("#funcionarioMaiorIdade .modal-body");

						modalBody.empty();

						const tableHtml = `<table class="table table-striped table-bordered">
							                    <thead>
							                        <tr>
							                            <th scope="col">Nome</th>
							                            <th scope="col">Salários Mínimos</th>
							                        </tr>
							                    </thead>
							                    <tbody>
							                        ${salariosMinimosList.map(funcionarioSalariosMinimos => `
							                            <tr>
							                                <td>${funcionarioSalariosMinimos.nome}</td>
							                                <td>${funcionarioSalariosMinimos.salariosMinimos.toFixed(2)}</td>
							                            </tr>
							                        `).join('')}
							                    </tbody>
							                </table>
							            `;

						modalBody.append(tableHtml);

						$("#funcionarioMaiorIdade").modal("show");
					} else {
						exibirMensagem('Nenhum funcionário encontrado.', 'alert-info');
					}
				})
				.fail(function() {
					exibirMensagem('Erro ao filtrar por maior idade.', 'alert-danger');
				});
		} else {
			atualizarTabela(funcionarios);
		}
	});

	// Função para formatar a data no formato 'dd/mm/yyyy'
	function formatarData(data) {
		const dataObj = new Date(data);
		const dia = String(dataObj.getDate() + 1).padStart(2, '0');
		const mes = String(dataObj.getMonth() + 1).padStart(2, '0');
		const ano = dataObj.getFullYear();
		return `${dia}/${mes}/${ano}`;
	}

	// Função para calcular o salário com base no percentual de aumento
	function calcularSalarioAtualizado(salario, percentualAumento) {
		const aumento = salario * (percentualAumento / 100);
		return salario + aumento;
	}

	function preencherModalEdicao(funcionario) {
		$("#nomeAtualizar").val(funcionario.nome);
		// Formatar a data de nascimento antes de preencher o campo
		const [dia, mes, ano] = funcionario.dataNascimentoFormatted.split('/');
		const dataNascimentoFormatted = `${ano}-${mes}-${dia}`;
		$("#dataNascimentoAtualizar").val(dataNascimentoFormatted);

		// Remover os caracteres não numéricos e converter para número antes de preencher o campo
		const salarioFormatted = parseFloat(funcionario.salarioFormatted.replace(/[^\d,]/g, "").replace(",", "."));
		$("#salarioAtualizar").val(salarioFormatted);
		$("#funcaoAtualizar").val(funcionario.funcao);
		$('#modalAtualizaFuncionario').data('funcionario-id', funcionario.id);
	}

	function exibirMensagem(mensagem, tipo) {
		let divMensagem = $('#message');
		divMensagem.removeClass('alert-success alert-danger').addClass(tipo);
		divMensagem.text(mensagem);
		divMensagem.show().delay(5000).fadeOut();
	}

	$('#filterCombo').on('select2:select', function(e) {
		if (e.params.data.id === 'aniversariantes') {
			// Exibir os meses
			$('.select2-results__group:contains("Aniversariantes")').find('.select2-results__option').show();
		} else {
			// Esconder os meses
			$('.select2-results__group:contains("Aniversariantes")').find('.select2-results__option').hide();
		}
	});

	$('#filterCombo').on('select2:close', function(e) {
		$(this).empty().select2({
			minimumResultsForSearch: Infinity,
			data: mainOptions
		});
	});

	$('#filterCombo').select2({
		minimumResultsForSearch: Infinity,
		placeholder: 'Filtros',
		data: mainOptions
	}).addClass('w-100 rounded form-control');

	function calcularIdade(dataNascimento) {
		const dataNascimentoArray = dataNascimento.split('/');
		const diaNascimento = parseInt(dataNascimentoArray[0]);
		const mesNascimento = parseInt(dataNascimentoArray[1]) - 1; // Mês é zero-indexed
		const anoNascimento = parseInt(dataNascimentoArray[2]);

		const hoje = new Date();
		const idade = hoje.getFullYear() - anoNascimento;

		if (hoje.getMonth() < mesNascimento || (hoje.getMonth() === mesNascimento && hoje.getDate() < diaNascimento)) {
			return idade - 1;
		}
		return idade;
	}

	function atualizarTotalSalarios() {
		const totalSalarios = Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" }).format(calcularTotalSalarios());
		totalSalariosElement.text(`Total de Salários: ${totalSalarios}`);
	}

	function calcularTotalSalarios() {
		const linhasTabela = $("tbody tr");
		let totalSalarios = 0;

		linhasTabela.each(function() {
			const salarioText = $(this).find("td:nth-child(3)").text();
			const salarioNumerico = parseFloat(salarioText.replace("R$", "").replace(".", "").replace(",", "."));

			if (!isNaN(salarioNumerico)) {
				totalSalarios += salarioNumerico;
			}
		});

		return totalSalarios;
	}

	atualizarTotalSalarios();
});
