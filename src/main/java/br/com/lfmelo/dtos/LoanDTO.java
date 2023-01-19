package br.com.lfmelo.dtos;

import br.com.lfmelo.entities.Loan;

public class LoanDTO {

    private String isbn;

    private String customer;

    public LoanDTO() {

    }

    public LoanDTO(String isbn, String customer) {
        this.isbn = isbn;
        this.customer = customer;
    }

    public LoanDTO(Loan loan) {
        this.isbn = loan.getBook().getIsbn();
        this.customer = loan.getCustomer();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
