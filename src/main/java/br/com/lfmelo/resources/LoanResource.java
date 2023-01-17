package br.com.lfmelo.resources;

import br.com.lfmelo.dtos.LoanDTO;
import br.com.lfmelo.dtos.ReturnedLoanDTO;
import br.com.lfmelo.entities.Book;
import br.com.lfmelo.entities.Loan;
import br.com.lfmelo.resources.exception.BusinessException;
import br.com.lfmelo.services.BookService;
import br.com.lfmelo.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanResource {

    @Autowired
    private BookService bookService;

    @Autowired
    private LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDTO dto) {
        Book book = bookService.getBookByIsbn(dto.getIsbn())
                .orElseThrow(() -> new BusinessException("Book not found for passed isbn."));

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setCustomer(dto.getCustomer());
        loan.setLoanDate(LocalDate.now());

        loan = loanService.save(loan);

        return loan.getId();
    }


    @PatchMapping("/{id}")
    public void returnedBook(
            @PathVariable Long id,
            @RequestBody ReturnedLoanDTO dto) {
        Loan loan = loanService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        loan.setReturned(dto.getReturned());
        loanService.update(loan);
    }

    @GetMapping
    public List<Loan> findAll() {
        return loanService.findAll();
    }

}
