package br.com.claro.servicos;

import br.com.claro.modulos.cursos.entidades.Curso;
import br.com.claro.modulos.cursos.repositorios.CursoRepository;
import br.com.claro.modulos.cursos.servicos.CursoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @InjectMocks
    private CursoServiceImpl cursoService;

    @Mock
    private CursoRepository cursoRepository;

    @Test
    void deveCriarUmNovoCurso() {
        Curso curso = new Curso();
        curso.setId(1235L);
        curso.setNome("Teste de Nome do Curso");
        curso.setDuracao(120);
        curso.setDescricao("Teste de Criação de Curso");

        Mockito.when(cursoRepository.save(Mockito.any(Curso.class))).thenReturn(curso);

        Curso cursoCriado = cursoService.salvar(curso);

        Mockito.verify(cursoRepository, Mockito.times(1)).save(Mockito.any(Curso.class));

        Assertions.assertNotNull(cursoCriado);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar salvar um curso com nome existente")
    void deveLancarExcecaoAoSalvarCursoComNomeExistente() {
        Curso cursoExistente = new Curso();
        cursoExistente.setNome("Curso Existente");

        Curso novoCurso = new Curso();
        novoCurso.setNome("Curso Existente");

        Mockito.when(cursoRepository.findByName("Curso Existente")).thenReturn(cursoExistente);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            cursoService.salvar(novoCurso);
        });

        Assertions.assertEquals("Curso já existente.", exception.getMessage());
        Mockito.verify(cursoRepository, Mockito.times(1)).findByName("Curso Existente");
        Mockito.verify(cursoRepository, Mockito.never()).save(Mockito.any(Curso.class));
    }

    @Test
    @DisplayName("Deve salvar um novo curso quando o nome não existe")
    void deveSalvarNovoCursoQuandoNomeNaoExiste() {
        Curso novoCurso = new Curso();
        novoCurso.setNome("Novo Curso");
        novoCurso.setDuracao(80);
        novoCurso.setDescricao("Descrição do Novo Curso");

        Mockito.when(cursoRepository.findByName("Novo Curso")).thenReturn(null);
        Mockito.when(cursoRepository.save(Mockito.any(Curso.class))).thenReturn(novoCurso);

        Curso cursoSalvo = cursoService.salvar(novoCurso);

        Assertions.assertNotNull(cursoSalvo);
        Assertions.assertEquals("Novo Curso", cursoSalvo.getNome());
        Mockito.verify(cursoRepository, Mockito.times(1)).findByName("Novo Curso");
        Mockito.verify(cursoRepository, Mockito.times(1)).save(Mockito.any(Curso.class));
    }

    @Test
    @DisplayName("Deve lançar NullPointerException ao tentar salvar um curso nulo")
    void deveLancarExcecaoAoSalvarCursoNulo() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            cursoService.salvar(null);
        });

        Assertions.assertEquals("Curso não pode ser nulo.", exception.getMessage());
        Mockito.verify(cursoRepository, Mockito.never()).findByName(Mockito.anyString());
        Mockito.verify(cursoRepository, Mockito.never()).save(Mockito.any(Curso.class));
    }

    @Test
    @DisplayName("Deve verificar se o curso salvo tem as mesmas propriedades do curso de entrada")
    void deveVerificarPropriedadesDoCursoSalvo() {
        Curso novoCurso = new Curso();
        novoCurso.setNome("Curso de Teste");
        novoCurso.setDuracao(100);
        novoCurso.setDescricao("Descrição do Curso de Teste");

        Mockito.when(cursoRepository.findByName("Curso de Teste")).thenReturn(null);
        Mockito.when(cursoRepository.save(Mockito.any(Curso.class))).thenReturn(novoCurso);

        Curso cursoSalvo = cursoService.salvar(novoCurso);

        Assertions.assertNotNull(cursoSalvo);
        Assertions.assertEquals(novoCurso.getNome(), cursoSalvo.getNome());
        Assertions.assertEquals(novoCurso.getDuracao(), cursoSalvo.getDuracao());
        Assertions.assertEquals(novoCurso.getDescricao(), cursoSalvo.getDescricao());

        Mockito.verify(cursoRepository, Mockito.times(1)).findByName("Curso de Teste");
        Mockito.verify(cursoRepository, Mockito.times(1)).save(Mockito.any(Curso.class));
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando o repository.save lança uma exceção")
    void deveLancarExcecaoQuandoRepositorySaveFalha() {
        Curso novoCurso = new Curso();
        novoCurso.setNome("Curso Teste");
        novoCurso.setDuracao(60);
        novoCurso.setDescricao("Descrição do Curso Teste");

        Mockito.when(cursoRepository.findByName("Curso Teste")).thenReturn(null);
        Mockito.when(cursoRepository.save(Mockito.any(Curso.class))).thenThrow(new RuntimeException("Erro ao salvar"));

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            cursoService.salvar(novoCurso);
        });

        Assertions.assertEquals("Erro ao salvar", exception.getMessage());
        Mockito.verify(cursoRepository, Mockito.times(1)).findByName("Curso Teste");
        Mockito.verify(cursoRepository, Mockito.times(1)).save(Mockito.any(Curso.class));
    }

    @Test
    @DisplayName("Deve verificar que nenhuma operação de salvamento ocorre quando um curso com o mesmo nome existe")
    void deveVerificarQueNaoSalvaQuandoCursoComMesmoNomeExiste() {
        Curso cursoExistente = new Curso();
        cursoExistente.setNome("Curso Existente");

        Curso novoCurso = new Curso();
        novoCurso.setNome("Curso Existente");

        Mockito.when(cursoRepository.findByName("Curso Existente")).thenReturn(cursoExistente);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            cursoService.salvar(novoCurso);
        });

        Assertions.assertEquals("Curso já existente.", exception.getMessage());
        Mockito.verify(cursoRepository, Mockito.times(1)).findByName("Curso Existente");
        Mockito.verify(cursoRepository, Mockito.never()).save(Mockito.any(Curso.class));
    }

    @Test
    @DisplayName("Deve salvar um curso quando o repository retorna null para findByName")
    void deveSalvarCursoQuandoRepositoryRetornaNullParaFindByName() {
        Curso novoCurso = new Curso();
        novoCurso.setNome("Curso Inexistente");
        novoCurso.setDuracao(90);
        novoCurso.setDescricao("Descrição do Curso Inexistente");

        Mockito.when(cursoRepository.findByName("Curso Inexistente")).thenReturn(null);
        Mockito.when(cursoRepository.save(Mockito.any(Curso.class))).thenReturn(novoCurso);

        Curso cursoSalvo = cursoService.salvar(novoCurso);

        Assertions.assertNotNull(cursoSalvo);
        Assertions.assertEquals("Curso Inexistente", cursoSalvo.getNome());
        Mockito.verify(cursoRepository, Mockito.times(1)).findByName("Curso Inexistente");
        Mockito.verify(cursoRepository, Mockito.times(1)).save(Mockito.any(Curso.class));
    }

    @Test
    @DisplayName("Deve garantir que o serviço não modifica o objeto curso de entrada")
    void deveGarantirQueServicoNaoModificaObjetoCursoDeEntrada() {
        Curso cursoEntrada = new Curso();
        cursoEntrada.setNome("Curso Teste");
        cursoEntrada.setDuracao(100);
        cursoEntrada.setDescricao("Descrição do Curso Teste");

        Curso cursoSalvo = new Curso();
        cursoSalvo.setId(1L);
        cursoSalvo.setNome("Curso Teste");
        cursoSalvo.setDuracao(100);
        cursoSalvo.setDescricao("Descrição do Curso Teste");

        Mockito.when(cursoRepository.findByName("Curso Teste")).thenReturn(null);
        Mockito.when(cursoRepository.save(Mockito.any(Curso.class))).thenReturn(cursoSalvo);

        Curso resultado = cursoService.salvar(cursoEntrada);

        Assertions.assertNotSame(cursoEntrada, resultado);
        Assertions.assertEquals(cursoEntrada.getNome(), resultado.getNome());
        Assertions.assertEquals(cursoEntrada.getDuracao(), resultado.getDuracao());
        Assertions.assertEquals(cursoEntrada.getDescricao(), resultado.getDescricao());
        Assertions.assertNull(cursoEntrada.getId());
        Assertions.assertNotNull(resultado.getId());

        Mockito.verify(cursoRepository, Mockito.times(1)).findByName("Curso Teste");
        Mockito.verify(cursoRepository, Mockito.times(1)).save(Mockito.any(Curso.class));
    }

    @Test
    @DisplayName("Deve verificar que o serviço retorna o objeto exato retornado pelo método save do repositório")
    void deveRetornarObjetoExatoDoRepositorioSave() {
        Curso novoCurso = new Curso();
        novoCurso.setNome("Novo Curso");
        novoCurso.setDuracao(120);
        novoCurso.setDescricao("Descrição do Novo Curso");

        Curso cursoSalvo = new Curso();
        cursoSalvo.setId(1L);
        cursoSalvo.setNome("Novo Curso");
        cursoSalvo.setDuracao(120);
        cursoSalvo.setDescricao("Descrição do Novo Curso");

        Mockito.when(cursoRepository.findByName("Novo Curso")).thenReturn(null);
        Mockito.when(cursoRepository.save(Mockito.any(Curso.class))).thenReturn(cursoSalvo);

        Curso resultado = cursoService.salvar(novoCurso);

        Assertions.assertSame(cursoSalvo, resultado);
        Mockito.verify(cursoRepository, Mockito.times(1)).findByName("Novo Curso");
        Mockito.verify(cursoRepository, Mockito.times(1)).save(Mockito.any(Curso.class));
    }

    @DisplayName("Alguma coisa")
    @ParameterizedTest
    @MethodSource("testDivisionInputParameters")
    void testDivision(int dividendo, int divisor, int expectedResult){

        int result = Math.floorDiv(dividendo, divisor);

        Assertions.assertEquals(expectedResult, result);
    }

    @DisplayName("Deve verificar que o serviço retorna o objeto exato retornado pelo método save do repositório")
    @ParameterizedTest
    @MethodSource("testCurso")
    void deveRetornarObjetosExatosDoRepositorioSave(Curso novoCurso) {
        Curso cursoSalvo = new Curso();
        cursoSalvo.setId(1L);
        cursoSalvo.setNome("Curso 1");
        cursoSalvo.setDuracao(120);
        cursoSalvo.setDescricao("Descrição do Novo Curso");

        Mockito.when(cursoRepository.findByName(novoCurso.getNome())).thenReturn(null);
        Mockito.when(cursoRepository.save(Mockito.any(Curso.class))).thenReturn(novoCurso);

        Curso resultado = cursoService.salvar(novoCurso);

        Assertions.assertSame(novoCurso, resultado);
        Mockito.verify(cursoRepository, Mockito.times(1)).findByName(novoCurso.getNome());
        Mockito.verify(cursoRepository, Mockito.times(1)).save(Mockito.any(Curso.class));
    }

    @DisplayName("Deve verificar que o serviço retorna o objeto exato retornado um erro de exception")
    @ParameterizedTest
    @MethodSource("testCursoComExcetion")
    void deveRetornarObjetosComUmaExceptionRepositorioSave(Curso novoCurso) {
        Curso cursoSalvo = new Curso();
        cursoSalvo.setId(1L);
        cursoSalvo.setNome("Novo Curso");
        cursoSalvo.setDuracao(120);
        cursoSalvo.setDescricao("Descrição do Novo Curso");
    
        Mockito.when(cursoRepository.findByName(novoCurso.getNome())).thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(IllegalArgumentException.class, () -> cursoService.salvar(novoCurso));
        Mockito.verify(cursoRepository, Mockito.times(1)).findByName(novoCurso.getNome());
        Mockito.verify(cursoRepository, Mockito.never()).save(Mockito.any(Curso.class));
    }

    public static Stream<Arguments> testDivisionInputParameters(){
        return Stream.of(
                Arguments.of(10, 2, 5),
                Arguments.of(15, 3, 5),
                Arguments.of(20, 4, 5)
        );
    }

    public static Stream<Curso> testCurso(){
        return Stream.of(
                new Curso(1L, "Curso 1", "Descrição do Curso 1", 100),
                new Curso(2L, "Curso 2", "Descrição do Curso 2", 150),
                new Curso(3L, "Curso 3", "Descrição do Curso 3", 200)
        );
    }

    public static Stream<Curso> testCursoComExcetion(){
        return Stream.of(
                new Curso(2L, "Curso 2", "Descrição do Curso 2", 100),
                new Curso(3L, "Curso 3", "Descrição do Curso 3", 100)
        );
    }
}
