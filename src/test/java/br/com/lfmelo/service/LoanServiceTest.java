package br.com.lfmelo.service;

import br.com.lfmelo.entities.Loan;
import br.com.lfmelo.repositories.LoanRepository;
import br.com.lfmelo.resources.exception.BusinessException;
import br.com.lfmelo.services.LoanService;
import br.com.lfmelo.services.impl.LoanServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.lfmelo.factors.LoanFactoryTest.buildLoan;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    @MockBean
    private LoanService service;

    @MockBean
    private LoanRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new LoanServiceImpl( repository );
    }

    @Test
    @DisplayName("Deve salvar um emprestimo")
    public void saveLoanTest() {
        Loan loan = buildLoan();
        when(repository.existsByBookAndNotReturned(loan.getBook())).thenReturn(false);
        when( repository.save(any(Loan.class))).thenReturn(loan);

        Loan savedLoan = service.save(loan);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
    }


    @Test
    @DisplayName("Deve retornar excecao para livro ja emprestado")
    public void loadedBookTest() {
        Loan loan = buildLoan();
        String msgError = "Book already loaned";
        when(repository.existsByBookAndNotReturned(loan.getBook())).thenReturn(true);

        Throwable exception = catchThrowable(() -> service.save(loan));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage(msgError);

        Mockito.verify( repository, Mockito.never() ).save(loan);
    }
}
