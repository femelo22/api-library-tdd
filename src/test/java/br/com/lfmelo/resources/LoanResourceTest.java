package br.com.lfmelo.resources;


import br.com.lfmelo.dtos.LoanDTO;
import br.com.lfmelo.dtos.ReturnedLoanDTO;
import br.com.lfmelo.entities.Loan;
import br.com.lfmelo.resources.exception.BusinessException;
import br.com.lfmelo.services.BookService;
import br.com.lfmelo.services.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
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

import java.util.List;
import java.util.Optional;

import static br.com.lfmelo.factors.BookFactoryTest.buildSavedBook;
import static br.com.lfmelo.factors.LoanFactoryTest.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LoanResource.class)
@AutoConfigureMockMvc
public class LoanResourceTest {

    static String LOAN_API = "/api/loans";

    @Autowired
    MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private LoanService service;


    @Test
    @DisplayName("Deve realizar um emprestimo")
    public void createLoanTest() throws Exception {
        //cenario
        LoanDTO dto = buildLoanDTO();
        Loan loan = buildLoan();
        String json = new ObjectMapper().writeValueAsString(dto);
        BDDMockito.given( bookService.getBookByIsbn("123") ).willReturn(Optional.of(buildSavedBook()));
        BDDMockito.given( service.save(Mockito.any(Loan.class))).willReturn(loan);

        //execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        //validacao
        mvc
                .perform(request)
                .andExpect( status().isCreated() )
                .andExpect( content().string("1")); // Quando o retorno não é um json, usamos o content(). MockMvcResultMatchers
    }



    @Test
    @DisplayName("Deve retornar excecao para ISBN nao encontrdo ao realizar um emprestimo")
    public void invalidIsbnCreatedLoan() throws Exception {
        //cenario
        String msgError = "Book not found for passed isbn.";
        LoanDTO dto = buildLoanDTO();
        String json = new ObjectMapper().writeValueAsString(dto);
        BDDMockito.given( bookService.getBookByIsbn(dto.getIsbn()) ).willReturn( Optional.empty() );

        //execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        //validacao
        mvc
                .perform(request)
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", Matchers.hasSize(1)))
                .andExpect( jsonPath("errors[0]").value(msgError));
    }

    @Test
    @DisplayName("Deve retornar excecao para livro ja emprestado")
    public void bookAlreadyLoaned() throws Exception {
        //cenario
        String msgError = "Book already loaned";
        LoanDTO dto = buildLoanDTO();
        String json = new ObjectMapper().writeValueAsString(dto);
        BDDMockito.given( bookService.getBookByIsbn("123") ).willReturn(Optional.of(buildSavedBook()));
        BDDMockito.given( service.save(Mockito.any(Loan.class))).willThrow(new BusinessException(msgError));

        //execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LOAN_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        //validacao
        mvc
                .perform(request)
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", Matchers.hasSize(1)))
                .andExpect( jsonPath("errors[0]").value(msgError));
    }


    @Test
    @DisplayName("Deve retornar um livro")
    public void returnBookTest() throws Exception {

        //cenario
        Loan loan = buildLoan();
        ReturnedLoanDTO dto = new ReturnedLoanDTO(true);
        String json = new ObjectMapper().writeValueAsString(dto);
        BDDMockito.given( service.getById(1l)).willReturn(Optional.of(loan));

        //execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch(LOAN_API.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        //validacao
        mvc.perform(request)
                .andExpect( status().isOk() );

        Mockito.verify( service, Mockito.times(1)).update(loan);
    }


    @Test
    @DisplayName("Deve retornar 400 quando tentar devolver um livro inexistente.")
    public void returnNonExistentBookTest() throws Exception {

        //cenario
        ReturnedLoanDTO dto = new ReturnedLoanDTO(true);
        String json = new ObjectMapper().writeValueAsString(dto);
        BDDMockito.given( service.getById(1l)).willReturn(Optional.empty());

        //execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .patch(LOAN_API.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        //validacao
        mvc.perform(request)
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("Deve retornar todos os emprestimos")
    public void returnLoans() throws Exception {

        List<Loan> loans = buildLoanList();
        BDDMockito.given( service.findAll() ).willReturn(loans);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LOAN_API)
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect( status().isOk() );
//                .andExpect( content(), Matchers.hasSize(2)); //TODO: ARRUMAR VALIDACAO DO JSON
    }

}
