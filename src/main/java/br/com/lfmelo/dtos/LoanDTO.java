package br.com.lfmelo.dtos;

public class LoanDTO {

    private String isbn;

    private String customer;

    public LoanDTO() {

    }

    public LoanDTO(String isbn, String customer) {
        this.isbn = isbn;
        this.customer = customer;
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
