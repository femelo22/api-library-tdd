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
    @DisplayName("Deve salvar um livro")
    public void saveBook() {
        Book book = buildNewBook();

        Book savedBook = repository.save(book);

        assertThat(savedBook.getId()).isNotNull();
    }

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
    @DisplayName("Deve retornar false quando não existir livro já cadastrado por isbn")
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

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBook() {
        //cenario
        Book book = buildNewBook();
        entityManager.persist(book); //primeiro temos que salvar um livro na base

        Book foundBook = entityManager.find(Book.class, book.getId()); //buscar o livro por id

        repository.delete(foundBook); //deletar o livro

        Book deletedBook = entityManager.find(Book.class, book.getId()); // tentar buscar novamente (Como foi deletado, não deve existir mais)
        assertThat(deletedBook).isNull(); //validação que não achou o livro na base
    }

}
