package com.industria.sistema.service;

import com.industria.sistema.dto.FuncionarioDTO;
import com.industria.sistema.model.Funcionario;
import com.industria.sistema.model.FuncionarioSalariosMinimos;
import com.industria.sistema.model.Pessoa;
import com.industria.sistema.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    @Autowired
    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<FuncionarioDTO> listarFuncionarios() {
        return funcionarioRepository.listarFuncionarios().stream()
                .map(this::formatarFuncionarioDTO)
                .collect(Collectors.toList());
    }

    public FuncionarioDTO encontrarFuncionarioPorId(Long id) {
        Funcionario funcionario = funcionarioRepository.encontrarFuncionarioPorId(id).orElse(null);
        return funcionario != null ? formatarFuncionarioDTO(funcionario) : null;
    }

    public Funcionario salvarFuncionario(Funcionario funcionario) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(funcionario.getNome());
        pessoa.setDataNascimento(funcionario.getDataNascimento());

        Funcionario novoFuncionario = new Funcionario(pessoa, funcionario.getSalario(), funcionario.getFuncao());
        return funcionarioRepository.salvarFuncionario(novoFuncionario);
    }

    public FuncionarioDTO atualizarFuncionario(FuncionarioDTO dto) {
        Funcionario funcionarioExistente = encontrarFuncionarioPorIdOriginal(dto.getId());

        if (funcionarioExistente == null) {
            return null;
        }
        
        funcionarioExistente.setNome(dto.getNome());
        funcionarioExistente.setDataNascimento(LocalDate.parse(dto.getDataNascimentoFormatted()));
        funcionarioExistente.setSalario(convertSalarioFormattedToBigDecimal(dto.getSalarioFormatted()));
        funcionarioExistente.setFuncao(dto.getFuncao());

        Funcionario funcionarioAtualizado = funcionarioRepository.atualizarFuncionario(funcionarioExistente);
        return formatarFuncionarioDTO(funcionarioAtualizado);
    }

    public void removerFuncionario(Long id) {
        Funcionario funcionario = funcionarioRepository.encontrarFuncionarioPorId(id).orElse(null);
        if (funcionario != null) {
            funcionarioRepository.removerFuncionario(funcionario);
        }
    }

    public FuncionarioDTO darAumento(Long id, double percentual) {
        Funcionario funcionario = funcionarioRepository.encontrarFuncionarioPorId(id).orElse(null);
        if (funcionario != null) {
            BigDecimal aumento = BigDecimal.valueOf(percentual / 100.0);
            BigDecimal salarioAtualizado = funcionario.getSalario().add(funcionario.getSalario().multiply(aumento));
            funcionario.setSalario(salarioAtualizado.setScale(2, RoundingMode.HALF_UP));
            funcionario = funcionarioRepository.atualizarFuncionario(funcionario);
            return formatarFuncionarioDTO(funcionario);
        }
        return null;
    }

    public List<FuncionarioDTO> listarFuncionariosPorFuncao(String funcao) {
        return funcionarioRepository.listarFuncionarios().stream()
                .filter(funcionario -> funcionario.getFuncao().equalsIgnoreCase(funcao))
                .map(this::formatarFuncionarioDTO)
                .collect(Collectors.toList());
    }

    public Map<String, List<FuncionarioDTO>> agruparFuncionariosPorFuncao() {
        return funcionarioRepository.listarFuncionarios().stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao,
                        Collectors.mapping(this::formatarFuncionarioDTO, Collectors.toList())));
    }

    public List<FuncionarioDTO> listarAniversariantesDoMes(int mes) {
        return funcionarioRepository.listarFuncionarios().stream()
                .filter(funcionario -> funcionario.getDataNascimento().getMonthValue() == mes)
                .map(this::formatarFuncionarioDTO)
                .collect(Collectors.toList());
    }

    public FuncionarioDTO encontrarFuncionarioMaiorIdade() {
        Funcionario funcionario = funcionarioRepository.listarFuncionarios().stream()
                .max(Comparator.comparingInt(this::calcularIdade))
                .orElse(null);
        return funcionario != null ? formatarFuncionarioDTO(funcionario) : null;
    }

    private int calcularIdade(Funcionario funcionario) {
        LocalDate dataAtual = LocalDate.now();
        int idade = dataAtual.getYear() - funcionario.getDataNascimento().getYear();
        if (dataAtual.getMonthValue() < funcionario.getDataNascimento().getMonthValue()
                || (dataAtual.getMonthValue() == funcionario.getDataNascimento().getMonthValue()
                && dataAtual.getDayOfMonth() < funcionario.getDataNascimento().getDayOfMonth())) {
            idade--;
        }
        return idade;
    }

    public List<FuncionarioDTO> ordenarFuncionariosPorNome() {
        return funcionarioRepository.listarFuncionarios().stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .map(this::formatarFuncionarioDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal calcularTotalSalarios() {
        return funcionarioRepository.listarFuncionarios().stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<FuncionarioSalariosMinimos> calcularSalariosMinimos() {
        BigDecimal salarioMinimo = BigDecimal.valueOf(1212.00);
        List<FuncionarioSalariosMinimos> salariosMinimosList = new ArrayList<>();

        for (Funcionario funcionario : funcionarioRepository.listarFuncionarios()) {
            BigDecimal salariosMinimos = funcionario.getSalario().divide(salarioMinimo, 2, RoundingMode.HALF_UP);
            FuncionarioDTO funcionarioDTO = formatarFuncionarioDTO(funcionario);

            FuncionarioSalariosMinimos funcionarioSalariosMinimos = new FuncionarioSalariosMinimos();
            funcionarioSalariosMinimos.setNome(funcionarioDTO.getNome());
            funcionarioSalariosMinimos.setSalariosMinimos(salariosMinimos);

            salariosMinimosList.add(funcionarioSalariosMinimos);
        }

        return salariosMinimosList;
    }


    
    public void restaurarListaPadrao() {
        funcionarioRepository.restaurarListaPadrao();
    }
    
    public FuncionarioDTO formatarFuncionarioDTO(Funcionario funcionario) {
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("#,##0.00");

        FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
        funcionarioDTO.setId(funcionario.getId());
        funcionarioDTO.setNome(funcionario.getNome());
        funcionarioDTO.setDataNascimentoFormatted(funcionario.getDataNascimento().format(dateFormatter));
        funcionarioDTO.setSalarioFormatted("R$ " + decimalFormat.format(funcionario.getSalario()));
        funcionarioDTO.setFuncao(funcionario.getFuncao());
        
        return funcionarioDTO;
    }

    public List<FuncionarioDTO> listarFuncionariosOrdenados(String sort) {
        List<Funcionario> funcionarios = funcionarioRepository.listarFuncionarios();

        Comparator<Funcionario> comparator = null;
        
        switch (sort) {
            case "nome":
                comparator = Comparator.comparing(Funcionario::getNome);
                break;
            case "dataNascimento":
                comparator = Comparator.comparing(Funcionario::getDataNascimento);
                break;
            case "salario":
                comparator = Comparator.comparing(Funcionario::getSalario);
                break;
            case "funcao":
                comparator = Comparator.comparing(Funcionario::getFuncao);
                break;
        }

        if (comparator != null) {
            funcionarios.sort(comparator);
        }
        
        return funcionarios.stream().map(this::formatarFuncionarioDTO).collect(Collectors.toList());
    }
    
    public Funcionario encontrarFuncionarioPorIdOriginal(Long id) {
        return funcionarioRepository.encontrarFuncionarioPorId(id)
                .orElse(null);
    }
    
    public List<FuncionarioDTO> buscarPorNome(String nome) {
        List<Funcionario> funcionarios = funcionarioRepository.getAll();
        List<Funcionario> funcionariosFiltrados = funcionarios.stream()
                .filter(funcionario -> funcionario.getNome().equalsIgnoreCase(nome))
                .collect(Collectors.toList());
        return funcionariosFiltrados.stream().map(this::formatarFuncionarioDTO).collect(Collectors.toList());
    }
    
    public Funcionario fromDTO(FuncionarioDTO dto) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(dto.getNome());
        pessoa.setDataNascimento(LocalDate.parse(dto.getDataNascimentoFormatted()));

        Funcionario funcionario = new Funcionario(pessoa, convertSalarioFormattedToBigDecimal(dto.getSalarioFormatted()), dto.getFuncao());
        return funcionario;
    }

    
    private BigDecimal convertSalarioFormattedToBigDecimal(String salarioFormatted) {
        // Remove todos os caracteres não numéricos, exceto o último ponto decimal
        String salarioUnformatted = salarioFormatted.replaceAll("[^\\d,]", "").replace(",", ".");
        return new BigDecimal(salarioUnformatted);
    }

}