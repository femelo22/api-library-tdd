package br.com.lfmelo.resources;

import br.com.lfmelo.dtos.BookDTO;
import br.com.lfmelo.entities.Book;
import br.com.lfmelo.services.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static br.com.lfmelo.factors.BookFactoryTest.buildBookDTO;
import static br.com.lfmelo.factors.BookFactoryTest.buildSavedBook;
import static org.hamcrest.Matchers.hasSize;
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

    @MockBean // Mock para criar a instancia da nossa classe mockada, e colocar dentro do nosso contexto
    BookService service;

    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    public void createBookTest() throws Exception {
        BookDTO dto = buildBookDTO();
        Book savedBook = buildSavedBook();

        //Mockar o servico de salvar a entidade, junto com o que a funcao vai retornar
        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);

        //Recebe um objeto e transforma em Json
        String json = new ObjectMapper().writeValueAsString(dto);

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
                .andExpect( jsonPath("title").value(dto.getTitle()))
                .andExpect( jsonPath("author").value(dto.getAuthor()))
                .andExpect( jsonPath("isbn").value(dto.getIsbn()));
    }

    @Test
    @DisplayName("Deve lancar erro de validacao quando nao houver dados suficiente para criacao do livro.")
    public void createInvalidBookTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
//                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("id").isEmpty())
                .andExpect( jsonPath("title").isEmpty())
                .andExpect( jsonPath("author").isEmpty());

    }

}
