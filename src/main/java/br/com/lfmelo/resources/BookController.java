package br.com.lfmelo.resources;

import br.com.lfmelo.dtos.BookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create() {
        BookDTO dto = new BookDTO();
        dto.setId(1l);
        dto.setAuthor("Autor");
        dto.setTitle("Meu Livro");
        dto.setIsbn("123123123");

        return dto;
    }
}
