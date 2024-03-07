package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.BankRepository;
import com.model.Bank;

@Service
public class BankServiceImpl implements BankService {
	@Autowired
	private BankRepository bankRepo;

	@Override
	public Bank createBank(Bank bank) {
		return bankRepo.save(bank);
	}

	@Override
	public Bank getBankById(Long bankId) {
		return bankRepo.findById(bankId).orElse(null);
	}

	@Override
	public List<Bank> getAllBanks() {
		return bankRepo.findAll();
	}

	@Override
	public void deleteBank(Long bankId) {
		bankRepo.deleteById(bankId);
	}

	@Override
	public Bank updateBank(Bank bank) {
		if (bankRepo.existsById(bank.getId())) {
			return bankRepo.save(bank);
		} else {
			// Handle case where bank doesn't exist
			return null;
		}
	}

}
