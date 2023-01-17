package br.com.lfmelo.dtos;

public class ReturnedLoanDTO {

    private boolean returned;

    public ReturnedLoanDTO() {

    }

    public ReturnedLoanDTO(boolean returned) {
        this.returned = returned;
    }

    public boolean getReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }
}
