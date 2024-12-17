package com.example.taxstatement.repo;

import com.example.taxstatement.model.TaxStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxStatementRepository extends JpaRepository<TaxStatement, Long> {
}
