package br.com.lfmelo.factors;

import br.com.lfmelo.dtos.BookDTO;
import br.com.lfmelo.entities.Book;

public class BookFactoryTest {

    public static BookDTO buildBookDTO() {
        BookDTO dto = new BookDTO();
        dto.setAuthor("Autor Teste");
        dto.setTitle("Titulo Teste");
        dto.setIsbn("123456");
        return dto;
    }

    public static Book buildSavedBook() {
        Book book = new Book();
        book.setId(1l);
        book.setAuthor("Autor Teste");
        book.setTitle("Titulo Teste");
        book.setIsbn("123456");
        return book;
    }
}
