package br.com.lfmelo.services.impl;

import br.com.lfmelo.entities.Loan;
import br.com.lfmelo.repositories.LoanRepository;
import br.com.lfmelo.resources.exception.BusinessException;
import br.com.lfmelo.resources.exception.NotFoundException;
import br.com.lfmelo.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if(repository.existsByBookAndNotReturned(loan.getBook())) {
            throw new BusinessException("Book already loaned");
        }

        return repository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return repository.save(loan);
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll();
    }
}
