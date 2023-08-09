package com.industria.sistema.controller;

import com.industria.sistema.dto.FuncionarioDTO;
import com.industria.sistema.service.FuncionarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final FuncionarioService funcionarioService;

    public HomeController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping("/api/funcionarios")
    public String home(Model model) {
        List<FuncionarioDTO> funcionarios = funcionarioService.listarFuncionarios();
        model.addAttribute("funcionarios", funcionarios);
        return "index";
    }
}
