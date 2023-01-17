package br.com.lfmelo.services;

import br.com.lfmelo.entities.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    List<Loan> findAll();
}
