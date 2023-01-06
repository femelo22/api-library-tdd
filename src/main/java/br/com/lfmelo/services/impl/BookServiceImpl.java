package br.com.lfmelo.services.impl;

import br.com.lfmelo.dtos.BookDTO;
import br.com.lfmelo.entities.Book;
import br.com.lfmelo.repositories.BookRepository;
import br.com.lfmelo.resources.exception.BusinessException;
import br.com.lfmelo.resources.exception.NotFoundException;
import br.com.lfmelo.services.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    /*
        Não vamos utilizar o Autowired, pois vamos precisar instanciar
        um Mock do nosso repository no teste
     */
    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())) { //query methods
            throw new BusinessException("Isbn already registered.");
        }
        return repository.save(book);
    }

    @Override
    public Book getById(Long id) {
        return repository.findById(id)
                        .orElseThrow(() -> new NotFoundException("User not found."));

    }

    @Override
    public void delete(Book book) {
        if(book == null || book.getId() == null) {
            throw new BusinessException("Book cannot be null");
        }
        repository.delete(book);
    }

    @Override
    public Book update(Long id, BookDTO dto) {
        Book book = getById(id);
        book.setId(id);
        book.setAuthor(dto.getAuthor());
        book.setTitle(dto.getTitle());
        repository.save(book);
        return book;
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        return null;
    }
}
