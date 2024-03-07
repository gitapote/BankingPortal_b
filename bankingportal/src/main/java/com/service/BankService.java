package com.service;

import java.util.List;

import com.model.Bank;

public interface BankService {
	Bank createBank(Bank bank);

	Bank getBankById(Long bankId);

	List<Bank> getAllBanks();

	void deleteBank(Long bankId);

	Bank updateBank(Bank bank);
}
