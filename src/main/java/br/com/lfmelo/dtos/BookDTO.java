package br.com.lfmelo.dtos;

import br.com.lfmelo.entities.Book;

import javax.validation.constraints.NotEmpty;


public class BookDTO {

    private Long id;

    @NotEmpty(message = "Title cannot be null")
    private String title;

    @NotEmpty(message = "Author cannot be null")
    private String author;

    @NotEmpty(message = "ISBN cannot be null")
    private String isbn;

    public BookDTO() {

    }

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
