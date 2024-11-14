package br.com.claro.modulos.cursos.servicos;

import br.com.claro.modulos.cursos.entidades.Curso;
import br.com.claro.modulos.cursos.repositorios.CursoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class CursoServiceImpl implements CursoService {

    private final CursoRepository repository;

    public Curso salvar(Curso curso) {
        if (curso == null) {
            throw new IllegalArgumentException("Curso não pode ser nulo.");
        }

        Curso cursoExistente = repository.findByName(curso.getNome());

        if (cursoExistente != null) {
            throw new IllegalArgumentException("Curso já existente.");
        }

        return repository.save(curso);
    }
}
