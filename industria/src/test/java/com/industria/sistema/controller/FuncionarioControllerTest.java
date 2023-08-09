package com.industria.sistema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industria.sistema.dto.FuncionarioDTO;
import com.industria.sistema.model.Funcionario;
import com.industria.sistema.model.Pessoa;
import com.industria.sistema.service.FuncionarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioControllerTest {

	private MockMvc mockMvc;

	@Mock
	private FuncionarioService funcionarioService;

	@InjectMocks
	private FuncionarioController funcionarioController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(funcionarioController).build();
	}

	@Test
	void testListarFuncionarios() throws Exception {
		Pessoa pessoa1 = new Pessoa("João", LocalDate.of(1990, 6, 15));
		Pessoa pessoa2 = new Pessoa("Maria", LocalDate.of(1985, 10, 18));
		Funcionario funcionario1 = new Funcionario(pessoa1, BigDecimal.valueOf(2500.00), "Analista");
		Funcionario funcionario2 = new Funcionario(pessoa2, BigDecimal.valueOf(3000.00), "Gerente");
		List<Funcionario> funcionarios = Arrays.asList(funcionario1, funcionario2);

		when(funcionarioService.listarFuncionarios()).thenReturn(
				funcionarios.stream().map(funcionario -> funcionarioService.formatarFuncionarioDTO(funcionario))
						.collect(Collectors.toList()));

		mockMvc.perform(get("/funcionarios")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(funcionarios.size()))
				.andExpect(jsonPath("$[0].nome").value(pessoa1.getNome()))
				.andExpect(jsonPath("$[0].dataNascimento").value(pessoa1.getDataNascimento().toString()))
				.andExpect(jsonPath("$[0].salario").value(funcionario1.getSalario().doubleValue()))
				.andExpect(jsonPath("$[0].funcao").value(funcionario1.getFuncao()))
				.andExpect(jsonPath("$[1].nome").value(pessoa2.getNome()))
				.andExpect(jsonPath("$[1].dataNascimento").value(pessoa2.getDataNascimento().toString()))
				.andExpect(jsonPath("$[1].salario").value(funcionario2.getSalario().doubleValue()))
				.andExpect(jsonPath("$[1].funcao").value(funcionario2.getFuncao()));
	}

	@Test
	void testEncontrarFuncionarioPorId() throws Exception {
		long id = 1L;
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Maria Silva");
		pessoa.setDataNascimento(LocalDate.of(2000, 10, 18));

		Funcionario funcionario = new Funcionario(pessoa, BigDecimal.valueOf(2009.44), "Operador");

		when(funcionarioService.encontrarFuncionarioPorId(id))
				.thenReturn(funcionarioService.encontrarFuncionarioPorId(funcionario.getId()));

		mockMvc.perform(get("/funcionarios/{id}", id)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.nome").value(funcionario.getNome()))
				.andExpect(jsonPath("$.dataNascimento").value(funcionario.getDataNascimento().toString()))
				.andExpect(jsonPath("$.salario").value(funcionario.getSalario().doubleValue()))
				.andExpect(jsonPath("$.funcao").value(funcionario.getFuncao()));
	}

	@Test
	void testEncontrarFuncionarioPorIdNotFound() throws Exception {
		long id = 100L;

		when(funcionarioService.encontrarFuncionarioPorId(id)).thenReturn(null);

		mockMvc.perform(get("/funcionarios/{id}", id)).andExpect(status().isNotFound());
	}

	@Test
	void testSalvarFuncionario() throws Exception {
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Maria Silva");
		pessoa.setDataNascimento(LocalDate.of(2000, 10, 18));

		Funcionario funcionario = new Funcionario(pessoa, BigDecimal.valueOf(2009.44), "Operador");

		when(funcionarioService.salvarFuncionario(any(Funcionario.class))).thenReturn(funcionario);

		mockMvc.perform(
				post("/funcionarios").content(asJsonString(funcionario)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.nome").value(funcionario.getNome()))
				.andExpect(jsonPath("$.dataNascimento").value(funcionario.getDataNascimento().toString()))
				.andExpect(jsonPath("$.salario").value(funcionario.getSalario().doubleValue()))
				.andExpect(jsonPath("$.funcao").value(funcionario.getFuncao()));
	}

	@Test
	public void testAtualizarFuncionario() throws Exception {
		long funcionarioId = 1L;
		FuncionarioDTO funcionarioExistenteDTO = new FuncionarioDTO();
		funcionarioExistenteDTO.setId(funcionarioId);
		funcionarioExistenteDTO.setNome("Maria");
		funcionarioExistenteDTO.setDataNascimentoFormatted("1990-05-15");
		funcionarioExistenteDTO.setSalarioFormatted("R$ 2500.00");
		funcionarioExistenteDTO.setFuncao("Analista");

		FuncionarioDTO funcionarioAtualizadoDTO = new FuncionarioDTO();
		funcionarioAtualizadoDTO.setId(funcionarioId);
		funcionarioAtualizadoDTO.setNome("Maria Silva");
		funcionarioAtualizadoDTO.setDataNascimentoFormatted("1990-05-15");
		funcionarioAtualizadoDTO.setSalarioFormatted("R$ 3000.00");
		funcionarioAtualizadoDTO.setFuncao("Gerente");

		when(funcionarioService.encontrarFuncionarioPorId(funcionarioId)).thenReturn(funcionarioExistenteDTO);
		when(funcionarioService.atualizarFuncionario(any(FuncionarioDTO.class))).thenReturn(funcionarioAtualizadoDTO);

		mockMvc.perform(put("/funcionarios/{id}", funcionarioId).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(funcionarioAtualizadoDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value(funcionarioAtualizadoDTO.getNome()))
				.andExpect(jsonPath("$.dataNascimento").value(funcionarioAtualizadoDTO.getDataNascimentoFormatted()))
				.andExpect(jsonPath("$.salario").value(funcionarioAtualizadoDTO.getSalarioFormatted()))
				.andExpect(jsonPath("$.funcao").value(funcionarioAtualizadoDTO.getFuncao()));
	}

	@Test
	void testRemoverFuncionario() throws Exception {
		long id = 1L;
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Maria Silva");
		pessoa.setDataNascimento(LocalDate.of(2000, 10, 18));

		Funcionario funcionario = new Funcionario(pessoa, BigDecimal.valueOf(2009.44), "Operador");

		when(funcionarioService.encontrarFuncionarioPorId(id))
				.thenReturn(funcionarioService.encontrarFuncionarioPorId(funcionario.getId()));
		doNothing().when(funcionarioService).removerFuncionario(funcionario.getId());

		mockMvc.perform(delete("/funcionarios/{id}", id)).andExpect(status().isOk());
	}

	private String asJsonString(Object object) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}
}
