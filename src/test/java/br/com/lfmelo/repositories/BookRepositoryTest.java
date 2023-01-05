package br.com.lfmelo.repositories;

import br.com.lfmelo.entities.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static br.com.lfmelo.factors.BookFactoryTest.buildNewBook;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    /*
        @DataJpaTest
        - Indica que vamos fazer testes com JPA
        - Vai criar uma instancia do H2 para excutar os testes, e apagar tudo no final

        TestEntityManager
        - Vai simular um entity manager do JPA para fazer testes
        - Executa as operações na base de dados
     */


    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;


    @Test
    @DisplayName("Deve retornar true quando existir livro já cadastrado")
    public void returnTrueWhenIsbnExists() {
        //cenario
        String isbn = "123";
        Book book = buildNewBook();
        book.setIsbn(isbn);
        entityManager.persist(book);

        //execucao
        boolean exists = repository.existsByIsbn(isbn);

        //validacao
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando existir livro já cadastrado")
    public void returnFalseWhenIsbnExists() {
        //cenario
        String isbn = "123";

        //execucao
        boolean exists = repository.existsByIsbn(isbn);

        //validacao
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve retornar livro por ID")
    public void returnBookById() {
        Long id = 1l;
        Book book = buildNewBook();
        entityManager.persist(book);

        Optional<Book> bookSaved = repository.findById(id);

        assertThat(bookSaved.isPresent()).isTrue();
    }

}
