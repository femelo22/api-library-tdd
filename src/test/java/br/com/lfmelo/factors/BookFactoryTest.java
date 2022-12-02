package br.com.lfmelo.factors;

import br.com.lfmelo.dtos.BookDTO;

public class BookFactoryTest {

    public static BookDTO buildBookDTO() {
        BookDTO dto = new BookDTO();
        dto.setId(1l);
        dto.setAuthor("Autor Teste");
        dto.setTitle("Titulo Teste");
        dto.setIsbn("123456");

        return dto;
    }
}
