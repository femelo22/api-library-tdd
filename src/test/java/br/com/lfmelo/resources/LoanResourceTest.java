package br.com.lfmelo.resources;


import br.com.lfmelo.dtos.LoanDTO;
import br.com.lfmelo.entities.Loan;
import br.com.lfmelo.services.BookService;
import br.com.lfmelo.services.LoanService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static br.com.lfmelo.factors.BookFactoryTest.buildSavedBook;
import static br.com.lfmelo.factors.LoanFactoryTest.buildLoan;
import static br.com.lfmelo.factors.LoanFactoryTest.buildLoanDTO;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .andExpect(jsonPath("id").value(1l));

    }
}
