package br.com.lfmelo.repositories;

import br.com.lfmelo.entities.Book;
import br.com.lfmelo.entities.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.lfmelo.factors.BookFactoryTest.buildNewBook;
import static br.com.lfmelo.factors.BookFactoryTest.buildSavedBook;
import static br.com.lfmelo.factors.LoanFactoryTest.buildLoan;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    LoanRepository repository;

    @Test
    @DisplayName("Deve verificar se existe emprestimo nao devolvido para o livro")
    public void existsByBookAndNotReturned() {

        //cenario
        Book book = buildNewBook();
        Loan loan = buildLoan();
        loan.setBook(book);
        entityManager.persist(book);
        entityManager.persist(loan);


        //execucao
        boolean exists = repository.existsByBookAndNotReturned(book);

        assertThat(exists).isTrue();
    }

}
