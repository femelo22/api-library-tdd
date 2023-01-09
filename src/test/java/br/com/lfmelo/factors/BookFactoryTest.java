package br.com.lfmelo.factors;

import br.com.lfmelo.dtos.BookDTO;
import br.com.lfmelo.entities.Book;

import java.util.Arrays;
import java.util.List;

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

    public static Book buildNewBook() {
        Book book = new Book();
        book.setAuthor("Autor Teste");
        book.setTitle("Titulo Teste");
        book.setIsbn("123456");
        return book;
    }

    public static Book buildUpdateBook() {
        Book book = new Book();
        book.setId(1l);
        book.setAuthor("Autor");
        book.setTitle("Titulo");
        book.setIsbn("321");
        return book;
    }

    public static List<Book> buildSavedBooksList() {
        Book book1 = buildSavedBook();
        Book book2 = buildSavedBook();
        Book book3 = buildSavedBook();

        return Arrays.asList(book1, book2, book3);
    }
}
