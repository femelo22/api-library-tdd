package br.com.lfmelo.service;

import br.com.lfmelo.entities.Book;
import br.com.lfmelo.repositories.BookRepository;
import br.com.lfmelo.resources.exception.BusinessException;
import br.com.lfmelo.services.impl.BookServiceImpl;
import br.com.lfmelo.services.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.lfmelo.factors.BookFactoryTest.buildNewBook;
import static br.com.lfmelo.factors.BookFactoryTest.buildSavedBook;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl( repository );
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        //cenario
        Book newBook = buildNewBook();
        Book savedBook = buildSavedBook();
        Mockito.when( repository.existsByIsbn(Mockito.anyString()) ).thenReturn(false);
        Mockito.when( repository.save(newBook) ).thenReturn(savedBook);

        //execucao
        Book saved = service.save(newBook);

        //validacao
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAuthor()).isEqualTo("Autor Teste");
        assertThat(saved.getTitle()).isEqualTo("Titulo Teste");
        assertThat(saved.getIsbn()).isEqualTo("123456");
    }

    @Test
    @DisplayName("Deve lancar um erro de negocio ao tentar salvar livro com Isbn já registrado")
    public void shouldNotSavedBookWithDuplicatedIsbn() {
        //cenario
        Book book = buildNewBook();
        Mockito.when( repository.existsByIsbn( Mockito.anyString()) ).thenReturn(true); //simular que já existe um ISBN cadastrado

        //execucao
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        //verificacao
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn already registered.");

        Mockito.verify(repository, Mockito.never()).save(book); //validar que nao chamou o save do repository  ?? existe outras variantes interessantes do Mockito.never()
    }
}
