package com.industria.sistema.controller;

import com.industria.sistema.dto.FuncionarioDTO;
import com.industria.sistema.model.Funcionario;
import com.industria.sistema.model.FuncionarioSalariosMinimos;
import com.industria.sistema.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @Autowired
    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }


    @GetMapping("/")
    public ResponseEntity<List<FuncionarioDTO>> listarTodos(@RequestParam(required = false) String sort) {
        List<FuncionarioDTO> funcionarios;

        if (sort == null) {
            funcionarios = funcionarioService.listarFuncionarios();
        } else {
            funcionarios = funcionarioService.listarFuncionariosOrdenados(sort);
        }

        return new ResponseEntity<>(funcionarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> encontrarFuncionarioPorId(@PathVariable Long id) {
        FuncionarioDTO funcionario = funcionarioService.encontrarFuncionarioPorId(id);
        if (funcionario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(funcionario);
    }

    @PostMapping("/add")
    public ResponseEntity<FuncionarioDTO> salvarFuncionario(@Valid @RequestBody FuncionarioDTO funcionarioDTO) {
        Funcionario funcionarioParaSalvar = funcionarioService.fromDTO(funcionarioDTO);
        Funcionario novoFuncionario = funcionarioService.salvarFuncionario(funcionarioParaSalvar);
        FuncionarioDTO dto = funcionarioService.formatarFuncionarioDTO(novoFuncionario);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioDTO> atualizarFuncionario(@PathVariable Long id, @Valid @RequestBody FuncionarioDTO funcionarioDTO) {
        FuncionarioDTO funcionarioAtualizado = funcionarioService.atualizarFuncionario(funcionarioDTO);

        if (funcionarioAtualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(funcionarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerFuncionario(@PathVariable Long id) {
        funcionarioService.removerFuncionario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/aumento")
    public ResponseEntity<FuncionarioDTO> darAumento(@PathVariable Long id, @RequestParam double percentual) {
        FuncionarioDTO funcionarioComAumento = funcionarioService.darAumento(id, percentual);
        if (funcionarioComAumento != null) {
            return ResponseEntity.ok(funcionarioComAumento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/funcao/{funcao}")
    public ResponseEntity<List<FuncionarioDTO>> listarFuncionariosPorFuncao(@PathVariable String funcao) {
        List<FuncionarioDTO> funcionariosPorFuncao = funcionarioService.listarFuncionariosPorFuncao(funcao);
        return ResponseEntity.ok(funcionariosPorFuncao);
    }

    @GetMapping("/agrupar-por-funcao")
    public ResponseEntity<?> agruparFuncionariosPorFuncao() {
        return ResponseEntity.ok(funcionarioService.agruparFuncionariosPorFuncao());
    }

    @GetMapping("/aniversariantes/{mes}")
    public ResponseEntity<List<FuncionarioDTO>> listarAniversariantesDoMes(@PathVariable int mes) {
        List<FuncionarioDTO> aniversariantesDoMes = funcionarioService.listarAniversariantesDoMes(mes);
        return ResponseEntity.ok(aniversariantesDoMes);
    }

    @GetMapping("/maior-idade")
    public ResponseEntity<FuncionarioDTO> encontrarFuncionarioMaiorIdade() {
        FuncionarioDTO funcionarioMaiorIdade = funcionarioService.encontrarFuncionarioMaiorIdade();
        return ResponseEntity.ok(funcionarioMaiorIdade);
    }

    @GetMapping("/ordenar-por-nome")
    public ResponseEntity<List<FuncionarioDTO>> ordenarFuncionariosPorNome() {
        List<FuncionarioDTO> funcionariosOrdenadosPorNome = funcionarioService.ordenarFuncionariosPorNome();
        return ResponseEntity.ok(funcionariosOrdenadosPorNome);
    }

    @GetMapping("/total-salarios")
    public ResponseEntity<BigDecimal> calcularTotalSalarios() {
        BigDecimal totalSalarios = funcionarioService.calcularTotalSalarios();
        return ResponseEntity.ok(totalSalarios);
    }
    
    @GetMapping("/buscarPorNome")
    public ResponseEntity<List<FuncionarioDTO>> buscarPorNome(@RequestParam String nome) {
        List<FuncionarioDTO> funcionarios = funcionarioService.buscarPorNome(nome);
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/restaurar")
    public ResponseEntity<String> restaurarListaPadrao() {
        funcionarioService.restaurarListaPadrao();
        return ResponseEntity.ok("Lista padrão restaurada com sucesso!");
    }
    
    @GetMapping("/salarios-minimos")
    public ResponseEntity<List<FuncionarioSalariosMinimos>> calcularSalariosMinimos() {
        List<FuncionarioSalariosMinimos> salariosMinimos = funcionarioService.calcularSalariosMinimos();
        return ResponseEntity.ok(salariosMinimos);
    }
}
