package br.com.lfmelo.resources;

import br.com.lfmelo.dtos.BookDTO;
import br.com.lfmelo.entities.Book;
import br.com.lfmelo.services.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService service;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
        Book entity = modelMapper.map(dto, Book.class);
        entity = service.save(entity);
        return modelMapper.map(entity, BookDTO.class);
    }

    @GetMapping("/{id}")
    public BookDTO get(@PathVariable Long id) {
        Book book = service.getById(id);
        return modelMapper.map(book, BookDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        Book book = service.getById(id);
        service.delete(book);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book updateBook(@PathVariable Long id, @RequestBody BookDTO dto) {
        return service.update(id, dto);
    }

//    @GetMapping
//    public Page<Book> findPagination(Pageable pageRequest) {
//        Page<Book> result = service.findAll(pageRequest);
//        List<Book> list = result.getContent()
//                .stream()
//                .map( book -> modelMapper.map(book, Book.class))
//                .collect(Collectors.toList());
//
//        return new PageImpl<Book>( list, pageRequest, result.getTotalElements() );
//    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> findAllBooks() {
        return service.findAll();
    }
}
