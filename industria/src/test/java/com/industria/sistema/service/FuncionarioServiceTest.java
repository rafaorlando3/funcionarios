package com.industria.sistema.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.industria.sistema.dto.FuncionarioDTO;
import com.industria.sistema.model.Funcionario;
import com.industria.sistema.model.Pessoa;
import com.industria.sistema.repository.FuncionarioRepository;

public class FuncionarioServiceTest {

	private FuncionarioService funcionarioService;
	private FuncionarioRepository funcionarioRepository;

	@BeforeEach
	public void setUp() {
		funcionarioRepository = mock(FuncionarioRepository.class);
		funcionarioService = new FuncionarioService(funcionarioRepository);
	}

	@Test
	public void testListarFuncionarios() {
		List<Funcionario> funcionarios = criarFuncionarios();
		when(funcionarioRepository.listarFuncionarios()).thenReturn(funcionarios);

		List<FuncionarioDTO> funcionariosObtidos = funcionarioService.listarFuncionarios();
		assertEquals(funcionarios.size(), funcionariosObtidos.size());
		for (int i = 0; i < funcionarios.size(); i++) {
			assertEquals(funcionarios.get(i), funcionariosObtidos.get(i));
		}
	}

	@Test
	public void testEncontrarFuncionarioPorId() {
		List<Funcionario> funcionarios = criarFuncionarios();
		when(funcionarioRepository.encontrarFuncionarioPorId(1L)).thenReturn(Optional.of(funcionarios.get(0)));
		when(funcionarioRepository.encontrarFuncionarioPorId(2L)).thenReturn(Optional.of(funcionarios.get(1)));

		FuncionarioDTO funcionario1 = funcionarioService.encontrarFuncionarioPorId(1L);
		FuncionarioDTO funcionario2 = funcionarioService.encontrarFuncionarioPorId(2L);

		assertEquals(funcionarios.get(0), funcionario1);
		assertEquals(funcionarios.get(1), funcionario2);
	}

	@Test
	public void testAtualizarFuncionario() {
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Maria Silva");
		pessoa.setDataNascimento(LocalDate.of(2000, 10, 18));

		Funcionario funcionario = new Funcionario(pessoa, BigDecimal.valueOf(2009.44), "Operador");
		when(funcionarioRepository.atualizarFuncionario(any(Funcionario.class))).thenReturn(funcionario);

		FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
		funcionarioDTO.setNome("Maria Silva");
		funcionarioDTO.setDataNascimentoFormatted("1990-05-15");
		funcionarioDTO.setSalarioFormatted("2009.44");
		funcionarioDTO.setFuncao("Operador");

		FuncionarioDTO funcionarioAtualizado = funcionarioService.atualizarFuncionario(funcionarioDTO);

		assertEquals("Maria Silva", funcionarioAtualizado.getNome());
		assertEquals("1990-05-15", funcionarioAtualizado.getDataNascimentoFormatted());
		assertEquals("R$ 2009.44", funcionarioAtualizado.getSalarioFormatted());
		assertEquals("Operador", funcionarioAtualizado.getFuncao());
	}

	@Test
	public void testRemoverFuncionario() {
		List<Funcionario> funcionarios = criarFuncionarios();
		when(funcionarioRepository.encontrarFuncionarioPorId(1L)).thenReturn(Optional.of(funcionarios.get(0)));
		when(funcionarioRepository.encontrarFuncionarioPorId(2L)).thenReturn(Optional.of(funcionarios.get(1)));
		when(funcionarioRepository.encontrarFuncionarioPorId(3L)).thenReturn(Optional.of(funcionarios.get(2)));

		funcionarioService.removerFuncionario(1L);
		funcionarioService.removerFuncionario(2L);
		funcionarioService.removerFuncionario(3L);

		verify(funcionarioRepository, times(1)).removerFuncionario(funcionarios.get(0));
		verify(funcionarioRepository, times(1)).removerFuncionario(funcionarios.get(1));
		verify(funcionarioRepository, times(1)).removerFuncionario(funcionarios.get(2));
	}

	@Test
	public void testDarAumento() {
		List<Funcionario> funcionarios = criarFuncionarios();
		when(funcionarioRepository.encontrarFuncionarioPorId(1L)).thenReturn(Optional.of(funcionarios.get(0)));
		when(funcionarioRepository.encontrarFuncionarioPorId(2L)).thenReturn(Optional.of(funcionarios.get(1)));
		when(funcionarioRepository.encontrarFuncionarioPorId(3L)).thenReturn(Optional.of(funcionarios.get(2)));

		FuncionarioDTO funcionarioComAumento1 = funcionarioService.darAumento(1L, 10);
		FuncionarioDTO funcionarioComAumento2 = funcionarioService.darAumento(2L, 5);
		FuncionarioDTO funcionarioComAumento3 = funcionarioService.darAumento(3L, 20);

		assertEquals(BigDecimal.valueOf(2210.43).setScale(2, RoundingMode.HALF_UP),
				funcionarioComAumento1.getSalarioFormatted());
		assertEquals(BigDecimal.valueOf(2398.60).setScale(2, RoundingMode.HALF_UP),
				funcionarioComAumento2.getSalarioFormatted());
		assertEquals(BigDecimal.valueOf(3600.00).setScale(2, RoundingMode.HALF_UP),
				funcionarioComAumento3.getSalarioFormatted());
	}

	private List<Funcionario> criarFuncionarios() {
		return Arrays.asList(
				new Funcionario(new Pessoa("Maria", LocalDate.of(2000, 10, 18)), BigDecimal.valueOf(2009.44),
						"Operador"),
				new Funcionario(new Pessoa("João", LocalDate.of(1990, 5, 12)), BigDecimal.valueOf(2284.38), "Operador"),
				new Funcionario(new Pessoa("Carlos", LocalDate.of(1995, 7, 25)), BigDecimal.valueOf(3000.00),
						"Gerente"));
	}
}
