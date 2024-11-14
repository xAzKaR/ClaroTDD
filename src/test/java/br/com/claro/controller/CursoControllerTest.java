package br.com.claro.controller;

import br.com.claro.modulos.cursos.controller.CursoController;
import br.com.claro.modulos.cursos.entidades.Curso;
import br.com.claro.modulos.cursos.servicos.CursoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CursoController.class)
@AutoConfigureMockMvc
class CursoControllerTest {

    static String ENDPOINT_URL = "/cursos";
    @MockBean
    CursoServiceImpl cursoService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldCreateNewCourseWhenValidDataProvided() throws Exception {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Java Programming");
        curso.setDescricao("Learn Java programming from scratch");
        curso.setDuracao(120);

        BDDMockito.given(cursoService.salvar(any(Curso.class))).willReturn(curso);

        String json = new ObjectMapper().writeValueAsString(curso);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(ENDPOINT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);


        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("nome").value(curso.getNome()))
                .andExpect(jsonPath("descricao").value(curso.getDescricao()))
                .andExpect(jsonPath("duracao").value(curso.getDuracao()));
    }
}
