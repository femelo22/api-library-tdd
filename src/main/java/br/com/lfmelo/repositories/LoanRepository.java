package br.com.lfmelo.repositories;

import br.com.lfmelo.entities.Book;
import br.com.lfmelo.entities.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query(value = " select case when (count(l.id) > 0 ) then true else end " +
            " from Loan l where l.book = :book and ( l.returned is null or l.returned is false ) ", nativeQuery = true)
    boolean existsByBookAndNotReturned(@Param("book") Book book);

    Page<Loan> findByBook(Book book, Pageable pageable);
}
