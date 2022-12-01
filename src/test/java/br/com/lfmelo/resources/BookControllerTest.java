package br.com.lfmelo.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class) //cria um contexto de injeçao de dependencia
@ActiveProfiles("test") //rodar no perfil de test
@WebMvcTest
@AutoConfigureMockMvc //O Spring configura um objeto pra fazermos as requisiçoes
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc; //Ele vai simular as requisiçoes da nossa api

    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    public void createBookTest() throws Exception {

        //Recebe um objeto e transforma em Json
        String json = new ObjectMapper().writeValueAsString(null);

        //Mockar uma requisição
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        //Fazer a requisição
        mvc
                .perform(request)
                .andExpect( status().isCreated() )
                .andExpect( jsonPath("id").isNotEmpty()) //pegar uma propriedade da resposta
                .andExpect( jsonPath("title").value("Meu Livro"))
                .andExpect( jsonPath("author").value("Autor"))
                .andExpect( jsonPath("isbn").value("123123123"));
    }

    @Test
    @DisplayName("Deve lancar erro de validacao quando nao houver dados suficiente para criacao do livro.")
    public void createInvalidBookTest() {

    }

}
