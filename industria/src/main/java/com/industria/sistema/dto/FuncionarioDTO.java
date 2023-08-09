package com.industria.sistema.dto;

import java.math.BigDecimal;

public class FuncionarioDTO {

    private Long id;
    private String nome;
    private String dataNascimentoFormatted;
    private String salarioFormatted;
    private String funcao;
    private BigDecimal salariosMinimos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimentoFormatted() {
        return dataNascimentoFormatted;
    }

    public void setDataNascimentoFormatted(String dataNascimentoFormatted) {
        this.dataNascimentoFormatted = dataNascimentoFormatted;
    }

    public String getSalarioFormatted() {
        return salarioFormatted;
    }

    public void setSalarioFormatted(String salarioFormatted) {
        this.salarioFormatted = salarioFormatted;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
    
    public BigDecimal getSalariosMinimos() {
        return salariosMinimos;
    }

    public void setSalariosMinimos(BigDecimal salariosMinimos) {
        this.salariosMinimos = salariosMinimos;
    }
}