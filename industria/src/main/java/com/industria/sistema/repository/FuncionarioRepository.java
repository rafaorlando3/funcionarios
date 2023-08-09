package com.industria.sistema.repository;

import com.industria.sistema.model.Funcionario;
import com.industria.sistema.model.Pessoa;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FuncionarioRepository {

    private List<Funcionario> funcionarios = new ArrayList<>();

    public List<Funcionario> listarFuncionarios() {
    	if (funcionarios.isEmpty()) {
    		restaurarListaPadrao();
            return new ArrayList<>(funcionarios);
        }
        return funcionarios;
    }

    public Optional<Funcionario> encontrarFuncionarioPorId(Long id) {
        return funcionarios.stream()
                .filter(funcionario -> Long.valueOf(funcionario.getId()).equals(id))
                .findFirst();
    }

    public Funcionario salvarFuncionario(Funcionario funcionario) {
        funcionarios.add(funcionario);
        return funcionario;
    }

    public void removerFuncionario(Funcionario funcionario) {
        funcionarios.remove(funcionario);
    }

    public Funcionario atualizarFuncionario(Funcionario funcionarioAtualizado) {
        Funcionario funcionarioExistente = encontrarFuncionarioPorId(funcionarioAtualizado.getId()).orElse(null);
        if (funcionarioExistente != null) {
            funcionarioExistente.setNome(funcionarioAtualizado.getNome());
            funcionarioExistente.setDataNascimento(funcionarioAtualizado.getDataNascimento());
            funcionarioExistente.setSalario(funcionarioAtualizado.getSalario());
            funcionarioExistente.setFuncao(funcionarioAtualizado.getFuncao());
        }
        return funcionarioExistente;
    }
    
    public void restaurarListaPadrao() {
        funcionarios.clear();

        Pessoa pessoa1 = new Pessoa("Maria", LocalDate.of(2000, 10, 18));
        Funcionario funcionario1 = new Funcionario(pessoa1, BigDecimal.valueOf(2009.44), "Operador");
        funcionarios.add(funcionario1);

        Pessoa pessoa2 = new Pessoa("João", LocalDate.of(1990, 5, 12));
        Funcionario funcionario2 = new Funcionario(pessoa2, BigDecimal.valueOf(2284.38), "Operador");
        funcionarios.add(funcionario2);

        Pessoa pessoa3 = new Pessoa("Caio", LocalDate.of(1961, 5, 2));
        Funcionario funcionario3 = new Funcionario(pessoa3, BigDecimal.valueOf(9836.14), "Coordenador");
        funcionarios.add(funcionario3);

        Pessoa pessoa4 = new Pessoa("Miguel", LocalDate.of(1988, 10, 14));
        Funcionario funcionario4 = new Funcionario(pessoa4, BigDecimal.valueOf(19119.88), "Diretor");
        funcionarios.add(funcionario4);

        Pessoa pessoa5 = new Pessoa("Alice", LocalDate.of(1995, 1, 5));
        Funcionario funcionario5 = new Funcionario(pessoa5, BigDecimal.valueOf(2234.68), "Recepcionista");
        funcionarios.add(funcionario5);

        Pessoa pessoa6 = new Pessoa("Heitor", LocalDate.of(1999, 11, 19));
        Funcionario funcionario6 = new Funcionario(pessoa6, BigDecimal.valueOf(1582.72), "Operador");
        funcionarios.add(funcionario6);

        Pessoa pessoa7 = new Pessoa("Arthur", LocalDate.of(1993, 3, 31));
        Funcionario funcionario7 = new Funcionario(pessoa7, BigDecimal.valueOf(4071.84), "Contador");
        funcionarios.add(funcionario7);

        Pessoa pessoa8 = new Pessoa("Laura", LocalDate.of(1994, 7, 8));
        Funcionario funcionario8 = new Funcionario(pessoa8, BigDecimal.valueOf(3017.45), "Gerente");
        funcionarios.add(funcionario8);

        Pessoa pessoa9 = new Pessoa("Heloísa", LocalDate.of(2003, 5, 24));
        Funcionario funcionario9 = new Funcionario(pessoa9, BigDecimal.valueOf(1606.85), "Eletricista");
        funcionarios.add(funcionario9);

        Pessoa pessoa10 = new Pessoa("Helena", LocalDate.of(1996, 9, 2));
        Funcionario funcionario10 = new Funcionario(pessoa10, BigDecimal.valueOf(2799.93), "Gerente");
        funcionarios.add(funcionario10);
    }

    public List<Funcionario> getAll() {
        return funcionarios;
    }

}
