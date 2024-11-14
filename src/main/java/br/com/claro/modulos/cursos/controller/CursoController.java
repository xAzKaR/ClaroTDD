package br.com.claro.modulos.cursos.controller;

import br.com.claro.modulos.cursos.entidades.Curso;
import br.com.claro.modulos.cursos.servicos.CursoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoServiceImpl cursoService;

    @PostMapping
    public Curso criarCurso(@RequestBody Curso curso) {
        Curso clienteSalvo = cursoService.salvar(curso);
        return ResponseEntity.ok().body(clienteSalvo).getBody();
    }
}
