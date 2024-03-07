package com.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
}

