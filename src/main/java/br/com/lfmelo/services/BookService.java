package br.com.lfmelo.services;

import br.com.lfmelo.dtos.BookDTO;
import br.com.lfmelo.entities.Book;

import java.util.Optional;

public interface BookService {
    Book save(Book book);

    Book getById(Long id);

    void delete(Book book);

    Book update(Long id, BookDTO dto);
}
