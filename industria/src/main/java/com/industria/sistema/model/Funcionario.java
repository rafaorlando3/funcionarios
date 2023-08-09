package com.industria.sistema.model;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Funcionario extends Pessoa {

    private long id;
    private static Long contadorId = 1L;

    @NotNull(message = "O salário é obrigatório.")
    @DecimalMin(value = "0.01", message = "O salário deve ser maior que zero.")
    private BigDecimal salario;

    @NotBlank(message = "A função é obrigatória.")
    private String funcao;

    public Funcionario() {
        this.id = contadorId++;
    }

    public Funcionario(Pessoa pessoa, BigDecimal salario, String funcao) {
    	super(pessoa.getNome(), pessoa.getDataNascimento());
    	this.id = contadorId++;
        this.salario = salario;
        this.funcao = funcao;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    @Override
    public String toString() {
        return "Funcionario [id=" + id + ", nome=" + getNome() + ", dataNascimento=" + getDataNascimento() + ", salario=" + salario
                + ", funcao=" + funcao + "]";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario funcionario = (Funcionario) o;
        return Objects.equals(id, funcionario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
