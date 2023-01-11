package br.com.lfmelo.factors;

import br.com.lfmelo.dtos.LoanDTO;
import br.com.lfmelo.entities.Book;
import br.com.lfmelo.entities.Loan;

import java.time.LocalDate;

import static br.com.lfmelo.factors.BookFactoryTest.buildSavedBook;

public class LoanFactoryTest {

    public static Loan buildLoan() {
        Book book = buildSavedBook();
        Loan loan = new Loan();
        loan.setId(1l);
        loan.setBook(book);
        loan.setCustomer("Luiz Fernando");
        loan.setReturned(false);
        loan.setLoanDate(LocalDate.now());
        return loan;
    }

    public static Loan buildLoanWithoutID() {
        Book book = buildSavedBook();
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setCustomer("Luiz Fernando");
        loan.setReturned(false);
        loan.setLoanDate(LocalDate.now());
        return loan;
    }

    public static LoanDTO buildLoanDTO() {
        return new LoanDTO("123", "Luiz Fernando");
    }
}
