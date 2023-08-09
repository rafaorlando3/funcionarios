package com.industria.sistema.model;
import java.math.BigDecimal;

public class FuncionarioSalariosMinimos {
    private String nome;
    private BigDecimal salariosMinimos;
	
    public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public BigDecimal getSalariosMinimos() {
		return salariosMinimos;
	}
	public void setSalariosMinimos(BigDecimal salariosMinimos) {
		this.salariosMinimos = salariosMinimos;
	} 
}
