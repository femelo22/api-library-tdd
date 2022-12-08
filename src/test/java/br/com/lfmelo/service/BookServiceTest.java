package br.com.lfmelo.service;

import br.com.lfmelo.entities.Book;
import br.com.lfmelo.services.impl.BookServiceImpl;
import br.com.lfmelo.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.lfmelo.factors.BookFactoryTest.buildSavedBook;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    // TODO: ARRUMAR CLASSE

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl();
    }

    BookService service;

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        Book book = buildSavedBook();

        Book saved = service.save(book);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAuthor()).isEqualTo("Autor Teste");
        assertThat(saved.getTitle()).isEqualTo("Titulo Teste");
        assertThat(saved.getIsbn()).isEqualTo("123456");
    }
}
