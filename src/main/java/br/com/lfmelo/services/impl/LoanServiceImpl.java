package br.com.lfmelo.services.impl;

import br.com.lfmelo.entities.Loan;
import br.com.lfmelo.repositories.LoanRepository;
import br.com.lfmelo.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        return repository.save(loan);
    }
}
