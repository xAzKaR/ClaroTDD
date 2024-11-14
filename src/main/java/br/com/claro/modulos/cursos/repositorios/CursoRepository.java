package br.com.claro.modulos.cursos.repositorios;

import br.com.claro.modulos.cursos.entidades.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    Curso findByName(String nome);
}
