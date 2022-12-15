package br.com.lfmelo.services.impl;

import br.com.lfmelo.entities.Book;
import br.com.lfmelo.repositories.BookRepository;
import br.com.lfmelo.resources.exception.BusinessException;
import br.com.lfmelo.services.BookService;
import org.springframework.stereotype.Service;

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

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())) { //query methods
            throw new BusinessException("Isbn already registered.");
        }
        return repository.save(book);
    }
}
