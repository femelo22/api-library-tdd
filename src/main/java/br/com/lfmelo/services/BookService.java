package br.com.lfmelo.services;

import br.com.lfmelo.entities.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book book);

    Optional<Book> getById(Long id);
}
