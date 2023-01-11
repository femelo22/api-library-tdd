package br.com.lfmelo.service;

import br.com.lfmelo.entities.Loan;
import br.com.lfmelo.repositories.LoanRepository;
import br.com.lfmelo.services.LoanService;
import br.com.lfmelo.services.impl.LoanServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;

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
        Mockito.when( repository.save(any(Loan.class))).thenReturn(loan);

        Loan savedLoan = service.save(loan);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
    }
}
