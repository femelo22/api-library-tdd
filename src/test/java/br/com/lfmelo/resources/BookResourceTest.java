package br.com.lfmelo.resources;

import br.com.lfmelo.dtos.BookDTO;
import br.com.lfmelo.entities.Book;
import br.com.lfmelo.resources.exception.BusinessException;
import br.com.lfmelo.resources.exception.NotFoundException;
import br.com.lfmelo.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static br.com.lfmelo.factors.BookFactoryTest.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class) //cria um contexto de injeçao de dependencia
@ActiveProfiles("test") //rodar no perfil de test
@WebMvcTest
@AutoConfigureMockMvc //O Spring configura um objeto pra fazermos as requisiçoes
public class BookResourceTest {

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

    @Test //validaçao de integridade do objeto
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
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", hasSize(3)));
    }


    @Test
    @DisplayName("Erro ao cadastrar livro com ISBN já existente")
    public void createBookWithDuplicatedIsbn() throws Exception {
        BookDTO dto = buildBookDTO();

        String msgError = "Isbn already registered.";
        String json = new ObjectMapper().writeValueAsString(dto);

        //Simular que o servico mandou a mensagem de erro
        BDDMockito.given(service.save(Mockito.any(Book.class))).willThrow(new BusinessException(msgError));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", hasSize(1)))
                .andExpect( jsonPath("errors[0]").value(msgError));
    }


    @Test
    @DisplayName("Deve obter informacoes de um livro por ID")
    public void returnBookById() throws Exception {
        //cenario (given)
        Long id = 1l;
        Book book = buildSavedBook();
        BDDMockito.given(service.getById(id)).willReturn(book);

        //execucao (when)
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect( status().isOk() )
                .andExpect( jsonPath("id").value(id)) //pegar uma propriedade da resposta
                .andExpect( jsonPath("title").value(book.getTitle()))
                .andExpect( jsonPath("author").value(book.getAuthor()))
                .andExpect( jsonPath("isbn").value(book.getIsbn()));
    }

    @Test
    @DisplayName("Livro não encontrado por ID")
    public void livroNotFoundById() throws Exception {
        String msgError = "User not found.";
        Book book = buildSavedBook();

        BDDMockito.given(service.getById(1l)).willThrow(new NotFoundException(msgError));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + 1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void shouldDeleteABook() throws Exception {
        Book book = buildSavedBook();
        BDDMockito.given( service.getById(anyLong())).willReturn(book);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1));

        mvc
                .perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar not found quando não encontrar o livro para deletar")
    public void shouldExceptionWhenDeleteABook() throws Exception {
        Book book = buildSavedBook();

        BDDMockito.given( service.getById(anyLong())).willReturn(null);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1))
                .contentType(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar livro")
    public void updateBookTest() throws Exception {
        Long id = 1l;
        Book updateBook = buildUpdateBook();
        BookDTO dto = buildBookDTO();
        String json = new ObjectMapper().writeValueAsString(dto);
        BDDMockito.given( service.getById(id) ).willReturn(updateBook);
        BDDMockito.given( service.update(id, dto) ).willReturn(updateBook);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect( status().isOk() );
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar livro inexistente")
    public void updateInexistentBook() throws Exception {
        BookDTO dto = buildBookDTO();
        String json = new ObjectMapper().writeValueAsString(dto);
        BDDMockito.given(service.getById(1l)).willReturn(null);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect( status().isNotFound() );
    }


//    @Test
//    @DisplayName("Deve filtrar livros")
//    public void findBooksFilter() throws Exception {
//
//        long id = 1l;
//        Book book = buildSavedBook();
//        BDDMockito.given( service.findAll(Mockito.any(Pageable.class)) )
//                .willReturn( new PageImpl<Book>(Arrays.asList(book), PageRequest.of(0, 100), 1) );
//
//        //"api/books?"
//        String queryString = String.format("?title=%s&author=%s&page=0&size=100", book.getTitle(), book.getAuthor());
//
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
//                .get(BOOK_API.concat(queryString))
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mvc
//                .perform(request)
//                .andExpect( status().isOk() )
//                .andExpect( jsonPath("content", hasSize(1)))
//                .andExpect( jsonPath("totalElements").value(1))
//                .andExpect( jsonPath("pageble.pageSize").value(100))
//                .andExpect( jsonPath("pageble.pageNumber").value(0));
//    }





}
